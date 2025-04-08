package com.warehouse.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main application class for the Warehouse Management System backend.
 * This application uses Spring Boot and implements DDD principles.
 * CI/CD test - this comment was added to test the CI/CD pipeline.
 */
@SpringBootApplication
public class BackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(BackendApplication.class, args);
	}

}
