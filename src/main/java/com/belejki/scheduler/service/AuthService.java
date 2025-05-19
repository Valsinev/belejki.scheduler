package com.belejki.scheduler.service;

import com.belejki.scheduler.config.AppConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
public class AuthService {

    private final RestTemplate restTemplate;
    private final AppConfig appConfig;

    @Autowired
    public AuthService(RestTemplate restTemplate, AppConfig appConfig) {
        this.restTemplate = restTemplate;
        this.appConfig = appConfig;
    }

    public String getJwtToken() {
        String loginUrl = appConfig.getBackendApiUrl() + "/login";
        Map<String, String> credentials = Map.of(
                "username", appConfig.getAuthUsername(),
                "password", appConfig.getAuthPassword()
        );

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, String>> request = new HttpEntity<>(credentials, headers);
        ResponseEntity<Map> response = restTemplate.postForEntity(loginUrl, request, Map.class);

        if (response.getStatusCode().is2xxSuccessful()) {
            return (String) response.getBody().get("token");
        } else {
            throw new RuntimeException("Failed to authenticate");
        }
    }
}

