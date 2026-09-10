package com.warehouse.order_service.service;

import com.warehouse.order_service.client.InventoryClient;
import com.warehouse.order_service.client.RobotClient;
import com.warehouse.order_service.entity.Order;
import com.warehouse.order_service.entity.OrderItem;
import com.warehouse.order_service.exception.OrderNotFoundException;
import com.warehouse.order_service.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final InventoryClient inventoryClient;
    private final RobotClient robotClient;

    public Order createOrder(Order order) {

        List<OrderItem> reservedItems = new java.util.ArrayList<>();

        try {

            // 1. Check and reserve inventory
            for (OrderItem item : order.getItems()) {

                boolean available = inventoryClient.checkStock(
                        item.getProductCode(),
                        item.getQuantity()
                );

                if (!available) {
                    throw new RuntimeException(
                            "Insufficient inventory for product: "
                                    + item.getProductCode()
                    );
                }

                inventoryClient.reserveStock(
                        item.getProductCode(),
                        item.getQuantity()
                );

                reservedItems.add(item);
            }

            // 2. Save order
            order.setStatus("INVENTORY_RESERVED");
            order.setCreatedAt(LocalDateTime.now());

            Order savedOrder = orderRepository.save(order);

            // 3. Create and assign robot tasks
            int taskIndex = 1;

            for (OrderItem item : savedOrder.getItems()) {

                String taskNumber =
                        savedOrder.getOrderNumber() + "-TASK-" + taskIndex++;

                RobotClient.TaskResponse task =
                        robotClient.createTask(
                                new RobotClient.TaskRequest(
                                        taskNumber,
                                        savedOrder.getOrderNumber(),
                                        item.getProductCode(),
                                        item.getQuantity()
                                )
                        );

                RobotClient.RobotResponse robot =
                        robotClient.assignRobot();

                robotClient.assignTask(
                        task.getId(),
                        robot.getRobotCode()
                );
            }

            // 4. Update order status
            savedOrder.setStatus("TASK_ASSIGNED");

            return orderRepository.save(savedOrder);

        } catch (Exception e) {

            // Release inventory if something failed
            for (OrderItem item : reservedItems) {
                try {
                    inventoryClient.releaseStock(
                            item.getProductCode(),
                            item.getQuantity()
                    );
                } catch (Exception ignored) {
                    // Keep original failure
                }
            }

            throw new RuntimeException(
                    "Order creation failed: " + e.getMessage()
            );
        }
    }

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    public Order getOrderById(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new OrderNotFoundException("Order not found with id: " + id));
    }

    public Order updateOrderStatus(Long id, String status) {
        Order order = getOrderById(id);

        order.setStatus(status);

        return orderRepository.save(order);
    }

    public Order updateOrderStatusByOrderNumber(
            String orderNumber,
            String status) {

        Order order = orderRepository.findByOrderNumber(orderNumber)
                .orElseThrow(() ->
                        new OrderNotFoundException(
                                "Order not found: " + orderNumber));

        order.setStatus(status);

        return orderRepository.save(order);
    }
}
