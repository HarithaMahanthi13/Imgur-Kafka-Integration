package com.sfs.image.mgmt.service;

import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

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
import org.springframework.stereotype.Service;
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

    @Autowired
    private RestTemplate restTemplate;
    
     

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ImageRepository imageRepository;
    
    @Autowired
    private ImgurAuthService authService;

    public Image uploadImage(MultipartFile file, String username, String password) throws IOException {
        User user = authenticateUser(username, password);
        
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        headers.set("Authorization", "Client-ID " + clientId);

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("image", new ByteArrayResource(file.getBytes()));

        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

        ResponseEntity<ImgurResponse> response = restTemplate.postForEntity("https://api.imgur.com/3/upload", requestEntity, ImgurResponse.class);

        Image image = new Image();
        image.setImgurId(response.getBody().getData().getId());
        image.setLink(response.getBody().getData().getLink());
        image.setUser(user);
        
        return imageRepository.save(image);
    }

    public List<Image> getUserImages(String username, String password) {
        User user = authenticateUser(username, password);
        return imageRepository.findByUser(user);
    }

    public void deleteImage(Long id, String username, String password) {
        User user = authenticateUser(username, password);
        
        Image image = imageRepository.findById(id).orElseThrow(() -> new RuntimeException("Image not found"));
        
        if (!image.getUser().equals(user)) {System.out.println("in not valid user");
        	
            throw new RuntimeException("You are not authorized to delete this image");
        }
        final StringBuilder accessToken = new StringBuilder();
        String accessToken1 = authService.refreshAccessToken();
        accessToken.append("Bearer ").append(accessToken1);
        System.out.println("Image ID ++++++++++++++++++++"+accessToken);
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization",accessToken.toString() );
        headers.set(username, password);
        System.out.println("in  valid user");
        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);
        System.out.println("Image ID ++++++++++++++++++++"+image.getImgurId());
        
        
        
        restTemplate.exchange("https://api.imgur.com/3/image/" + image.getImgurId(), HttpMethod.DELETE, requestEntity, Void.class);
        
        

        imageRepository.delete(image);
    }

    private User authenticateUser(String username, String password) {
    	 Optional<User> optionalUser = userRepository.findBySfsUser(username);
	     User user = optionalUser.orElseThrow(() -> new RuntimeException("Invalid username or password"));
        if (user != null && user.getSfsUserpassword().equals(password)) {
            return user;
        }
        throw new RuntimeException("Invalid username or password");
    }
}

