package com.shopease.controller;

import com.shopease.entity.Category;
import com.shopease.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST Controller for Category operations.
 * Provides endpoints for CRUD operations on categories.
 * Base path: /api/categories
 */
@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    /**
     * GET /api/categories - Retrieve all categories.
     *
     * @return ResponseEntity with list of all categories and HTTP 200 status
     */
    @GetMapping
    public ResponseEntity<List<Category>> getAllCategories() {
        List<Category> categories = categoryService.getAllCategories();
        return ResponseEntity.ok(categories);
    }

    /**
     * GET /api/categories/{id} - Retrieve a category by ID.
     *
     * @param id the category ID
     * @return ResponseEntity with the category and HTTP 200 status
     */
    @GetMapping("/{id}")
    public ResponseEntity<Category> getCategoryById(@PathVariable Long id) {
        Category category = categoryService.getCategoryById(id);
        return ResponseEntity.ok(category);
    }

    /**
     * GET /api/categories/name/{name} - Retrieve a category by name.
     *
     * @param name the category name
     * @return ResponseEntity with the category and HTTP 200 status
     */
    @GetMapping("/name/{name}")
    public ResponseEntity<Category> getCategoryByName(@PathVariable String name) {
        Category category = categoryService.getCategoryByName(name);
        return ResponseEntity.ok(category);
    }

    /**
     * POST /api/categories - Create a new category.
     *
     * @param category the category to create
     * @return ResponseEntity with the created category and HTTP 201 status
     */
    @PostMapping
    public ResponseEntity<Category> createCategory(@RequestBody Category category) {
        Category createdCategory = categoryService.createCategory(category);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdCategory);
    }

    /**
     * PUT /api/categories/{id} - Update an existing category.
     *
     * @param id the category ID
     * @param category the updated category data
     * @return ResponseEntity with the updated category and HTTP 200 status
     */
    @PutMapping("/{id}")
    public ResponseEntity<Category> updateCategory(@PathVariable Long id, @RequestBody Category category) {
        Category updatedCategory = categoryService.updateCategory(id, category);
        return ResponseEntity.ok(updatedCategory);
    }

    /**
     * DELETE /api/categories/{id} - Delete a category by ID.
     *
     * @param id the category ID
     * @return ResponseEntity with HTTP 204 No Content status
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long id) {
        categoryService.deleteCategory(id);
        return ResponseEntity.noContent().build();
    }
}
