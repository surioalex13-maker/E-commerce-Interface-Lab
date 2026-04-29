package com.shopease.entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;

/**
 * OrderItem Entity representing items within an order.
 * Demonstrates Many-to-One relationships with both Order and Product entities.
 * This join table design allows for modeling a shopping cart system.
 *
 * @see Order
 * @see Product
 */
@Entity
@Table(name = "order_items")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderItem {

    /**
     * Unique identifier for the order item.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Quantity of the product in this order item.
     */
    @Column(nullable = false)
    private Integer quantity;

    /**
     * Unit price at the time of order (for historical accuracy).
     */
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal unitPrice;

    /**
     * Total price (quantity × unitPrice).
     */
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal totalPrice;

    /**
     * Many-to-One relationship with Order.
     * Multiple order items belong to one order.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    /**
     * Many-to-One relationship with Product.
     * Multiple order items can reference the same product.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;
}
