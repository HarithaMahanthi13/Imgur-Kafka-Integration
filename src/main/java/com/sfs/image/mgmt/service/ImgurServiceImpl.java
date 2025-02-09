package com.sfs.image.mgmt.service;

import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StopWatch;

import com.sfs.image.mgmt.dto.ImgurResponse;
import com.sfs.image.mgmt.entity.Image;
import com.sfs.image.mgmt.entity.User;
import com.sfs.image.mgmt.repository.ImageRepository;
import com.sfs.image.mgmt.repository.UserRepository;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service("imgurService")
public class ImgurServiceImpl implements IImgurService {

    @Value("${imgur.client-id}")
    private String clientId;
    
    @Value("${imgur.image.upload.url}")
    private String imgurUploadURL;
    
    @Value("${imgur.image.delete.url}")
    private String imgurDeleteURL;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ImageRepository imageRepository;

    @Autowired
    private ImgurAuthService authService;

    /**
     * Uploads an image to Imgur.
     * 
     * This method uploads an image to Imgur, associates the image with a user, and saves the image metadata in the database.
     * 
     * @param file the image file to be uploaded
     * @param username the username of the user uploading the image
     * @param password the password of the user uploading the image
     * @return the saved Image entity with metadata
     * @throws IOException if an I/O error occurs
     */
    public Image uploadImage(MultipartFile file, String username, String password) throws IOException {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        User user = authenticateUser(username, password);
        final StringBuilder accessToken = new StringBuilder();
        String accessToken1 = authService.refreshAccessToken();
      //  accessToken.append("Bearer").append(accessToken1);
        log.info("accessToken1 is: {}",accessToken1);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        headers.set("Authorization", "Client-ID " + clientId);
       // headers.set("Authorization", accessToken1);

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("image", new ByteArrayResource(file.getBytes()));

        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);
        Image image = new Image();
        try {
        	log.info("requestentity: {}",requestEntity);
        	String curlCommand = generateCurlCommand(imgurUploadURL, headers, body);
        	log.info("requestentity: {}",curlCommand);
        	ResponseEntity<String> response1 = restTemplate.postForEntity(imgurUploadURL, requestEntity, String.class);
            log.info("imgurresposne is1: {}",response1);
            ResponseEntity<ImgurResponse> response = restTemplate.postForEntity(imgurUploadURL, requestEntity, ImgurResponse.class);
           log.info("imgurresposne is: {}",response);
            if (response.getStatusCode() == HttpStatus.OK) {
                ImgurResponse responseBody = response.getBody();
                
                image.setImgurId(responseBody.getData().getId());
                image.setLink(responseBody.getData().getLink());
                image.setDeleteHash(responseBody.getData().getDeletehash());
                image.setTooluser(user);
                imageRepository.save(image);
            } else {
                log.error("Failed to upload image. Status code:: " + response.getStatusCode());
            }
        } catch (HttpClientErrorException e) {
            log.error("Client error: " + e.getStatusCode());
            log.error("Error body: " + e.getResponseBodyAsString());
        } catch (HttpServerErrorException e) {
            log.error("Server error: " + e.getStatusCode());
            log.error("Error body: " + e.getResponseBodyAsString());
        } catch (RestClientException e) {
            log.error("Error occurred: " + e.getMessage());
        } finally {
            stopWatch.stop();
            log.info("Time taken to upload image to api: " + stopWatch.getTotalTimeMillis() + " ms");
        }
        return image;
    }
    private String generateCurlCommand(String url, HttpHeaders headers, MultiValueMap<String, Object> body) {
        StringBuilder curlCommand = new StringBuilder("curl -X POST ");
        curlCommand.append("\"").append(url).append("\"");

        // Add headers
        headers.forEach((key, values) -> {
            for (String value : values) {
                curlCommand.append(" -H \"").append(key).append(": ").append(value).append("\"");
            }
        });

        // Add body data (assuming it's for multipart form data)
        body.forEach((key, values) -> {
            for (Object value : values) {
                if (value instanceof ByteArrayResource) {
                    // Assuming a ByteArrayResource as the image
                    curlCommand.append(" -F \"").append(key).append("=@<file-path>\""); // Replace <file-path> with your file path
                } else {
                    curlCommand.append(" -F \"").append(key).append("=").append(value.toString()).append("\"");
                }
            }
        });

        return curlCommand.toString();
    }

    /**
     * Retrieves images for a user.
     * 
     * This method authenticates the user and retrieves all images associated with the user from the database.
     * 
     * @param username the username of the user whose images are to be retrieved
     * @param password the password of the user
     * @return a list of Image entities associated with the user
     */
    public List<Image> getUserImages(String username, String password) {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        List<Image> images = null;
        try {
            User user = authenticateUser(username, password);
            images = imageRepository.findByTooluser(user);
        } catch (RuntimeException e) {
            log.error("Exception occurred while fetching user images: " + e.getMessage(), e);
            throw e; // Re-throw the exception if you want it to propagate further
        } finally {
            stopWatch.stop();
            log.info("Time taken to get user images from DB: " + stopWatch.getTotalTimeMillis() + " ms");
        }

        return images;
    }

    /**
     * Deletes an image.
     * 
     * This method authenticates the user, verifies ownership of the image, deletes the image from Imgur, and removes the image metadata from the database.
     * 
     * @param id the ID of the image to be deleted
     * @param username the username of the user requesting the deletion
     * @param password the password of the user requesting the deletion
     */
    public String deleteImage(Long id, String username, String password,String delethash) {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        try {
            User user = authenticateUser(username, password);
            Image image = imageRepository.findById(id).orElseThrow(() -> new RuntimeException("Image not found"));
         
            if (!image.getTooluser().equals(user)) {
                throw new RuntimeException("You are not authorized to delete this image");
            }

            final StringBuilder accessToken = new StringBuilder();
            String accessToken1 = authService.refreshAccessToken();
            log.info("accessToken1 delet: {}",accessToken1);
            accessToken.append("Client-ID ").append(clientId);
            log.info("authenticateUser delet: {}",accessToken.toString() );
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", accessToken.toString());
            
            HttpEntity<String> entity = new HttpEntity<>(headers);
            StringBuilder imageurl = new StringBuilder();
            imageurl.append(imgurDeleteURL);
            imageurl.append("/");
            imageurl.append(delethash);
            log.info("deleteURL: {}", imageurl.toString());
            try {
            	restTemplate.exchange(imageurl.toString(), HttpMethod.DELETE, entity, String.class);
                imageRepository.delete(image);
                log.info("Image deleted successfully.");
            } catch (HttpClientErrorException e) {
                log.error("Client error: " + e.getStatusCode());
                log.error("Error body: " + e.getResponseBodyAsString());
                throw new RuntimeException("Client error: " + e.getMessage(), e);
            } catch (HttpServerErrorException e) {
                log.error("Server error: " + e.getStatusCode());
                log.error("Error body: " + e.getResponseBodyAsString());
                throw new RuntimeException("Server error: " + e.getMessage(), e);
            } catch (RestClientException e) {
                log.error("Error occurred: " + e.getMessage());
                throw new RuntimeException("Error occurred: " + e.getMessage(), e);
            }

        } catch (RuntimeException e) {
            log.error("Exception occurred while deleting image: " + e.getMessage(), e);
            throw e;
        } finally {
            stopWatch.stop();
            log.info("Time taken to delete image: " + stopWatch.getTotalTimeMillis() + " ms");
        }
        return "image deleted successfully";
    }

    /**
     * Authenticates a user.
     * 
     * This method verifies the provided username and password against the stored user credentials in the database.
     * 
     * @param username the username of the user to be authenticated
     * @param password the password of the user to be authenticated
     * @return the authenticated User entity
     * @throws RuntimeException if authentication fails
     */
    private User authenticateUser(String username, String password) {
    	log.info("authenticateUser");
        Optional<User> optionalUser = userRepository.findBySfsUser(username);
        User user = optionalUser.orElseThrow(() -> new RuntimeException("Invalid username or password"));
        if (user != null && user.getSfsUserpassword().equals(password)) {
            return user;
        }
        throw new RuntimeException("Invalid username or password");
    }
}
