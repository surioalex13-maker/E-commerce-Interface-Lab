package com.ws101.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.EqualsAndHashCode;

/**
 * Product entity representing an e-commerce product.
 * 
 * This class defines the structure of a product with essential e-commerce fields
 * including identification, pricing, inventory, and categorization.
 * 
 * @author Pair Programming Team
 * @see com.ws101.service.ProductService
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode
public class Product {

    /**
     * Unique identifier for the product.
     * Generated automatically and remains immutable after creation.
     */
    private Long id;

    /**
     * Name of the product.
     * Required field with minimum length validation.
     */
    private String name;

    /**
     * Detailed description of the product.
     * Provides additional information about features and specifications.
     */
    private String description;

    /**
     * Price of the product in the store's currency.
     * Must be a positive number greater than zero.
     */
    private Double price;

    /**
     * Category classification for the product.
     * Used for filtering and organization of products.
     */
    private String category;

    /**
     * Quantity of product available in stock.
     * Must be a non-negative integer.
     */
    private Integer stockQuantity;

    /**
     * URL pointing to the product's image resource.
     * Optional field that may be null if no image is available.
     */
    private String imageUrl;

}
