package com.warehouse.robot_service.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "incidents")
@Data
@NoArgsConstructor
public class Incident {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String robotCode;

    private Long taskId;

    private String type;

    private String status;

    private String description;

    private LocalDateTime createdAt;

    private LocalDateTime resolvedAt;
}