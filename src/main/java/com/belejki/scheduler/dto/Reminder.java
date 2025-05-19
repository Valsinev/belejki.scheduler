package com.belejki.scheduler.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class Reminder {
    private Long id;
    private LocalDate expiration;
    private boolean expired;
    private boolean expiresSoon;
    private boolean expiresToday;
    private boolean expiresAfterMonth;
    private String username;
    private String locale;
    private boolean monthMail;
    private boolean weekMail;
    private boolean todayMail;

    // getters and setters
}

