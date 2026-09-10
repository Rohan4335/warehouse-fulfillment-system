package com.warehouse.inventory_service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(InventoryNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, String> handleNotFound(InventoryNotFoundException ex) {
        return Map.of("error", ex.getMessage());
    }

    @ExceptionHandler(InsufficientStockException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleInsufficientStock(InsufficientStockException ex) {
        return Map.of("error", ex.getMessage());
    }
}
