package com.travel.bus.pricing;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class PricingApplication {

	public static final String CONTEXT_PATH = "/pricing/bus";

	public static void main(String[] args) {
		SpringApplication.run(PricingApplication.class, args);
	}

}
