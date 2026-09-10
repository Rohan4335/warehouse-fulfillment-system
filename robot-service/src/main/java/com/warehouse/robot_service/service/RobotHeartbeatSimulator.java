package com.warehouse.robot_service.service;

import com.warehouse.robot_service.entity.Robot;
import com.warehouse.robot_service.repository.RobotRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class RobotHeartbeatSimulator {

    private final RobotRepository robotRepository;

    //@Scheduled(fixedRate = 20000)
    public void sendHeartbeats() {

        robotRepository.findAll().stream()
                .filter(robot -> !"OFFLINE".equals(robot.getStatus()))
                .forEach(robot -> {
                    robot.setLastHeartbeat(LocalDateTime.now());
                    robotRepository.save(robot);

                    log.debug("Heartbeat received from {}",
                            robot.getRobotCode());
                });
    }
}