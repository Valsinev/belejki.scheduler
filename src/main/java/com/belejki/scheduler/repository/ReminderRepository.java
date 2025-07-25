package com.belejki.scheduler.repository;

import com.belejki.scheduler.dto.Reminder;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ReminderRepository {
	List<Reminder> findAllExpiringBefore(LocalDate cutoff);

	void patchAll(List<Reminder> allExpiringBefore);

	List<Reminder> findAllExpiresAfterMonthTrue();

	List<Reminder> findAllExpiresSoonTrue();

	List<Reminder> findAllExpiresTodayTrue();

}
