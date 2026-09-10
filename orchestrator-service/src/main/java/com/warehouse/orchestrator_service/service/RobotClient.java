package com.warehouse.orchestrator_service.service;

import com.warehouse.orchestrator_service.dto.RobotResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
@RequiredArgsConstructor
public class RobotClient {

    private final RestClient robotRestClient;

    public RobotResponse assignRobot() {
        return robotRestClient.post()
                .uri("/api/robots/assign")
                .retrieve()
                .body(RobotResponse.class);
    }
}
