package com.warehouse.robot_service.controller;

import com.warehouse.robot_service.entity.Task;
import com.warehouse.robot_service.service.TaskService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tasks")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Task createTask(@Valid @RequestBody Task task) {
        return taskService.createTask(task);
    }

    @GetMapping
    public List<Task> getAllTasks() {
        return taskService.getAllTasks();
    }

    @GetMapping("/{taskNumber}")
    public Task getTaskByNumber(@PathVariable String taskNumber) {
        return taskService.getTaskByNumber(taskNumber);
    }

    @PutMapping("/{taskId}/assign/{robotCode}")
    public Task assignRobot(
            @PathVariable Long taskId,
            @PathVariable String robotCode) {

        return taskService.assignRobot(taskId, robotCode);
    }

    @PutMapping("/{taskId}/start")
    public Task startTask(@PathVariable Long taskId) {
        return taskService.startTask(taskId);
    }

    @PutMapping("/{taskId}/complete")
    public Task completeTask(@PathVariable Long taskId) {
        return taskService.completeTask(taskId);
    }

    @PutMapping("/{taskId}/reassign/{robotCode}")
    public Task reassignTask(
            @PathVariable Long taskId,
            @PathVariable String robotCode) {

        return taskService.reassignTask(taskId, robotCode);
    }

}