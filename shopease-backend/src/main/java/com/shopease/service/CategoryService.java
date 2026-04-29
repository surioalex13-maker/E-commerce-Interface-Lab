package com.shopease.service;

import com.shopease.entity.Category;
import com.shopease.exception.ResourceNotFoundException;
import com.shopease.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Service class for Category operations.
 * Provides business logic and interacts with CategoryRepository.
 */
@Service
@RequiredArgsConstructor
@Transactional
public class CategoryService {

    private final CategoryRepository categoryRepository;

    /**
     * Retrieve all categories.
     *
     * @return list of all categories
     */
    @Transactional(readOnly = true)
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    /**
     * Retrieve a category by ID.
     *
     * @param id the category ID
     * @return the category if found
     * @throws ResourceNotFoundException if category is not found
     */
    @Transactional(readOnly = true)
    public Category getCategoryById(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + id));
    }

    /**
     * Retrieve a category by name.
     *
     * @param name the category name
     * @return the category if found
     * @throws ResourceNotFoundException if category is not found
     */
    @Transactional(readOnly = true)
    public Category getCategoryByName(String name) {
        return categoryRepository.findByName(name)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with name: " + name));
    }

    /**
     * Create a new category.
     *
     * @param category the category to create
     * @return the created category
     * @throws IllegalArgumentException if category name already exists
     */
    public Category createCategory(Category category) {
        if (categoryRepository.existsByName(category.getName())) {
            throw new IllegalArgumentException("Category with name '" + category.getName() + "' already exists");
        }
        return categoryRepository.save(category);
    }

    /**
     * Update an existing category.
     *
     * @param id the category ID
     * @param category the updated category data
     * @return the updated category
     * @throws ResourceNotFoundException if category is not found
     */
    public Category updateCategory(Long id, Category category) {
        Category existing = getCategoryById(id);
        existing.setName(category.getName());
        existing.setDescription(category.getDescription());
        return categoryRepository.save(existing);
    }

    /**
     * Delete a category by ID.
     *
     * @param id the category ID
     * @throws ResourceNotFoundException if category is not found
     */
    public void deleteCategory(Long id) {
        if (!categoryRepository.existsById(id)) {
            throw new ResourceNotFoundException("Category not found with id: " + id);
        }
        categoryRepository.deleteById(id);
    }
}
