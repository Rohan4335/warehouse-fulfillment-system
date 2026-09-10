package com.warehouse.orchestrator_service.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "inventory-service")
public interface InventoryClient {

    @GetMapping("/api/inventory/{productCode}/check")
    Boolean checkStock(
            @PathVariable String productCode,
            @RequestParam int quantity
    );

    @PostMapping("/api/inventory/{productCode}/reserve")
    void reserveStock(
            @PathVariable String productCode,
            @RequestParam int quantity
    );

    @PostMapping("/api/inventory/{productCode}/release")
    void releaseStock(
            @PathVariable String productCode,
            @RequestParam int quantity
    );
}