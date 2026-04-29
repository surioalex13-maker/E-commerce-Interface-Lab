package com.shopease.repository;

import com.shopease.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Repository interface for Order entity.
 * Provides CRUD operations and custom queries for order management.
 */
@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    /**
     * Find all orders by customer email.
     *
     * @param customerEmail the customer's email
     * @return list of orders for the specified customer
     */
    List<Order> findByCustomerEmail(String customerEmail);

    /**
     * Find orders by status.
     *
     * @param status the order status
     * @return list of orders with the specified status
     */
    List<Order> findByStatus(Order.OrderStatus status);

    /**
     * Find orders created within a date range.
     *
     * @param startDate the start date
     * @param endDate the end date
     * @return list of orders created between the dates
     */
    @Query("SELECT o FROM Order o WHERE o.createdAt BETWEEN :startDate AND :endDate ORDER BY o.createdAt DESC")
    List<Order> findOrdersByDateRange(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);

    /**
     * Find recent orders (last 30 days).
     *
     * @return list of recent orders
     */
    @Query("SELECT o FROM Order o WHERE o.createdAt >= CURRENT_TIMESTAMP - 30 ORDER BY o.createdAt DESC")
    List<Order> findRecentOrders();
}
