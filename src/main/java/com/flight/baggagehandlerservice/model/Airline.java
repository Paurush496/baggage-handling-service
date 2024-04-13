package com.flight.baggagehandlerservice.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class Airline {

	@JsonProperty(value = "Name")
	private String name;
	
	@JsonAlias({ "iata", "Code" })
	private String codeIATA;
	
	@JsonAlias({ "icao", "ICAO" })
	private String codeICAO;
}
