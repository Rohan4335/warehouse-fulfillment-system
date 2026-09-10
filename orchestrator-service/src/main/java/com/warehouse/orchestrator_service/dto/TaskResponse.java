package com.warehouse.orchestrator_service.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class TaskResponse {

    private Long id;
    private String taskNumber;
    private String orderNumber;
    private String productCode;
    private Integer quantity;
    private String status;
    private String robotCode;
    private LocalDateTime createdAt;
    private LocalDateTime completedAt;
}
