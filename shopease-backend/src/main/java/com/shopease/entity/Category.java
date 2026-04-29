package com.shopease.entity;

import jakarta.persistence.*;
import lombok.*;

/**
 * Category Entity representing product categories.
 * Demonstrates One-to-Many relationship with Product entity.
 *
 * @see Product
 */
@Entity
@Table(name = "categories")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Category {

    /**
     * Unique identifier for the category.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Name of the category (e.g., "necklace", "bracelet").
     */
    @Column(nullable = false, unique = true)
    private String name;

    /**
     * Description of the category.
     */
    @Column(length = 500)
    private String description;

    /**
     * One-to-Many relationship with Product.
     * One category can have many products.
     * Using CascadeType.ALL to propagate all operations to child products.
     * Using FetchType.LAZY for performance optimization.
     */
    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private java.util.List<Product> products;
}
