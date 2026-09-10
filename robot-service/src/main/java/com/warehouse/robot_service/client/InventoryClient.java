package com.warehouse.robot_service.client;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "inventory-service")
public interface InventoryClient {

    @PostMapping("/api/inventory/{productCode}/complete")
    void completeStock(
            @PathVariable String productCode,
            @RequestParam int quantity
    );
}
