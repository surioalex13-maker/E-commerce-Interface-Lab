package com.shopease.service;

import com.shopease.entity.Order;
import com.shopease.entity.OrderItem;
import com.shopease.entity.Product;
import com.shopease.exception.ResourceNotFoundException;
import com.shopease.repository.OrderRepository;
import com.shopease.repository.OrderItemRepository;
import com.shopease.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Service class for Order operations.
 * Provides business logic and interacts with OrderRepository and OrderItemRepository.
 */
@Service
@RequiredArgsConstructor
@Transactional
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final ProductRepository productRepository;

    /**
     * Retrieve all orders.
     *
     * @return list of all orders
     */
    @Transactional(readOnly = true)
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    /**
     * Retrieve an order by ID.
     *
     * @param id the order ID
     * @return the order if found
     * @throws ResourceNotFoundException if order is not found
     */
    @Transactional(readOnly = true)
    public Order getOrderById(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + id));
    }

    /**
     * Retrieve all orders for a customer.
     *
     * @param customerEmail the customer's email
     * @return list of orders for the customer
     */
    @Transactional(readOnly = true)
    public List<Order> getOrdersByCustomer(String customerEmail) {
        return orderRepository.findByCustomerEmail(customerEmail);
    }

    /**
     * Retrieve orders by status.
     *
     * @param status the order status
     * @return list of orders with the specified status
     */
    @Transactional(readOnly = true)
    public List<Order> getOrdersByStatus(Order.OrderStatus status) {
        return orderRepository.findByStatus(status);
    }

    /**
     * Retrieve recent orders (last 30 days).
     *
     * @return list of recent orders
     */
    @Transactional(readOnly = true)
    public List<Order> getRecentOrders() {
        return orderRepository.findRecentOrders();
    }

    /**
     * Create a new order.
     *
     * @param order the order to create
     * @return the created order
     */
    public Order createOrder(Order order) {
        validateOrder(order);

        if (order.getStatus() == null) {
            order.setStatus(Order.OrderStatus.PENDING);
        }

        if (order.getOrderItems() != null) {
            for (OrderItem item : order.getOrderItems()) {
                item.setOrder(order);

                Long productId = item.getProductId();
                if (productId == null && item.getProduct() != null) {
                    productId = item.getProduct().getId();
                }

                if (productId == null) {
                    throw new ResourceNotFoundException("Order item productId is required");
                }

                Long lookupProductId = productId;
                Product product = productRepository.findById(lookupProductId)
                        .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + lookupProductId));
                item.setProduct(product);
            }
        }

        return orderRepository.save(order);
    }

    private void validateOrder(Order order) {
        if (order.getCustomerEmail() == null || !order.getCustomerEmail().contains("@")) {
            throw new IllegalArgumentException("A valid customer email is required");
        }
        if (order.getDeliveryAddress() == null || order.getDeliveryAddress().isBlank()) {
            throw new IllegalArgumentException("Delivery address is required");
        }
        if (order.getTotalAmount() == null || order.getTotalAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Order total must be greater than zero");
        }
        if (order.getOrderItems() == null || order.getOrderItems().isEmpty()) {
            throw new IllegalArgumentException("Order must contain at least one item");
        }
    }

    /**
     * Update an existing order.
     *
     * @param id the order ID
     * @param order the updated order data
     * @return the updated order
     * @throws ResourceNotFoundException if order is not found
     */
    public Order updateOrder(Long id, Order order) {
        Order existing = getOrderById(id);
        existing.setCustomerEmail(order.getCustomerEmail());
        existing.setStatus(order.getStatus());
        existing.setTotalAmount(order.getTotalAmount());
        existing.setShippingFee(order.getShippingFee());
        existing.setDeliveryAddress(order.getDeliveryAddress());
        return orderRepository.save(existing);
    }

    /**
     * Delete an order by ID.
     *
     * @param id the order ID
     * @throws ResourceNotFoundException if order is not found
     */
    public void deleteOrder(Long id) {
        if (!orderRepository.existsById(id)) {
            throw new ResourceNotFoundException("Order not found with id: " + id);
        }
        orderRepository.deleteById(id);
    }

    /**
     * Retrieve order items for a specific order.
     *
     * @param orderId the order ID
     * @return list of order items
     */
    @Transactional(readOnly = true)
    public List<OrderItem> getOrderItems(Long orderId) {
        return orderItemRepository.findByOrder_Id(orderId);
    }
}
