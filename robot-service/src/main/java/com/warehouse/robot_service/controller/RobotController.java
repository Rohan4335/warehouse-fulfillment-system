package com.warehouse.robot_service.controller;

import com.warehouse.robot_service.entity.Robot;
import com.warehouse.robot_service.service.RobotService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/robots")
@RequiredArgsConstructor
public class RobotController {

    private final RobotService robotService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Robot registerRobot(@Valid @RequestBody Robot robot) {
        return robotService.registerRobot(robot);
    }

    @GetMapping
    public List<Robot> getAllRobots() {
        return robotService.getAllRobots();
    }

    @GetMapping("/{robotCode}")
    public Robot getRobotByCode(@PathVariable String robotCode) {
        return robotService.getRobotByCode(robotCode);
    }

    @PostMapping("/{robotCode}/heartbeat")
    public Robot heartbeat(@PathVariable String robotCode) {
        return robotService.updateHeartbeat(robotCode);
    }

    @PostMapping("/assign")
    public Robot assignRobot() {
        return robotService.assignRobot();
    }

    @PutMapping("/{robotCode}/release")
    public Robot releaseRobot(@PathVariable String robotCode) {
        return robotService.releaseRobot(robotCode);
    }
}
