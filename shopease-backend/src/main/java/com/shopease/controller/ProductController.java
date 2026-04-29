package com.shopease.controller;

import com.shopease.entity.Product;
import com.shopease.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

/**
 * REST Controller for Product operations.
 * Provides endpoints for CRUD operations and filtering products.
 * Base path: /api/products
 */
@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    /**
     * GET /api/products - Retrieve all products.
     *
     * @return ResponseEntity with list of all products and HTTP 200 status
     */
    @GetMapping
    public ResponseEntity<List<Product>> getAllProducts() {
        List<Product> products = productService.getAllProducts();
        return ResponseEntity.ok(products);
    }

    /**
     * GET /api/products/{id} - Retrieve a product by ID.
     *
     * @param id the product ID
     * @return ResponseEntity with the product and HTTP 200 status
     */
    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable Long id) {
        Product product = productService.getProductById(id);
        return ResponseEntity.ok(product);
    }

    /**
     * GET /api/products/category/{categoryName} - Retrieve products by category name.
     *
     * @param categoryName the category name
     * @return ResponseEntity with list of products in the category and HTTP 200 status
     */
    @GetMapping("/category/{categoryName}")
    public ResponseEntity<List<Product>> getProductsByCategory(@PathVariable String categoryName) {
        List<Product> products = productService.getProductsByCategory(categoryName);
        return ResponseEntity.ok(products);
    }

    /**
     * GET /api/products/categoryId/{categoryId} - Retrieve products by category ID.
     *
     * @param categoryId the category ID
     * @return ResponseEntity with list of products in the category and HTTP 200 status
     */
    @GetMapping("/categoryId/{categoryId}")
    public ResponseEntity<List<Product>> getProductsByCategoryId(@PathVariable Long categoryId) {
        List<Product> products = productService.getProductsByCategoryId(categoryId);
        return ResponseEntity.ok(products);
    }

    /**
     * GET /api/products/price-range?min={min}&max={max} - Retrieve products within price range.
     *
     * @param min the minimum price
     * @param max the maximum price
     * @return ResponseEntity with list of products in price range and HTTP 200 status
     */
    @GetMapping("/price-range")
    public ResponseEntity<List<Product>> getProductsByPriceRange(
            @RequestParam BigDecimal min,
            @RequestParam BigDecimal max) {
        List<Product> products = productService.getProductsByPriceRange(min, max);
        return ResponseEntity.ok(products);
    }

    /**
     * GET /api/products/discounted - Retrieve all discounted products.
     *
     * @return ResponseEntity with list of discounted products and HTTP 200 status
     */
    @GetMapping("/discounted")
    public ResponseEntity<List<Product>> getDiscountedProducts() {
        List<Product> products = productService.getDiscountedProducts();
        return ResponseEntity.ok(products);
    }

    /**
     * GET /api/products/available - Retrieve all available products (stock > 0).
     *
     * @return ResponseEntity with list of available products and HTTP 200 status
     */
    @GetMapping("/available")
    public ResponseEntity<List<Product>> getAvailableProducts() {
        List<Product> products = productService.getAvailableProducts();
        return ResponseEntity.ok(products);
    }

    /**
     * GET /api/products/search?term={searchTerm} - Search products by name.
     *
     * @param term the search term
     * @return ResponseEntity with list of matching products and HTTP 200 status
     */
    @GetMapping("/search")
    public ResponseEntity<List<Product>> searchProducts(@RequestParam String term) {
        List<Product> products = productService.searchProducts(term);
        return ResponseEntity.ok(products);
    }

    /**
     * POST /api/products - Create a new product.
     *
     * @param product the product to create
     * @return ResponseEntity with the created product and HTTP 201 status
     */
    @PostMapping
    public ResponseEntity<Product> createProduct(@RequestBody Product product) {
        Product createdProduct = productService.createProduct(product);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdProduct);
    }

    /**
     * PUT /api/products/{id} - Update an existing product.
     *
     * @param id the product ID
     * @param product the updated product data
     * @return ResponseEntity with the updated product and HTTP 200 status
     */
    @PutMapping("/{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable Long id, @RequestBody Product product) {
        Product updatedProduct = productService.updateProduct(id, product);
        return ResponseEntity.ok(updatedProduct);
    }

    /**
     * DELETE /api/products/{id} - Delete a product by ID.
     *
     * @param id the product ID
     * @return ResponseEntity with HTTP 204 No Content status
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }
}
