package com.warehouse.robot_service.service;

import com.warehouse.robot_service.client.InventoryClient;
import com.warehouse.robot_service.client.OrderClient;
import com.warehouse.robot_service.entity.Robot;
import com.warehouse.robot_service.entity.Task;
import com.warehouse.robot_service.exception.ResourceNotFoundException;
import com.warehouse.robot_service.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;
    private final RobotService robotService;
    private final InventoryClient inventoryClient;
    private final OrderClient orderClient;

    public Task createTask(Task task) {
        task.setStatus("CREATED");
        task.setCreatedAt(LocalDateTime.now());

        return taskRepository.save(task);
    }

    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }

    public Task getTaskByNumber(String taskNumber) {
        return taskRepository.findByTaskNumber(taskNumber)
                .orElseThrow(() ->
                        new RuntimeException("Task not found: " + taskNumber));
    }

    public Task assignRobot(Long taskId, String robotCode) {

        Task task = taskRepository.findById(taskId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Task not found: " + taskId));

        Robot robot = robotService.getRobotByCode(robotCode);

        if (!"AVAILABLE".equals(robot.getStatus())) {
            throw new RuntimeException(
                    "Robot is not available: " + robotCode
            );
        }

        if (robot.getBatteryLevel() < 30) {
            throw new RuntimeException(
                    "Robot battery too low: " + robotCode
            );
        }

        task.setRobotCode(robotCode);
        task.setStatus("ASSIGNED");

        robot.setStatus("BUSY");

        // We need to save the robot
        robotService.saveRobot(robot);

        return taskRepository.save(task);
    }

    public Task startTask(Long taskId) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() ->
                        new RuntimeException("Task not found: " + taskId));

        if (!"ASSIGNED".equals(task.getStatus())) {
            throw new RuntimeException(
                    "Only ASSIGNED tasks can be started"
            );
        }

        task.setStatus("IN_PROGRESS");

        return taskRepository.save(task);
    }

    public List<Task> getActiveTasksByRobot(String robotCode) {
        return taskRepository.findByRobotCodeAndStatusIn(
                robotCode,
                List.of("ASSIGNED", "IN_PROGRESS")
        );
    }

    public Task reassignTask(Long taskId, String robotCode) {

        Task task = taskRepository.findById(taskId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Task not found: " + taskId));

        if ("COMPLETED".equals(task.getStatus())) {
            throw new RuntimeException("Completed task cannot be reassigned");
        }

        Robot robot = robotService.getRobotByCode(robotCode);

        if (!"AVAILABLE".equals(robot.getStatus())) {
            throw new RuntimeException("Robot is not available: " + robotCode);
        }

        if (robot.getBatteryLevel() < 30) {
            throw new RuntimeException("Robot battery too low: " + robotCode);
        }

        task.setRobotCode(robotCode);
        task.setStatus("ASSIGNED");

        robot.setStatus("BUSY");
        robotService.saveRobot(robot);

        return taskRepository.save(task);
    }

    public Task completeTask(Long taskId) {

        Task task = taskRepository.findById(taskId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Task not found: " + taskId));

        if (!"IN_PROGRESS".equals(task.getStatus())) {
            throw new IllegalStateException(
                    "Only IN_PROGRESS tasks can be completed");
        }

        // 1. Complete inventory
        inventoryClient.completeStock(
                task.getProductCode(),
                task.getQuantity()
        );

        // 2. Complete task
        task.setStatus("COMPLETED");
        task.setCompletedAt(LocalDateTime.now());

        Task completedTask = taskRepository.save(task);

        // 3. Release robot
        if (task.getRobotCode() != null) {
            robotService.releaseRobot(task.getRobotCode());
        }

        // 4. Mark order fulfilled
        List<Task> orderTasks =
                taskRepository.findByOrderNumber(task.getOrderNumber());

        boolean allTasksCompleted =
                orderTasks.stream()
                        .allMatch(t -> "COMPLETED".equals(t.getStatus()));

        if (allTasksCompleted) {
            orderClient.markOrderFulfilled(
                    task.getOrderNumber(),
                    "FULFILLED"
            );
        }

        return completedTask;
    }

    public Task markTaskWaitingForRobot(Long taskId) {

        Task task = taskRepository.findById(taskId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Task not found: " + taskId
                        ));

        task.setStatus("WAITING_FOR_ROBOT");
        task.setRobotCode(null);

        return taskRepository.save(task);
    }
}
