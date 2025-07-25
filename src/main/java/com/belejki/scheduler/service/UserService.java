package com.belejki.scheduler.service;

import com.belejki.scheduler.config.AppConfig;
import com.belejki.scheduler.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

	private final UserRepository userRepository;
	private final AppConfig appConfig;

	@Autowired
	public UserService(UserRepository userRepository, AppConfig appConfig) {
		this.userRepository = userRepository;
		this.appConfig = appConfig;
	}

	//deletes not confirmed users
	@Scheduled(cron = "0 0 4 * * *") // Every day at 4:00
	public void clearNotConfirmedUsers() {

		userRepository.deleteAllNotConfirmed();

		System.out.println("DONE DELETING NOT CONFIRMED USERS.");
	}

	//deletes users with setForDeletion flag
	@Scheduled(cron = "0 0 5 * * *") // Every day at 5:00
	public void clearUsersWithSetForDeletionFlag() {

		userRepository.deleteAllSetForDeletion();

		System.out.println("DONE DELETING USERS WITH setForDeletionFlag.");
	}


	//deletes users not logged for decided period in months
	@Scheduled(cron = "0 0 6 * * *") // Every day at 6:00
	public void clearUsersNotLoggedForMonths() {

		userRepository.deleteAllNotLoggedForMonths(appConfig.getMonths());

		System.out.printf("DONE DELETING USERS NOT LOGGED FOR %d MONTHS.", appConfig.getMonths());
	}


}
