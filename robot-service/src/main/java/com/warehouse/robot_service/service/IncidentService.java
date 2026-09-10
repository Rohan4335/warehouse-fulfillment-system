package com.warehouse.robot_service.service;

import com.warehouse.robot_service.entity.Incident;
import com.warehouse.robot_service.exception.ResourceNotFoundException;
import com.warehouse.robot_service.repository.IncidentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class IncidentService {

    private final IncidentRepository incidentRepository;

    public Incident createIncident(
            String robotCode,
            Long taskId,
            String type,
            String description
    ) {
        Incident incident = new Incident();

        incident.setRobotCode(robotCode);
        incident.setTaskId(taskId);
        incident.setType(type);
        incident.setStatus("OPEN");
        incident.setDescription(description);
        incident.setCreatedAt(LocalDateTime.now());

        return incidentRepository.save(incident);
    }

    public List<Incident> getAllIncidents() {
        return incidentRepository.findAll();
    }

    public Incident resolveIncident(Long incidentId) {

        Incident incident = incidentRepository.findById(incidentId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Incident not found: " + incidentId
                        ));

        incident.setStatus("RESOLVED");
        incident.setResolvedAt(LocalDateTime.now());

        return incidentRepository.save(incident);
    }

    public boolean hasOpenIncident(Long taskId) {
        return incidentRepository.existsByTaskIdAndStatus(
                taskId,
                "OPEN"
        );
    }

    public void resolveIncidentForTask(Long taskId) {

        incidentRepository
                .findFirstByTaskIdAndStatus(taskId, "OPEN")
                .ifPresent(incident -> {

                    incident.setStatus("RESOLVED");
                    incident.setResolvedAt(LocalDateTime.now());

                    incidentRepository.save(incident);
                });
    }
}
