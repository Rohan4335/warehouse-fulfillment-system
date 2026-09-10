package com.warehouse.robot_service.repository;

import com.warehouse.robot_service.entity.Robot;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RobotRepository extends JpaRepository<Robot, Long> {

    Optional<Robot> findByRobotCode(String robotCode);
}