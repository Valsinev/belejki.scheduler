package com.belejki.scheduler.service;


import com.belejki.scheduler.dto.Reminder;
import com.belejki.scheduler.repository.ReminderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class ReminderFlagUpdateService {

    private final ReminderRepository reminderRepository;

    @Autowired
    public ReminderFlagUpdateService(ReminderRepository reminderRepository) {
	    this.reminderRepository = reminderRepository;
    }

    //check the date of the reminders and sets flags if it expires after month, after week, or today
    @Scheduled(cron = "0 55 22 * * *") // Every day at 3:00
    public void updateRemindersFlags() {

        LocalDate today = LocalDate.now();
        LocalDate nextMonth = today.plusMonths(1);
        //all before next month
        List<Reminder> allExpiringBeforeNextMonth = reminderRepository.findAllExpiringBefore(nextMonth);

        if (allExpiringBeforeNextMonth == null || allExpiringBeforeNextMonth.isEmpty()) return;

        setExpirationFlags(allExpiringBeforeNextMonth, today);

        //update the reminders
        reminderRepository.patchAll(allExpiringBeforeNextMonth);
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

