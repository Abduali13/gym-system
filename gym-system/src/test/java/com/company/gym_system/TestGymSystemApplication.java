package com.company.gym_system;

import org.springframework.boot.SpringApplication;

public class TestGymSystemApplication {

	public static void main(String[] args) {
		SpringApplication.from(GymSystemApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
