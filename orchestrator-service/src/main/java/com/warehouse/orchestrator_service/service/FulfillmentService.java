package com.warehouse.orchestrator_service.service;

import com.warehouse.orchestrator_service.dto.FulfillmentRequest;
import com.warehouse.orchestrator_service.dto.RobotResponse;
import com.warehouse.orchestrator_service.dto.TaskRequest;
import com.warehouse.orchestrator_service.dto.TaskResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FulfillmentService {

    private final TaskClient taskClient;
    private final RobotClient robotClient;
    private final InventoryClient inventoryClient;

    public String startFulfillment(FulfillmentRequest request) {

        boolean available = inventoryClient.checkStock(
                request.getProductCode(),
                request.getQuantity()
        );

        if (!available) {
            throw new RuntimeException(
                    "Insufficient stock for product: " + request.getProductCode()
            );
        }

        inventoryClient.reserveStock(
                request.getProductCode(),
                request.getQuantity()
        );

        try {

            RobotResponse robot = robotClient.assignRobot();

            TaskRequest taskRequest = new TaskRequest();

            taskRequest.setTaskNumber("TASK-" + System.currentTimeMillis());
            taskRequest.setOrderNumber(request.getOrderNumber());
            taskRequest.setProductCode(request.getProductCode());
            taskRequest.setQuantity(request.getQuantity());

            TaskResponse task = taskClient.createTask(taskRequest);
            taskClient.assignTask(task.getId(), robot.getRobotCode());

            return "Order " + request.getOrderNumber()
                    + " assigned to robot " + robot.getRobotCode();

        } catch (Exception e) {

            System.err.println("FULFILLMENT ERROR: " + e.getMessage());
            e.printStackTrace();

            try {
                inventoryClient.releaseStock(
                        request.getProductCode(),
                        request.getQuantity()
                );
            } catch (Exception releaseException) {
                System.err.println("STOCK RELEASE ERROR: "
                        + releaseException.getMessage());
                releaseException.printStackTrace();
            }

            throw new RuntimeException(
                    "Fulfillment failed: " + e.getMessage(),
                    e
            );
        }
    }
}