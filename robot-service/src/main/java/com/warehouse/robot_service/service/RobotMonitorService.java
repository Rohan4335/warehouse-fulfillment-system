package com.warehouse.robot_service.service;
import com.warehouse.robot_service.entity.Incident;
import com.warehouse.robot_service.entity.Robot;
import com.warehouse.robot_service.entity.Task;
import com.warehouse.robot_service.repository.RobotRepository;
import com.warehouse.robot_service.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class RobotMonitorService {

    private final RobotRepository robotRepository;
    private final RobotService robotService;
    private final TaskService taskService;
    private final IncidentService incidentService;
    private final TaskRepository taskRepository;

    @Value("${robot.heartbeat.timeout-seconds}")
    private long heartbeatTimeoutSeconds;

    @Scheduled(fixedRate = 30000)
    public void checkRobotHeartbeats() {

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime threshold =
                now.minusSeconds(heartbeatTimeoutSeconds);

        log.info("===== ROBOT HEARTBEAT CHECK =====");
        log.info("Current time: {}", now);
        log.info("Threshold: {}", threshold);

        robotRepository.findAll().forEach(robot -> {

            log.info(
                    "Robot={} | Status={} | LastHeartbeat={}",
                    robot.getRobotCode(),
                    robot.getStatus(),
                    robot.getLastHeartbeat()
            );

            if ("OFFLINE".equals(robot.getStatus())) {

                log.info(
                        "Robot {} is already OFFLINE. Checking active tasks for recovery.",
                        robot.getRobotCode()
                );

                recoverTasksForOfflineRobot(robot);

                return;
            }

            if (robot.getLastHeartbeat() == null) {
                log.warn(
                        "Robot {} has NULL heartbeat",
                        robot.getRobotCode()
                );
                return;
            }

            if (robot.getLastHeartbeat().isBefore(threshold)) {

                log.warn(
                        "Robot {} heartbeat timed out!",
                        robot.getRobotCode()
                );

                handleOfflineRobot(robot);

            } else {

                log.info(
                        "Robot {} heartbeat is still valid",
                        robot.getRobotCode()
                );
            }
        });
        recoverWaitingTasks();
    }

    private void handleOfflineRobot(Robot robot) {

        robot.setStatus("OFFLINE");
        robotRepository.save(robot);

        log.warn(
                "Robot {} marked OFFLINE - heartbeat timeout",
                robot.getRobotCode()
        );

        taskService
                .getActiveTasksByRobot(robot.getRobotCode())
                .forEach(task -> {

                    boolean incidentAlreadyExists =
                            incidentService.hasOpenIncident(task.getId());

                    if (incidentAlreadyExists) {
                        log.info(
                                "Open incident already exists for task {}",
                                task.getTaskNumber()
                        );
                        return;
                    }

                    Incident incident = incidentService.createIncident(
                            robot.getRobotCode(),
                            task.getId(),
                            "ROBOT_OFFLINE",
                            "Robot went offline while executing task"
                    );

                    log.warn(
                            "Incident {} created for task {}",
                            incident.getId(),
                            task.getTaskNumber()
                    );

                    try {

                        Robot replacement =
                                robotService.assignReplacementRobot();

                        taskService.reassignTask(
                                task.getId(),
                                replacement.getRobotCode()
                        );

                        incidentService.resolveIncident(
                                incident.getId()
                        );

                        log.info(
                                "Task {} reassigned from {} to {}",
                                task.getTaskNumber(),
                                robot.getRobotCode(),
                                replacement.getRobotCode()
                        );

                    } catch (Exception e) {

                        log.error(
                                "Could not reassign task {}: {}",
                                task.getTaskNumber(),
                                e.getMessage()
                        );
                    }
                });
    }

    private void recoverTasksForOfflineRobot(Robot robot) {

        taskService
                .getActiveTasksByRobot(robot.getRobotCode())
                .forEach(task -> {

                    boolean incidentAlreadyExists =
                            incidentService.hasOpenIncident(task.getId());

                    if (!incidentAlreadyExists) {

                        incidentService.createIncident(
                                robot.getRobotCode(),
                                task.getId(),
                                "ROBOT_OFFLINE",
                                "Robot is offline while task is still active"
                        );

                        log.warn(
                                "Created recovery incident for task {}",
                                task.getTaskNumber()
                        );
                    }

                    try {

                        Robot replacement =
                                robotService.assignReplacementRobot();

                        taskService.reassignTask(
                                task.getId(),
                                replacement.getRobotCode()
                        );

                        log.info(
                                "Task {} recovered: {} -> {}",
                                task.getTaskNumber(),
                                robot.getRobotCode(),
                                replacement.getRobotCode()
                        );

                        // Find the OPEN incident for this task
                        incidentService.resolveIncidentForTask(task.getId());

                    } catch (Exception e) {

                        taskService.markTaskWaitingForRobot(task.getId());

                        log.error(
                                "Recovery failed for task {}. No replacement robot available. Task moved to WAITING_FOR_ROBOT.",
                                task.getTaskNumber()
                        );
                    }
                });
    }

    private void recoverWaitingTasks() {

        List<Task> waitingTasks =
                taskRepository.findByStatus("WAITING_FOR_ROBOT");

        for (Task task : waitingTasks) {

            try {

                Robot replacement =
                        robotService.assignReplacementRobot();

                taskService.reassignTask(
                        task.getId(),
                        replacement.getRobotCode()
                );

                incidentService.resolveIncidentForTask(
                        task.getId()
                );

                log.info(
                        "Waiting task {} assigned to robot {}",
                        task.getTaskNumber(),
                        replacement.getRobotCode()
                );

            } catch (Exception e) {

                log.info(
                        "No robot available for waiting task {}",
                        task.getTaskNumber()
                );
            }
        }
    }
}