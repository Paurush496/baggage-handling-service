package com.flight.baggagehandlerservice.model;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class CheckInBaggage {

	String bagUId;
	String bagAPC;
	String userId;
	float weight;
	LocalDateTime checkInTime;
}
