package com.belejki.scheduler.repository;

import com.belejki.scheduler.config.AppConfig;
import com.belejki.scheduler.dto.User;
import com.belejki.scheduler.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Repository
public class UserRepositoryImpl implements UserRepository {

	private final AuthService authService;
	private final AppConfig appConfig;
	private final RestTemplate restTemplate;

	@Autowired
	public UserRepositoryImpl(AuthService authService, AppConfig appConfig, RestTemplate restTemplate) {
		this.authService = authService;
		this.appConfig = appConfig;
		this.restTemplate = restTemplate;
	}

	@Override
	public void deleteAllNotConfirmed() {

		String token = authService.getJwtToken();
		String DELETE_URL = appConfig.getBackendApiUrl() + "/admin/users/not-confirmed";

		HttpHeaders headers = new HttpHeaders();
		headers.setBearerAuth(token);
		headers.setContentType(MediaType.APPLICATION_JSON);

		HttpEntity<User> getEntity = new HttpEntity<>(headers);


		restTemplate.exchange(
				DELETE_URL,
				HttpMethod.DELETE,
				getEntity,
				Void.class
		);
	}

	@Override
	public void deleteAllSetForDeletion() {

		String token = authService.getJwtToken();
		String DELETE_URL = appConfig.getBackendApiUrl() + "/admin/users/set-for-deletion";

		HttpHeaders headers = new HttpHeaders();
		headers.setBearerAuth(token);
		headers.setContentType(MediaType.APPLICATION_JSON);

		HttpEntity<User> getEntity = new HttpEntity<>(headers);


		restTemplate.exchange(
				DELETE_URL,
				HttpMethod.DELETE,
				getEntity,
				Void.class
		);

	}

	@Override
	public void deleteAllNotLoggedForMonths(int months) {

		String token = authService.getJwtToken();
		String DELETE_URL = appConfig.getBackendApiUrl() + "/admin/users/not-logged/" + months;

		HttpHeaders headers = new HttpHeaders();
		headers.setBearerAuth(token);
		headers.setContentType(MediaType.APPLICATION_JSON);

		HttpEntity<User> getEntity = new HttpEntity<>(headers);


		restTemplate.exchange(
				DELETE_URL,
				HttpMethod.DELETE,
				getEntity,
				Void.class
		);
	}
}
