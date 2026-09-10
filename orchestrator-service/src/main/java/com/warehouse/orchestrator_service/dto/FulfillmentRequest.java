package com.warehouse.orchestrator_service.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class FulfillmentRequest {

    @NotBlank(message = "Product code is required")
    private String productCode;

    @Min(value = 1, message = "Quantity must be at least 1")
    private Integer quantity;

    @NotBlank(message = "Order number is required")
    private String orderNumber;
}
