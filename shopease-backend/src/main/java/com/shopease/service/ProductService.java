package com.shopease.service;

import com.shopease.entity.Product;
import com.shopease.exception.ResourceNotFoundException;
import com.shopease.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

/**
 * Service class for Product operations.
 * Provides business logic and interacts with ProductRepository.
 */
@Service
@RequiredArgsConstructor
@Transactional
public class ProductService {

    private final ProductRepository productRepository;

    /**
     * Retrieve all products.
     *
     * @return list of all products
     */
    @Transactional(readOnly = true)
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    /**
     * Retrieve a product by ID.
     *
     * @param id the product ID
     * @return the product if found
     * @throws ResourceNotFoundException if product is not found
     */
    @Transactional(readOnly = true)
    public Product getProductById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));
    }

    /**
     * Retrieve products by category name.
     *
     * @param categoryName the name of the category
     * @return list of products in the specified category
     */
    @Transactional(readOnly = true)
    public List<Product> getProductsByCategory(String categoryName) {
        return productRepository.findByCategory_Name(categoryName);
    }

    /**
     * Retrieve products by category ID.
     *
     * @param categoryId the category ID
     * @return list of products in the specified category
     */
    @Transactional(readOnly = true)
    public List<Product> getProductsByCategoryId(Long categoryId) {
        return productRepository.findByCategory_Id(categoryId);
    }

    /**
     * Retrieve products within a price range.
     *
     * @param minPrice the minimum price
     * @param maxPrice the maximum price
     * @return list of products within the price range
     */
    @Transactional(readOnly = true)
    public List<Product> getProductsByPriceRange(BigDecimal minPrice, BigDecimal maxPrice) {
        return productRepository.findByPriceRange(minPrice, maxPrice);
    }

    /**
     * Retrieve all discounted products.
     *
     * @return list of products with discount (original price > current price)
     */
    @Transactional(readOnly = true)
    public List<Product> getDiscountedProducts() {
        return productRepository.findDiscountedProducts();
    }

    /**
     * Search products by name.
     *
     * @param searchTerm the search term
     * @return list of products matching the search term
     */
    @Transactional(readOnly = true)
    public List<Product> searchProducts(String searchTerm) {
        return productRepository.searchByName(searchTerm);
    }

    /**
     * Retrieve all products with available stock.
     *
     * @return list of available products
     */
    @Transactional(readOnly = true)
    public List<Product> getAvailableProducts() {
        return productRepository.findAvailableProducts();
    }

    /**
     * Create a new product.
     *
     * @param product the product to create
     * @return the created product
     */
    public Product createProduct(Product product) {
        return productRepository.save(product);
    }

    /**
     * Update an existing product.
     *
     * @param id the product ID
     * @param product the updated product data
     * @return the updated product
     * @throws ResourceNotFoundException if product is not found
     */
    public Product updateProduct(Long id, Product product) {
        Product existing = getProductById(id);
        existing.setName(product.getName());
        existing.setDescription(product.getDescription());
        existing.setTagline(product.getTagline());
        existing.setPrice(product.getPrice());
        existing.setOriginalPrice(product.getOriginalPrice());
        existing.setImage(product.getImage());
        existing.setBadge(product.getBadge());
        existing.setSpecs(product.getSpecs());
        existing.setReviews(product.getReviews());
        existing.setStock(product.getStock());
        if (product.getCategory() != null) {
            existing.setCategory(product.getCategory());
        }
        return productRepository.save(existing);
    }

    /**
     * Delete a product by ID.
     *
     * @param id the product ID
     * @throws ResourceNotFoundException if product is not found
     */
    public void deleteProduct(Long id) {
        if (!productRepository.existsById(id)) {
            throw new ResourceNotFoundException("Product not found with id: " + id);
        }
        productRepository.deleteById(id);
    }
}
