package com.flight.baggagehandlerservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class BaggageHandlerServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(BaggageHandlerServiceApplication.class, args);
	}

}
