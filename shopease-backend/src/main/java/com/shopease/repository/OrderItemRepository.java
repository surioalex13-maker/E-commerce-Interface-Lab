package com.shopease.repository;

import com.shopease.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository interface for OrderItem entity.
 * Provides CRUD operations for managing order items.
 */
@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

    /**
     * Find all items for a specific order.
     *
     * @param orderId the order ID
     * @return list of items in the specified order
     */
    List<OrderItem> findByOrder_Id(Long orderId);

    /**
     * Find all order items for a specific product.
     *
     * @param productId the product ID
     * @return list of order items containing the specified product
     */
    List<OrderItem> findByProduct_Id(Long productId);
}
