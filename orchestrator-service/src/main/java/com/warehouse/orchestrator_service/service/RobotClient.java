package com.warehouse.orchestrator_service.service;

import com.warehouse.orchestrator_service.dto.RobotResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "robot-service")
public interface RobotClient {

    @PostMapping("/api/robots/assign")
    RobotResponse assignRobot();
}