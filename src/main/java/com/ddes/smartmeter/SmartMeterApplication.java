package com.ddes.smartmeter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * The entry point for the SmartMeter application.
 * This class contains the main method that launches the Spring Boot application.
 */
@SpringBootApplication  // This annotation marks the class as a Spring Boot application and enables auto-configuration, component scanning, etc.
public class SmartMeterApplication {

	/**
	 * The main method that serves as the entry point for the application.
	 * It triggers the Spring Boot application to start up and initialize the necessary components.
	 */
	public static void main(String[] args) {
		// Start the Spring Boot application by running SmartMeterApplication class.
		SpringApplication.run(SmartMeterApplication.class, args);
	}
}