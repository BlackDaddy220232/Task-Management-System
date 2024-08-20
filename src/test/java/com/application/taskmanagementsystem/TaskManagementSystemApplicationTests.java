package com.application.taskmanagementsystem;

import io.github.cdimascio.dotenv.Dotenv;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

@SpringBootTest
class TaskManagementSystemApplicationTests {
	@DynamicPropertySource
	static void setProperties(DynamicPropertyRegistry registry) {
		Dotenv dotenv = Dotenv.load();
		registry.add("spring.datasource.username", () -> dotenv.get("POSTGRES_USER"));
		registry.add("spring.datasource.password", () -> dotenv.get("POSTGRES_PASSWORD"));
	}
	@Test
	void contextLoads() {
		/*
		 * This test method is intentionally left empty.
		 * The purpose of this test is to verify that the Spring application context
		 * can be successfully loaded and initialized. Since there are no specific
		 * assertions to be made, the method body is left empty.
		 * If the context fails to load, the test will fail, indicating an issue
		 * with the application configuration.
		 */
	}

}
