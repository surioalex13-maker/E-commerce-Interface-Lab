package com.shopease.repository;

import com.shopease.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

/**
 * Repository interface for Product entity.
 * Provides CRUD operations and custom query methods for product filtering.
 */
@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    /**
     * Find all products by category name using method naming convention.
     *
     * @param categoryName the name of the category
     * @return list of products in the specified category
     */
    List<Product> findByCategory_Name(String categoryName);

    /**
     * Find a product by its name.
     *
     * @param name the product name
     * @return Optional containing the product if found
     */
    Optional<Product> findByName(String name);

    /**
     * Custom JPQL query to find products within a price range.
     * Demonstrates @Query usage for complex filtering.
     *
     * @param minPrice the minimum price
     * @param maxPrice the maximum price
     * @return list of products within the specified price range
     */
    @Query("SELECT p FROM Product p WHERE p.price BETWEEN :minPrice AND :maxPrice ORDER BY p.price ASC")
    List<Product> findByPriceRange(@Param("minPrice") BigDecimal minPrice, @Param("maxPrice") BigDecimal maxPrice);

    /**
     * Find products with discount (where originalPrice > price).
     *
     * @return list of discounted products
     */
    @Query("SELECT p FROM Product p WHERE p.originalPrice > p.price ORDER BY p.price DESC")
    List<Product> findDiscountedProducts();

    /**
     * Find products by name containing search term (case-insensitive).
     *
     * @param searchTerm the search term
     * @return list of products matching the search term
     */
    @Query("SELECT p FROM Product p WHERE LOWER(p.name) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    List<Product> searchByName(@Param("searchTerm") String searchTerm);

    /**
     * Find products by category ID.
     *
     * @param categoryId the category ID
     * @return list of products in the specified category
     */
    List<Product> findByCategory_Id(Long categoryId);

    /**
     * Find products with available stock (stock > 0).
     *
     * @return list of available products
     */
    @Query("SELECT p FROM Product p WHERE p.stock > 0")
    List<Product> findAvailableProducts();
}
