package com.application.taskmanagementsystem;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class TaskManagementSystemApplication {

	public static void main(String[] args) {
		Dotenv dotenv = Dotenv.load();
		System.setProperty("spring.datasource.username", dotenv.get("POSTGRES_USER"));
		System.setProperty("spring.datasource.password", dotenv.get("POSTGRES_PASSWORD"));
		SpringApplication.run(TaskManagementSystemApplication.class, args);
	}

}
