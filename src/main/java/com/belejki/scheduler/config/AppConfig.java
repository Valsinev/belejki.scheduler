package com.belejki.scheduler.config;

// AppProperties.java
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Getter
public class AppConfig {

    @Value("${backend.api.url}")
    private String backendApiUrl;

    public String getBackendApiUrl() {
        return backendApiUrl;
    }
}

