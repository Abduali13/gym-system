package com.company.gym_system;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;


@SpringBootApplication
public class GymSystemApplication {

	public static void main(String[] args) {
		SpringApplication.run(GymSystemApplication.class, args);
	}

}
