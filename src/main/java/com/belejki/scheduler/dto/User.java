package com.belejki.scheduler.dto;


import lombok.Data;

import java.time.LocalDate;

@Data
public class User {

    private Long id;
    private String username;
    private String firstName;
    private String lastName;
    private boolean enabled;
    private LocalDate lastLogin;
    private String password;

}
