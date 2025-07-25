package com.belejki.scheduler.repository;

import com.belejki.scheduler.config.AppConfig;
import com.belejki.scheduler.dto.Reminder;
import com.belejki.scheduler.service.AuthService;
import com.belejki.scheduler.service.PagedResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDate;
import java.util.List;

@Repository
public class ReminderRepositoryImpl implements ReminderRepository {

	private final AppConfig appConfig;
	private final AuthService authService;
	private final RestTemplate restTemplate;

	@Autowired
	public ReminderRepositoryImpl(AppConfig appConfig, AuthService authService, RestTemplate restTemplate) {
		this.appConfig = appConfig;
		this.authService = authService;
		this.restTemplate = restTemplate;
	}

	@Override
	public List<Reminder> findAllExpiringBefore(LocalDate cutoff) {

		String token = authService.getJwtToken();
		String READ_URL = appConfig.getBackendApiUrl() + "/schedule/reminders/flags-before"; //?page=0&size=1000"; // paging optional

		HttpHeaders headers = new HttpHeaders();
		headers.setBearerAuth(token);
		headers.setContentType(MediaType.APPLICATION_JSON);

		HttpEntity<Void> getEntity = new HttpEntity<>(headers);


		String urlWithParams = UriComponentsBuilder.fromHttpUrl(READ_URL)
				.queryParam("cutoff", cutoff.toString())
				.toUriString();

		ResponseEntity<PagedResponse<Reminder>> response = restTemplate.exchange(
				urlWithParams,
				HttpMethod.GET,
				getEntity,
				new ParameterizedTypeReference<PagedResponse<Reminder>>() {}
		);

		return response.getBody().getContent();
	}


	@Override
	public List<Reminder> findAllExpiresAfterMonthTrue() {
		String token = authService.getJwtToken();
		String READ_URL = appConfig.getBackendApiUrl() + "/schedule/reminders/expires-month";

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

		return response.getBody().getContent();
	}

	@Override
	public List<Reminder> findAllExpiresSoonTrue() {

		String token = authService.getJwtToken();
		String READ_URL = appConfig.getBackendApiUrl() + "/schedule/reminders/expires-soon"; //?page=0&size=1000"; // paging optional

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

		return response.getBody().getContent();
	}

	@Override
	public List<Reminder> findAllExpiresTodayTrue() {

		String token = authService.getJwtToken();
		String READ_URL = appConfig.getBackendApiUrl() + "/schedule/reminders/expires-today"; //?page=0&size=1000"; // paging optional

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

		return response.getBody().getContent();
	}


	@Override
	public void patchAll(List<Reminder> allExpiringBefore) {

		String token = authService.getJwtToken();

		HttpHeaders headers = new HttpHeaders();
		headers.setBearerAuth(token);
		headers.setContentType(MediaType.APPLICATION_JSON);

		String UPDATE_URL = appConfig.getBackendApiUrl() + "/schedule/reminders/patch";

		HttpEntity<List<Reminder>> putEntity = new HttpEntity<>(allExpiringBefore, headers);
		restTemplate.exchange(
				UPDATE_URL,
				HttpMethod.PUT,
				putEntity,
				Void.class
		);


		System.out.println("DONE UPDATING FLAGS");
	}

}
