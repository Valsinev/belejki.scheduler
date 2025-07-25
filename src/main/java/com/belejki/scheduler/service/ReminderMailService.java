package com.belejki.scheduler.service;

import com.belejki.scheduler.dto.Reminder;
import com.belejki.scheduler.repository.ReminderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReminderMailService {

   private final ReminderRepository reminderRepository;
   private final MailService mailService;


   @Autowired
	public ReminderMailService(ReminderRepository reminderRepository, MailService mailService) {
		this.reminderRepository = reminderRepository;
		this.mailService = mailService;
	}

	@Scheduled(cron = "0 30 12 * * *") // Every day at 12:30
    public void sendMailForExpiringAfterMonthReminders() {

        List<Reminder> allExpiresAfterMonthTrue = reminderRepository.findAllExpiresAfterMonthTrue();
        if (allExpiresAfterMonthTrue == null || allExpiresAfterMonthTrue.isEmpty()) return;

        for (Reminder reminder: allExpiresAfterMonthTrue) {
            if (reminder.getUserUsername() != null && !reminder.isMonthMail()) {
                mailService.sendReminder(reminder, "month");
                //sets the flag for sended mail to prevent continuous mail sending
                reminder.setMonthMail(true);

                System.out.println("DONE MAIL SEND FOR EXPIRED AFTER MONTH REMINDERS");
            }
        }

        //update reminders
        reminderRepository.patchAll(allExpiresAfterMonthTrue);
        System.out.println("MONTH MAIL DONE");
    }


    @Scheduled(cron = "0 30 10 * * *")
    public void sendMailForExpiringAfterWeekReminders() {

        List<Reminder> allExpiresSoonTrue = reminderRepository.findAllExpiresSoonTrue();

        if (allExpiresSoonTrue == null || allExpiresSoonTrue.isEmpty()) return;

        for (Reminder reminder: allExpiresSoonTrue) {
            if (reminder.getUserUsername() != null && !reminder.isWeekMail() && !reminder.isExpiresToday()) {
                mailService.sendReminder(reminder, "week");
                reminder.setWeekMail(true);

                System.out.println("DONE MAIL SEND FOR EXPIRED AFTER WEEK REMINDERS");
            }
        }

        //update the reminder flags
        reminderRepository.patchAll(allExpiresSoonTrue);
        System.out.println("WEEK MAIL DONE");
    }


    @Scheduled(cron = "0 0 7 * * *") // Every day at 7:00
    public void sendMailForExpiringTodayReminders() {

        List<Reminder> allExpiresTodayTrue = reminderRepository.findAllExpiresTodayTrue();

        if (allExpiresTodayTrue == null || allExpiresTodayTrue.isEmpty()) return;

        for (Reminder reminder: allExpiresTodayTrue) {
            if (reminder.getUserUsername() != null && !reminder.isTodayMail()) {
                mailService.sendReminder(reminder, "today");
                reminder.setTodayMail(true);

                System.out.println("DONE MAIL SEND FOR EXPIRED AFTER TODAY REMINDERS");
            }
        }

        //update reminder flags
        reminderRepository.patchAll(allExpiresTodayTrue);

        System.out.println("TODAY MAIL DONE");
    }
}
