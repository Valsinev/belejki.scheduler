package com.belejki.scheduler.repository;

import com.belejki.scheduler.dto.User;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository {
	void deleteAllNotConfirmed();

	void deleteAllSetForDeletion();

	void deleteAllNotLoggedForMonths(int months);
}
