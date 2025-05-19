package com.belejki.scheduler.service;

import com.belejki.scheduler.config.AppConfig;
import com.belejki.scheduler.dto.Reminder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.Locale;

@Service
public class MailService {

    private final JavaMailSender mailSender;
    private final MessageSource messageSource;
    private final AppConfig appConfig;

    @Autowired
    public MailService(JavaMailSender mailSender, MessageSource messageSource, AppConfig appConfig) {
        this.mailSender = mailSender;
        this.messageSource = messageSource;
        this.appConfig = appConfig;
    }

    public void sendReminder(Reminder reminder, String type) {
        String localeStr = reminder.getLocale();
        Locale locale = localeStr != null ? Locale.forLanguageTag(localeStr) : Locale.ENGLISH;

        String subject = messageSource.getMessage("reminder.email.subject." + type, null, locale);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        String formattedDate = reminder.getExpiration().format(formatter);
        String body = messageSource.getMessage(
                "reminder.email.body." + type,
                new Object[]{reminder.getName(), formattedDate},
                locale
        );

        sendSimpleEmail(reminder.getUsername(), subject, body);

    }

    private void sendSimpleEmail(String to, String subject, String body) {

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(appConfig.getAuthUsername());
        message.setTo(to);
        message.setSubject(subject);
        message.setText(body);


        mailSender.send(message);

    }
}
