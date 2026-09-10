package com.warehouse.inventory_service.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "inventory")
@Data
@NoArgsConstructor
public class Inventory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Product code is required")
    @Column(nullable = false, unique = true)
    private String productCode;

    @Min(value = 0, message = "Quantity cannot be negative")
    @Column(nullable = false)
    private Integer quantity;

    @Min(value = 0, message = "Reserved quantity cannot be negative")
    @Column(nullable = false)
    private Integer reservedQuantity = 0;

    @NotBlank(message = "Location is required")
    @Column(nullable = false)
    private String location;
}