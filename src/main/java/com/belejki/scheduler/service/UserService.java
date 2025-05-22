package com.belejki.scheduler.service;

import com.belejki.scheduler.config.AppConfig;
import com.belejki.scheduler.dto.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class UserService {

    private final RestTemplate restTemplate;
    private final AppConfig appConfig;
    private final AuthService authService;

    @Autowired
    public UserService(RestTemplate restTemplate, AppConfig appConfig, AuthService authService) {
        this.restTemplate = restTemplate;
        this.appConfig = appConfig;
        this.authService = authService;
    }




    //deletes not confirmed users
    @Scheduled(cron = "0 0 4 * * *") // Every day at 4:00
    public void clearNotConfirmedUsers() {
        String token = authService.getJwtToken();
        String DELETE_URL = appConfig.getBackendApiUrl() + "/admin/users/not-confirmed";

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<User> getEntity = new HttpEntity<>(headers);


        ResponseEntity<PagedResponse<User>> response = restTemplate.exchange(
                DELETE_URL,
                HttpMethod.DELETE,
                getEntity,
                new ParameterizedTypeReference<PagedResponse<User>>() {}
        );

        List<User> users = response.getBody().getContent();

        System.out.println("FOUNDED NOT CONFIRMED USERS: ");
        users.forEach(System.out::println);
        System.out.println("DONE DELETING NOT CONFIRMED USERS.");
    }

    //deletes users with setForDeletion flag
    @Scheduled(cron = "0 0 5 * * *") // Every day at 5:00
    public void clearUsersWithSetForDeletionFlag() {
        String token = authService.getJwtToken();
        String DELETE_URL = appConfig.getBackendApiUrl() + "/admin/users/set-for-deletion";

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<User> getEntity = new HttpEntity<>(headers);


        ResponseEntity<PagedResponse<User>> response = restTemplate.exchange(
                DELETE_URL,
                HttpMethod.DELETE,
                getEntity,
                new ParameterizedTypeReference<PagedResponse<User>>() {}
        );

        List<User> users = response.getBody().getContent();

        System.out.println("FOUNDED setForDeletion USERS: ");
        users.forEach(System.out::println);
        System.out.println("DONE DELETING USERS WITH setForDeletionFlag.");
    }


    //deletes users not logged for 1 year
    @Scheduled(cron = "0 0 6 * * *") // Every day at 6:00
    public void clearUsersNotLoggedLongerThanAYear() {
        int months = 12; //one year
        String token = authService.getJwtToken();
        String DELETE_URL = appConfig.getBackendApiUrl() + "/admin/users/not-logged/" + months;

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<User> getEntity = new HttpEntity<>(headers);


        ResponseEntity<PagedResponse<User>> response = restTemplate.exchange(
                DELETE_URL,
                HttpMethod.DELETE,
                getEntity,
                new ParameterizedTypeReference<PagedResponse<User>>() {}
        );

        List<User> users = response.getBody().getContent();

        System.out.printf("FOUNDED USERS NOT LOGGED FOR %d MONTHS: ", months);
        System.out.println();
        users.forEach(System.out::println);
        System.out.printf("DONE DELETING USERS NOT LOGGED FOR %d MONTHS.", months);
    }



}
