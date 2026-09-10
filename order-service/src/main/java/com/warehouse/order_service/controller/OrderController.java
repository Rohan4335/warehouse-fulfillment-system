package com.warehouse.order_service.controller;

import com.warehouse.order_service.entity.Order;
import com.warehouse.order_service.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Order createOrder(@Valid @RequestBody Order order) {
        return orderService.createOrder(order);
    }

    @GetMapping
    public List<Order> getAllOrders() {
        return orderService.getAllOrders();
    }

    @GetMapping("/{id}")
    public Order getOrderById(@PathVariable Long id) {
        return orderService.getOrderById(id);
    }

    @PutMapping("/{id}/status")
    public Order updateOrderStatus(
            @PathVariable Long id,
            @RequestParam String status) {

        return orderService.updateOrderStatus(id, status);
    }

    @PutMapping("/number/{orderNumber}/status")
    public Order updateOrderStatusByOrderNumber(
            @PathVariable String orderNumber,
            @RequestParam String status) {

        return orderService.updateOrderStatusByOrderNumber(
                orderNumber,
                status
        );
    }
}
