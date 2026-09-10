package com.warehouse.robot_service.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "robots")
@Data
@NoArgsConstructor
public class Robot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Robot code is required")
    @Column(nullable = false, unique = true)
    private String robotCode;

    @NotBlank(message = "Status is required")
    @Column(nullable = false)
    private String status;

    @Min(value = 0, message = "Battery cannot be negative")
    @Column(nullable = false)
    private Integer batteryLevel;

    @NotBlank(message = "Location is required")
    @Column(nullable = false)
    private String location;

    @Column(nullable = false)
    private LocalDateTime lastHeartbeat;
}