package com.warehouse.robot_service.repository;

import com.warehouse.robot_service.entity.Incident;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface IncidentRepository extends JpaRepository<Incident, Long> {

    List<Incident> findByStatus(String status);

    List<Incident> findByRobotCode(String robotCode);

    boolean existsByTaskIdAndStatus(Long taskId, String status);

    Optional<Incident> findFirstByTaskIdAndStatus(
            Long taskId,
            String status
    );
}