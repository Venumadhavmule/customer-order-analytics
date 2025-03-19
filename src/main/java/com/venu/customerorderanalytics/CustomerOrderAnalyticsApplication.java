package com.venu.customerorderanalytics;import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.venu.customerorderanalytics.testing.DataGeneratorService;


@SpringBootApplication
public class CustomerOrderAnalyticsApplication implements CommandLineRunner {

	private DataGeneratorService dataGeneratorService;

	public CustomerOrderAnalyticsApplication(DataGeneratorService dataGeneratorService) {
		this.dataGeneratorService = dataGeneratorService;
	}

	public static void main(String[] args) {
		SpringApplication.run(CustomerOrderAnalyticsApplication.class, args);
	}

	@Override
	public void run(String... args) {
		dataGeneratorService.generateTestData();
	}

}
