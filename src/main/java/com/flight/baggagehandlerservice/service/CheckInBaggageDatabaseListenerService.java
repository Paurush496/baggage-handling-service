package com.flight.baggagehandlerservice.service;

import java.util.List;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.flight.baggagehandlerservice.model.CheckedInBagsFlights;

@Service
public class CheckInBaggageDatabaseListenerService {

	private final String fetchCheckedInBagsFlights = "SELECT COUNT(*) as \"bagsCount\", airline, flight, airline_logo_url as \"airlinesLogo\", "
			+ "departure FROM checked_in_baggage GROUP BY airline, flight, departure, airline_logo_url HAVING departure < CURRENT_TIMESTAMP";

	private final JdbcTemplate jdbcTemplate;
	private final SimpMessagingTemplate messagingTemplate;

	public CheckInBaggageDatabaseListenerService(JdbcTemplate jdbcTemplate, SimpMessagingTemplate messagingTemplate) {
		this.jdbcTemplate = jdbcTemplate;
		this.messagingTemplate = messagingTemplate;
	}

	@Async
	@Scheduled(fixedDelay = 5000)
	public void relayRealTimeCheckedInBagsFlightsInfo() {
		try {
			List<CheckedInBagsFlights> payload = jdbcTemplate.query(fetchCheckedInBagsFlights,
					new BeanPropertyRowMapper<>(CheckedInBagsFlights.class));
			if (payload != null && !payload.isEmpty()) {
				messagingTemplate.convertAndSend("/topic/update", payload);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
