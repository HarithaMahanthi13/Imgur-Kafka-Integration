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
import org.springframework.web.client.RestTemplate;

import java.util.Map;

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

    public String refreshAccessToken() {
        String url = "https://api.imgur.com/oauth2/token";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(org.springframework.http.MediaType.MULTIPART_FORM_DATA);

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("refresh_token", refreshToken);
        body.add("client_id", clientId);
        body.add("client_secret",clientSecret);
        body.add("grant_type", grantType);

        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(body, headers);

        ResponseEntity<Map> response = restTemplate.exchange(tokenEndpoint, HttpMethod.POST, requestEntity, Map.class);

        Map<String, Object> responseBody = response.getBody();
        if (responseBody != null) {
            return (String) responseBody.get("access_token");
        }
        return null;
    }
}
