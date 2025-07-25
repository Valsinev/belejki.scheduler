package com.belejki.scheduler.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Getter
public class AppConfig {

    @Value("${backend.api.url}")
    private String backendApiUrl;

    @Value("${auth.username}")
    private String authUsername;

    @Value("${auth.password}")
    private String authPassword;

    @Value("${delete.user.after.months.not.logged:12}") //default 12(1 year)
    private int months;

    public String getBackendApiUrl() {
        return backendApiUrl;
    }

    public String getAuthPassword() {
        return authPassword;
    }

    public String getAuthUsername() {
        return authUsername;
    }

    public int getMonths() {
        return months;
    }
}

