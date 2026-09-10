package com.warehouse.inventory_service.service;

import com.warehouse.inventory_service.entity.Inventory;
import com.warehouse.inventory_service.repository.InventoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class InventoryService {

    private final InventoryRepository inventoryRepository;

    public Inventory addInventory(Inventory inventory) {
        return inventoryRepository.save(inventory);
    }

    public List<Inventory> getAllInventory() {
        return inventoryRepository.findAll();
    }

    public Inventory getByProductCode(String productCode) {
        return inventoryRepository.findByProductCode(productCode)
                .orElseThrow(() ->
                        new RuntimeException("Product not found: " + productCode));
    }

    public boolean checkStock(String productCode, int quantity) {
        Inventory inventory = getByProductCode(productCode);

        return inventory.getQuantity() - inventory.getReservedQuantity() >= quantity;
    }

    @Transactional
    public Inventory reserveStock(String productCode, int quantity) {
        Inventory inventory = getByProductCode(productCode);

        int available = inventory.getQuantity() - inventory.getReservedQuantity();

        if (available < quantity) {
            throw new RuntimeException("Insufficient stock for product: " + productCode);
        }

        inventory.setReservedQuantity(
                inventory.getReservedQuantity() + quantity
        );

        return inventoryRepository.save(inventory);
    }

    @Transactional
    public Inventory releaseStock(String productCode, int quantity) {
        Inventory inventory = getByProductCode(productCode);

        if (inventory.getReservedQuantity() < quantity) {
            throw new RuntimeException(
                    "Cannot release more stock than reserved for: " + productCode
            );
        }

        inventory.setReservedQuantity(
                inventory.getReservedQuantity() - quantity
        );

        return inventoryRepository.save(inventory);
    }

    @Transactional
    public Inventory completeStock(String productCode, int quantity) {
        Inventory inventory = getByProductCode(productCode);

        if (inventory.getReservedQuantity() < quantity) {
            throw new RuntimeException(
                    "Cannot complete more stock than reserved for: " + productCode
            );
        }

        inventory.setQuantity(inventory.getQuantity() - quantity);
        inventory.setReservedQuantity(
                inventory.getReservedQuantity() - quantity
        );

        return inventoryRepository.save(inventory);
    }
}
