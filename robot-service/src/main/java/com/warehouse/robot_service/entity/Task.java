package com.warehouse.robot_service.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "tasks")
@Data
@NoArgsConstructor
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Task number is required")
    @Column(nullable = false, unique = true)
    private String taskNumber;

    @NotBlank(message = "Order number is required")
    @Column(nullable = false)
    private String orderNumber;

    @NotBlank(message = "Product code is required")
    @Column(nullable = false)
    private String productCode;

    @Column(nullable = false)
    private Integer quantity;

    @Column(nullable = false)
    private String status;

    private String robotCode;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    private LocalDateTime completedAt;
}