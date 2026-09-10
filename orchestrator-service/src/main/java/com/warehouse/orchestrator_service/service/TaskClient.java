package com.warehouse.orchestrator_service.service;

import com.warehouse.orchestrator_service.dto.TaskRequest;
import com.warehouse.orchestrator_service.dto.TaskResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
@RequiredArgsConstructor
public class TaskClient {

    private final RestClient robotRestClient;

    public TaskResponse createTask(TaskRequest request) {
        return robotRestClient.post()
                .uri("/api/tasks")
                .body(request)
                .retrieve()
                .body(TaskResponse.class);
    }

    public TaskResponse assignTask(Long taskId, String robotCode) {
        return robotRestClient.put()
                .uri("/api/tasks/{taskId}/assign/{robotCode}",
                        taskId, robotCode)
                .retrieve()
                .body(TaskResponse.class);
    }
}
