package com.warehouse.robot_service.service;

import com.warehouse.robot_service.entity.Robot;
import com.warehouse.robot_service.exception.ResourceNotFoundException;
import com.warehouse.robot_service.repository.RobotRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RobotService {

    private final RobotRepository robotRepository;

    public Robot registerRobot(Robot robot) {
        robot.setLastHeartbeat(LocalDateTime.now());
        return robotRepository.save(robot);
    }

    public Robot saveRobot(Robot robot) {
        return robotRepository.save(robot);
    }

    public List<Robot> getAllRobots() {
        return robotRepository.findAll();
    }

    public Robot getRobotByCode(String robotCode) {
        return robotRepository.findByRobotCode(robotCode)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Robot not found: " + robotCode));
    }

    public Robot updateHeartbeat(String robotCode) {
        Robot robot = getRobotByCode(robotCode);

        robot.setLastHeartbeat(LocalDateTime.now());

        if ("OFFLINE".equals(robot.getStatus())) {
            robot.setStatus("AVAILABLE");
        }

        return robotRepository.save(robot);
    }

    public Robot assignRobot() {

        return robotRepository.findAll().stream()
                .filter(r -> "AVAILABLE".equals(r.getStatus()))
                .filter(r -> r.getBatteryLevel() >= 30)
                .max((r1, r2) -> Integer.compare(
                        r1.getBatteryLevel(),
                        r2.getBatteryLevel()
                ))
                .orElseThrow(() ->
                        new RuntimeException("No suitable robot available"));
    }

    public Robot releaseRobot(String robotCode) {
        Robot robot = getRobotByCode(robotCode);

        robot.setStatus("AVAILABLE");

        return robotRepository.save(robot);
    }

    public Robot assignReplacementRobot() {
        return assignRobot();
    }
}
