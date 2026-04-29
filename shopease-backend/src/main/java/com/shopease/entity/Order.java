package com.shopease.entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Order Entity representing customer orders.
 * Demonstrates One-to-Many relationship with OrderItem entity.
 *
 * @see OrderItem
 */
@Entity
@Table(name = "orders")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Order {

    /**
     * Unique identifier for the order.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Customer email or identifier.
     */
    @Column(nullable = false, length = 255)
    private String customerEmail;

    /**
     * Order status (e.g., PENDING, SHIPPED, DELIVERED, CANCELLED).
     */
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private OrderStatus status = OrderStatus.PENDING;

    /**
     * Total order amount including items and shipping.
     */
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal totalAmount;

    /**
     * Shipping fee for the order.
     */
    @Column(precision = 10, scale = 2)
    private BigDecimal shippingFee;

    /**
     * Delivery address for the order.
     */
    @Column(columnDefinition = "TEXT")
    private String deliveryAddress;

    /**
     * Timestamp when the order was created.
     */
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    /**
     * Timestamp of last modification.
     */
    @Column(nullable = false)
    private LocalDateTime updatedAt;

    /**
     * One-to-Many relationship with OrderItem.
     * One order contains many order items (purchased products).
     * Using CascadeType.ALL to delete all items when order is deleted.
     */
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<OrderItem> orderItems;

    /**
     * Lifecycle callback to set createdAt before persistence.
     */
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    /**
     * Lifecycle callback to update updatedAt on modification.
     */
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    /**
     * Enum representing possible order statuses.
     */
    public enum OrderStatus {
        PENDING,
        CONFIRMED,
        SHIPPED,
        DELIVERED,
        CANCELLED
    }
}
