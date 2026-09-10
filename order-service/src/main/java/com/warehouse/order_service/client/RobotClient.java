package com.warehouse.order_service.client;

import lombok.Data;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "robot-service")
public interface RobotClient {

    @PostMapping("/api/tasks")
    TaskResponse createTask(@RequestBody TaskRequest request);

    @PostMapping("/api/robots/assign")
    RobotResponse assignRobot();

    @PutMapping("/api/tasks/{taskId}/assign/{robotCode}")
    TaskResponse assignTask(
            @PathVariable Long taskId,
            @PathVariable String robotCode
    );

    @Data
    class TaskRequest {
        private String taskNumber;
        private String orderNumber;
        private String productCode;
        private Integer quantity;

        public TaskRequest(
                String taskNumber,
                String orderNumber,
                String productCode,
                Integer quantity
        ) {
            this.taskNumber = taskNumber;
            this.orderNumber = orderNumber;
            this.productCode = productCode;
            this.quantity = quantity;
        }
    }

    @Data
    class TaskResponse {
        private Long id;
        private String taskNumber;
        private String orderNumber;
        private String productCode;
        private Integer quantity;
        private String status;
        private String robotCode;
    }

    @Data
    class RobotResponse {
        private String robotCode;
        private String status;
        private Integer batteryLevel;
    }
}