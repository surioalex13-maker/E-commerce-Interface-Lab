package com.shopease.repository;

import com.shopease.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository interface for Category entity.
 * Extends JpaRepository to provide CRUD operations and custom query methods.
 */
@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    /**
     * Find a category by its name using method naming convention.
     *
     * @param name the category name
     * @return Optional containing the category if found
     */
    Optional<Category> findByName(String name);

    /**
     * Check if a category exists by name.
     *
     * @param name the category name
     * @return true if category exists, false otherwise
     */
    boolean existsByName(String name);
}
