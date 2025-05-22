package com.belejki.scheduler.service;


import com.belejki.scheduler.config.AppConfig;
import com.belejki.scheduler.dto.Reminder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDate;
import java.util.List;

@Service
public class ReminderFlagUpdateService {

    private final RestTemplate restTemplate;
    private final AppConfig appConfig;
    private final AuthService authService;

    @Autowired
    public ReminderFlagUpdateService(RestTemplate restTemplate, AppConfig appConfig, AuthService authService) {
        this.restTemplate = restTemplate;
        this.appConfig = appConfig;
        this.authService = authService;
    }

    //check the date of the reminders and sets flags if it expires after month, after week, or today
    @Scheduled(cron = "0 0 3 * * *") // Every day at 3:00
    public void checkReminders() {
        String token = authService.getJwtToken();
        String READ_URL = appConfig.getBackendApiUrl() + "/schedule/reminders/flags-before"; //?page=0&size=1000"; // paging optional
        String UPDATE_URL = appConfig.getBackendApiUrl() + "/schedule/reminders/patch";

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Void> getEntity = new HttpEntity<>(headers);

        LocalDate today = LocalDate.now();
        LocalDate cutoff = today.plusMonths(1).plusDays(2);

        String urlWithParams = UriComponentsBuilder.fromHttpUrl(READ_URL)
                .queryParam("cutoff", cutoff.toString())
                .toUriString();

        ResponseEntity<PagedResponse<Reminder>> response = restTemplate.exchange(
                urlWithParams,
                HttpMethod.GET,
                getEntity,
                new ParameterizedTypeReference<PagedResponse<Reminder>>() {}
        );

        if (checkIfRetrievingIsSuccessfull(response)) return;

        List<Reminder> reminders = response.getBody().getContent();
        if (checkIfRemindersEmptyOrNull(reminders)) return;

        setExpirationFlags(reminders, today);

        // Prepare PUT request with headers
        HttpEntity<List<Reminder>> putEntity = new HttpEntity<>(reminders, headers);
        restTemplate.exchange(
                UPDATE_URL,
                HttpMethod.PUT,
                putEntity,
                Void.class
        );
        System.out.println("DONE UPDATING FLAGS");
    }

    private static boolean checkIfRetrievingIsSuccessfull(ResponseEntity<PagedResponse<Reminder>> response) {
        if (!response.getStatusCode().is2xxSuccessful() || response.getBody() == null) {
            System.out.println("Failed to fetch reminders: " + response.getStatusCode());
            return true;
        }
        return false;
    }

    private static boolean checkIfRemindersEmptyOrNull(List<Reminder> reminders) {
        if (reminders == null || reminders.isEmpty()) {
            return true;
        }
        return false;
    }

    private static void setExpirationFlags(List<Reminder> reminders, LocalDate today) {
        for (Reminder reminder : reminders) {
            LocalDate expiration = reminder.getExpiration();

            boolean expired = expiration.isBefore(today);
            boolean expiresToday = expiration.isEqual(today);
            boolean expiresSoon = !expired && !expiresToday && expiration.isBefore(today.plusDays(8));
            boolean expiresAfterMonth = expiration.isEqual(today.plusMonths(1));

            if (expired) {
                reminder.setExpired(true);
                reminder.setExpiresToday(false);
                reminder.setExpiresSoon(false);
                reminder.setExpiresAfterMonth(false);
            } else if (expiresToday) {
                reminder.setExpired(false);
                reminder.setExpiresToday(true);
                reminder.setExpiresSoon(true);
                reminder.setExpiresAfterMonth(false);
            } else if (expiresSoon) {
                reminder.setExpired(false);
                reminder.setExpiresToday(false);
                reminder.setExpiresSoon(true);
                reminder.setExpiresAfterMonth(false);
            } else if (expiresAfterMonth) {
                reminder.setExpired(false);
                reminder.setExpiresToday(false);
                reminder.setExpiresSoon(false);
                reminder.setExpiresAfterMonth(true);
            } else {
                reminder.setExpired(false);
                reminder.setExpiresToday(false);
                reminder.setExpiresSoon(false);
                reminder.setExpiresAfterMonth(false);
            }
        }
    }

}

