package com.belejki.scheduler.service;

import com.belejki.scheduler.config.AppConfig;
import com.belejki.scheduler.dto.Reminder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.List;

@Service
public class ReminderMailService {

    private final RestTemplate restTemplate;
    private final AppConfig appConfig;
    private final AuthService authService;
    private final MailService mailService;

    @Autowired
    public ReminderMailService(RestTemplate restTemplate, AppConfig appConfig, AuthService authService, MailService mailService) {
        this.restTemplate = restTemplate;
        this.appConfig = appConfig;
        this.authService = authService;
        this.mailService = mailService;
    }

    @Scheduled(cron = "0 30 12 * * *") // Every day at 12:30
    public void sendMailForExpiringAfterMonthReminders() {
        String token = authService.getJwtToken();
        String READ_URL = appConfig.getBackendApiUrl() + "/admin/reminders/expires-month"; //?page=0&size=1000"; // paging optional
        String UPDATE_URL = appConfig.getBackendApiUrl() + "/schedule/reminders/patch";

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Void> getEntity = new HttpEntity<>(headers);


        ResponseEntity<PagedResponse<Reminder>> response = restTemplate.exchange(
                READ_URL,
                HttpMethod.GET,
                getEntity,
                new ParameterizedTypeReference<PagedResponse<Reminder>>() {}
        );

        if (checkIfRetrievingIsSuccessfull(response)) return;

        List<Reminder> reminders = response.getBody().getContent();
        if (checkIfRemindersEmptyOrNull(reminders)) return;

        for (Reminder reminder: reminders) {
            if (reminder.getUsername() != null && !reminder.isMonthMail()) {
                mailService.sendReminder(reminder, "month");
                reminder.setMonthMail(true);

                System.out.println("DONE MAIL SEND FOR EXPIRED AFTER MONTH REMINDERS");
            }
        }


        // Prepare PUT request with headers
        if (!reminders.isEmpty()) {
            HttpEntity<List<Reminder>> putEntity = new HttpEntity<>(reminders, headers);
            restTemplate.exchange(
                    UPDATE_URL,
                    HttpMethod.PUT,
                    putEntity,
                    Void.class
            );
        }
        System.out.println("MONTH MAIL DONE");
    }


    @Scheduled(cron = "0 30 12 * * *")
    public void sendMailForExpiringAfterWeekReminders() {
        String token = authService.getJwtToken();
        String READ_URL = appConfig.getBackendApiUrl() + "/admin/reminders/expires-soon"; //?page=0&size=1000"; // paging optional
        String UPDATE_URL = appConfig.getBackendApiUrl() + "/schedule/reminders/patch";

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Void> getEntity = new HttpEntity<>(headers);


        ResponseEntity<PagedResponse<Reminder>> response = restTemplate.exchange(
                READ_URL,
                HttpMethod.GET,
                getEntity,
                new ParameterizedTypeReference<PagedResponse<Reminder>>() {}
        );

        if (checkIfRetrievingIsSuccessfull(response)) return;

        List<Reminder> reminders = response.getBody().getContent();
        if (checkIfRemindersEmptyOrNull(reminders)) return;

        for (Reminder reminder: reminders) {
            if (reminder.getUsername() != null && !reminder.isWeekMail() && !reminder.isExpiresToday()) {
                mailService.sendReminder(reminder, "week");
                reminder.setWeekMail(true);

                System.out.println("DONE MAIL SEND FOR EXPIRED AFTER WEEK REMINDERS");
            }
        }

        // Prepare PUT request with headers
        if (!reminders.isEmpty()) {
            HttpEntity<List<Reminder>> putEntity = new HttpEntity<>(reminders, headers);
            restTemplate.exchange(
                    UPDATE_URL,
                    HttpMethod.PUT,
                    putEntity,
                    Void.class
            );
        }
        System.out.println("WEEK MAIL DONE");
    }


    @Scheduled(cron = "0 0 7 * * *") // Every day at 7:00
    public void sendMailForExpiringTodayReminders() {
        String token = authService.getJwtToken();
        String READ_URL = appConfig.getBackendApiUrl() + "/admin/reminders/expires-today"; //?page=0&size=1000"; // paging optional
        String UPDATE_URL = appConfig.getBackendApiUrl() + "/schedule/reminders/patch";

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Void> getEntity = new HttpEntity<>(headers);


        ResponseEntity<PagedResponse<Reminder>> response = restTemplate.exchange(
                READ_URL,
                HttpMethod.GET,
                getEntity,
                new ParameterizedTypeReference<PagedResponse<Reminder>>() {}
        );

        if (checkIfRetrievingIsSuccessfull(response)) return;

        List<Reminder> reminders = response.getBody().getContent();
        if (checkIfRemindersEmptyOrNull(reminders)) return;

        for (Reminder reminder: reminders) {
            if (reminder.getUsername() != null && !reminder.isTodayMail()) {
                mailService.sendReminder(reminder, "today");
                reminder.setTodayMail(true);

                System.out.println("DONE MAIL SEND FOR EXPIRED AFTER TODAY REMINDERS");
            }
        }


        // Prepare PUT request with headers
        if (!reminders.isEmpty()) {
            HttpEntity<List<Reminder>> putEntity = new HttpEntity<>(reminders, headers);
            restTemplate.exchange(
                    UPDATE_URL,
                    HttpMethod.PUT,
                    putEntity,
                    Void.class
            );
        }
        System.out.println("TODAY MAIL DONE");
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
}
