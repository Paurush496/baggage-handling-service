package com.flight.baggagehandlerservice.configuration;

import java.util.concurrent.TimeUnit;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import feign.Logger;
import feign.Retryer;

@Configuration
public class FeignClientConfig {

	@Bean
	Retryer retryer() {
		return new Retryer.Default(TimeUnit.SECONDS.toMillis(1L), TimeUnit.SECONDS.toMillis(5L), 5);
	}

	@Bean
	Logger.Level feignLoggerLevel() {
		return Logger.Level.BASIC;
	}
}
