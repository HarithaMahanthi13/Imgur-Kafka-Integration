package com.sfs.image.mgmt.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.util.StopWatch;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

@Slf4j
@Service
public class ImgurAuthService {

    @Autowired
    private RestTemplate restTemplate;

    @Value("${oAuth.imgur.token.endPoint}")
    private String tokenEndpoint;

    @Value("${imgur.client-id}")
    private String clientId;

    @Value("${imgur.client-secret}")
    private String clientSecret;

    @Value("${oAuth.imgur.refresh.token}")
    private String refreshToken;

    @Value("${oAuth.imgur.grant.type}")
    private String grantType;
    
    /**
     * Refreshes the access token for Imgur API.
     *
     * This method sends a request to the Imgur token endpoint to refresh the access token
     * using the refresh token, client ID, client secret, and grant type provided in the application properties.
     * It logs the time taken for the API call and handles any client or server errors that may occur.
     *
     * @return the new access token as a String, or null if the request fails
     */
    public String refreshAccessToken() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        String url = "https://api.imgur.com/oauth2/token";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(org.springframework.http.MediaType.MULTIPART_FORM_DATA);

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("refresh_token", refreshToken);
        body.add("client_id", clientId);
        body.add("client_secret", clientSecret);
        body.add("grant_type", grantType);

        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(body, headers);
        String accessToken = null;

        try {
            ResponseEntity<Map> response = restTemplate.exchange(tokenEndpoint, HttpMethod.POST, requestEntity, Map.class);

            Map<String, Object> responseBody = response.getBody();
            if (responseBody != null) {
                accessToken = (String) responseBody.get("access_token");
            }
            log.info("Response from Imgur API: " + responseBody);
        } catch (HttpClientErrorException e) {
            log.error("Client error while refreshing access token: " + e.getStatusCode());
            log.error("Error body: " + e.getResponseBodyAsString());
        } catch (HttpServerErrorException e) {
            log.error("Server error while refreshing access token: " + e.getStatusCode());
            log.error("Error body: " + e.getResponseBodyAsString());
        } catch (RestClientException e) {
            log.error("Error occurred while refreshing access token: " + e.getMessage());
        } finally {
            stopWatch.stop();
            log.info("Time taken to refresh access token: " + stopWatch.getTotalTimeMillis() + " ms");
        }

        return accessToken;
    }
}
