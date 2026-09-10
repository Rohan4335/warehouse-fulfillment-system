package com.warehouse.orchestrator_service.service;

import com.warehouse.orchestrator_service.dto.TaskRequest;
import com.warehouse.orchestrator_service.dto.TaskResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "robot-service")
public interface TaskClient {

    @PostMapping("/api/tasks")
    TaskResponse createTask(@RequestBody TaskRequest request);

    @PutMapping("/api/tasks/{taskId}/assign/{robotCode}")
    TaskResponse assignTask(
            @PathVariable Long taskId,
            @PathVariable String robotCode
    );
}