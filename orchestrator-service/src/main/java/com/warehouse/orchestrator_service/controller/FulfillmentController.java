package com.warehouse.orchestrator_service.controller;

import com.warehouse.orchestrator_service.dto.FulfillmentRequest;
import com.warehouse.orchestrator_service.service.FulfillmentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/fulfillment")
@RequiredArgsConstructor
public class FulfillmentController {

    private final FulfillmentService fulfillmentService;

    @PostMapping
    public String startFulfillment(
            @Valid @RequestBody FulfillmentRequest request) {

        return fulfillmentService.startFulfillment(request);
    }
}