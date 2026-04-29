package com.shopease.controller;

import com.shopease.entity.Order;
import com.shopease.entity.OrderItem;
import com.shopease.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST Controller for Order operations.
 * Provides endpoints for CRUD operations on orders.
 * Base path: /api/orders
 */
@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    /**
     * GET /api/orders - Retrieve all orders.
     *
     * @return ResponseEntity with list of all orders and HTTP 200 status
     */
    @GetMapping
    public ResponseEntity<List<Order>> getAllOrders() {
        List<Order> orders = orderService.getAllOrders();
        return ResponseEntity.ok(orders);
    }

    /**
     * GET /api/orders/{id} - Retrieve an order by ID.
     *
     * @param id the order ID
     * @return ResponseEntity with the order and HTTP 200 status
     */
    @GetMapping("/{id}")
    public ResponseEntity<Order> getOrderById(@PathVariable Long id) {
        Order order = orderService.getOrderById(id);
        return ResponseEntity.ok(order);
    }

    /**
     * GET /api/orders/customer/{email} - Retrieve all orders for a customer.
     *
     * @param email the customer email
     * @return ResponseEntity with list of customer orders and HTTP 200 status
     */
    @GetMapping("/customer/{email}")
    public ResponseEntity<List<Order>> getOrdersByCustomer(@PathVariable String email) {
        List<Order> orders = orderService.getOrdersByCustomer(email);
        return ResponseEntity.ok(orders);
    }

    /**
     * GET /api/orders/status/{status} - Retrieve orders by status.
     *
     * @param status the order status
     * @return ResponseEntity with list of orders with specified status and HTTP 200 status
     */
    @GetMapping("/status/{status}")
    public ResponseEntity<List<Order>> getOrdersByStatus(@PathVariable Order.OrderStatus status) {
        List<Order> orders = orderService.getOrdersByStatus(status);
        return ResponseEntity.ok(orders);
    }

    /**
     * GET /api/orders/recent - Retrieve recent orders (last 30 days).
     *
     * @return ResponseEntity with list of recent orders and HTTP 200 status
     */
    @GetMapping("/recent")
    public ResponseEntity<List<Order>> getRecentOrders() {
        List<Order> orders = orderService.getRecentOrders();
        return ResponseEntity.ok(orders);
    }

    /**
     * GET /api/orders/{orderId}/items - Retrieve items in an order.
     *
     * @param orderId the order ID
     * @return ResponseEntity with list of order items and HTTP 200 status
     */
    @GetMapping("/{orderId}/items")
    public ResponseEntity<List<OrderItem>> getOrderItems(@PathVariable Long orderId) {
        List<OrderItem> items = orderService.getOrderItems(orderId);
        return ResponseEntity.ok(items);
    }

    /**
     * POST /api/orders - Create a new order.
     *
     * @param order the order to create
     * @return ResponseEntity with the created order and HTTP 201 status
     */
    @PostMapping
    public ResponseEntity<Order> createOrder(@RequestBody Order order) {
        Order createdOrder = orderService.createOrder(order);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdOrder);
    }

    /**
     * PUT /api/orders/{id} - Update an existing order.
     *
     * @param id the order ID
     * @param order the updated order data
     * @return ResponseEntity with the updated order and HTTP 200 status
     */
    @PutMapping("/{id}")
    public ResponseEntity<Order> updateOrder(@PathVariable Long id, @RequestBody Order order) {
        Order updatedOrder = orderService.updateOrder(id, order);
        return ResponseEntity.ok(updatedOrder);
    }

    /**
     * DELETE /api/orders/{id} - Delete an order by ID.
     *
     * @param id the order ID
     * @return ResponseEntity with HTTP 204 No Content status
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrder(@PathVariable Long id) {
        orderService.deleteOrder(id);
        return ResponseEntity.noContent().build();
    }
}
