package com.warehouse.robot_service.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "order-service")
public interface OrderClient {

    @PutMapping("/api/orders/number/{orderNumber}/status")
    void markOrderFulfilled(
            @PathVariable String orderNumber,
            @RequestParam String status
    );
}