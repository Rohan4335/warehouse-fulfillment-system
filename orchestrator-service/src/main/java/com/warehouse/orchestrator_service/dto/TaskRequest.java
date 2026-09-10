package com.warehouse.orchestrator_service.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class TaskRequest {

    private String taskNumber;
    private String orderNumber;
    private String productCode;
    private Integer quantity;
}
