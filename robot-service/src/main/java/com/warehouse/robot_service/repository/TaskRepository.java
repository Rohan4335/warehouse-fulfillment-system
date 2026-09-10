package com.warehouse.robot_service.repository;

import com.warehouse.robot_service.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TaskRepository extends JpaRepository<Task, Long> {

    Optional<Task> findByTaskNumber(String taskNumber);

    List<Task> findByRobotCodeAndStatusIn(
            String robotCode,
            List<String> statuses
    );

    List<Task> findByOrderNumber(String orderNumber);
    List<Task> findByStatus(String status);
}