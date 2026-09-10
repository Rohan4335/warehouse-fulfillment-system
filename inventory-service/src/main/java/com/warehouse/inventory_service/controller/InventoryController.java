package com.warehouse.inventory_service.controller;

import com.warehouse.inventory_service.entity.Inventory;
import com.warehouse.inventory_service.service.InventoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/inventory")
@RequiredArgsConstructor
public class InventoryController {

    private final InventoryService inventoryService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Inventory addInventory(@Valid @RequestBody Inventory inventory) {
        return inventoryService.addInventory(inventory);
    }

    @GetMapping
    public List<Inventory> getAllInventory() {
        return inventoryService.getAllInventory();
    }

    @GetMapping("/{productCode}")
    public Inventory getByProductCode(@PathVariable String productCode) {
        return inventoryService.getByProductCode(productCode);
    }

    @PostMapping("/{productCode}/reserve")
    public Inventory reserveStock(
            @PathVariable String productCode,
            @RequestParam int quantity) {

        return inventoryService.reserveStock(productCode, quantity);
    }

    @PostMapping("/{productCode}/release")
    public Inventory releaseStock(
            @PathVariable String productCode,
            @RequestParam int quantity) {

        return inventoryService.releaseStock(productCode, quantity);
    }

    @PostMapping("/{productCode}/complete")
    public Inventory completeStock(
            @PathVariable String productCode,
            @RequestParam int quantity) {

        return inventoryService.completeStock(productCode, quantity);
    }

    @GetMapping("/{productCode}/check")
    public boolean checkStock(
            @PathVariable String productCode,
            @RequestParam int quantity) {

        return inventoryService.checkStock(productCode, quantity);
    }
}
