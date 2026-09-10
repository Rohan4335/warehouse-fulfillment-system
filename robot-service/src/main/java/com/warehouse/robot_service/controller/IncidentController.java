package com.warehouse.robot_service.controller;

import com.warehouse.robot_service.entity.Incident;
import com.warehouse.robot_service.service.IncidentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/incidents")
@RequiredArgsConstructor
public class IncidentController {

    private final IncidentService incidentService;

    @GetMapping
    public List<Incident> getAllIncidents() {
        return incidentService.getAllIncidents();
    }

    @PutMapping("/{incidentId}/resolve")
    public Incident resolveIncident(
            @PathVariable Long incidentId) {

        return incidentService.resolveIncident(incidentId);
    }
}
