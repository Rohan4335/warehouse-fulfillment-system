package com.warehouse.orchestrator_service.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RobotResponse {

    private Long id;
    private String robotCode;
    private String status;
    private Integer batteryLevel;
    private String location;
}
