package com.flight.baggagehandlerservice.model;

import java.util.List;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ErrorResponse {

	private String code;
	private Map<String, List<String>> messages;
}
