package com.warehouse.robot_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
@EnableFeignClients
public class RobotServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(RobotServiceApplication.class, args);
	}

}
