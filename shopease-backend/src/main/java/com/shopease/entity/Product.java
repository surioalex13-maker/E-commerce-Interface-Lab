package com.shopease.entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.util.List;

/**
 * Product Entity representing products in the e-commerce platform.
 * Demonstrates Many-to-One relationship with Category and One-to-Many with OrderItem.
 *
 * @see Category
 * @see OrderItem
 */
@Entity
@Table(name = "products")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Product {

    /**
     * Unique identifier for the product.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Product name (e.g., "Gold Necklace", "Silver Bracelet").
     */
    @Column(nullable = false)
    private String name;

    /**
     * Detailed product description.
     */
    @Column(columnDefinition = "TEXT")
    private String description;

    /**
     * Short tagline for the product.
     */
    @Column(length = 255)
    private String tagline;

    /**
     * Current selling price of the product.
     */
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    /**
     * Original/list price (used for discount calculation).
     */
    @Column(precision = 10, scale = 2)
    private BigDecimal originalPrice;

    /**
     * URL to product image.
     */
    @Column(length = 500)
    private String image;

    /**
     * Badge/label for the product (e.g., "Best Seller", "Limited Offer").
     */
    @Column(length = 100)
    private String badge;

    /**
     * Specification details stored as JSON (e.g., material, dimensions, etc.).
     */
    @Column(columnDefinition = "JSON")
    private String specs;

    /**
     * Customer reviews in JSON format.
     */
    @Column(columnDefinition = "JSON")
    private String reviews;

    /**
     * Many-to-One relationship with Category.
     * Many products belong to one category.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    /**
     * One-to-Many relationship with OrderItem.
     * One product can appear in many order items.
     */
    @OneToMany(mappedBy = "product", cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
    private List<OrderItem> orderItems;

    /**
     * Stock quantity available for this product.
     */
    @Column(nullable = false, columnDefinition = "INT DEFAULT 100")
    private Integer stock = 100;
}
