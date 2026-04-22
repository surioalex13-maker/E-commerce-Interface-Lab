package com.ws101.controller;

import com.ws101.model.Product;
import com.ws101.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * REST Controller for managing product-related API endpoints.
 * 
 * Handles all HTTP requests related to products, including CRUD operations
 * and product filtering. This controller acts as the bridge between the HTTP
 * layer and the business logic layer (ProductService).
 * 
 * Base path: /api/v1/products
 * 
 * @author Pair Programming Team
 * @see ProductService
 * @see Product
 */
@RestController
@RequestMapping("/api/v1/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    /**
     * Retrieves all products from the system.
     * 
     * @return ResponseEntity containing a list of all products with 200 OK status
     * 
     * Example:
     * GET /api/v1/products
     * Response: [
     *   {
     *     "id": 1,
     *     "name": "Gold Bracelet",
     *     "description": "Elegant 14k gold bracelet",
     *     "price": 299.99,
     *     "category": "Bracelets",
     *     "stockQuantity": 15,
     *     "imageUrl": "..."
     *   }
     * ]
     */
    @GetMapping
    public ResponseEntity<List<Product>> getAllProducts() {
        List<Product> products = productService.getAllProducts();
        return ResponseEntity.ok(products);
    }

    /**
     * Retrieves a single product by its unique identifier.
     * 
     * @param id the unique identifier of the product to retrieve
     * @return ResponseEntity containing the requested product with 200 OK status
     * @throws ProductNotFoundException if the product with the given ID is not found
     * 
     * Example:
     * GET /api/v1/products/1
     * Response: {
     *   "id": 1,
     *   "name": "Gold Bracelet",
     *   "price": 299.99,
     *   ...
     * }
     */
    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable Long id) {
        Product product = productService.getProductById(id);
        return ResponseEntity.ok(product);
    }

    /**
     * Filters products based on specified criteria.
     * 
     * Supports multiple filter types: category, name, and price range.
     * 
     * @param filterType the type of filter to apply (category, name, price)
     * @param filterValue the value to filter by:
     *                    - For category/name: the exact or partial name/category
     *                    - For price: format as "minPrice-maxPrice" (e.g., "100-500")
     * @return ResponseEntity containing list of filtered products with 200 OK status
     * @throws InvalidProductException if filter parameters are invalid
     * 
     * Examples:
     * GET /api/v1/products/filter?filterType=category&filterValue=Bracelets
     * GET /api/v1/products/filter?filterType=name&filterValue=Bracelet
     * GET /api/v1/products/filter?filterType=price&filterValue=100-500
     */
    @GetMapping("/filter")
    public ResponseEntity<List<Product>> filterProducts(
            @RequestParam String filterType,
            @RequestParam String filterValue) {
        List<Product> filteredProducts = productService.filterProducts(filterType, filterValue);
        return ResponseEntity.ok(filteredProducts);
    }

    /**
     * Creates a new product in the system.
     * 
     * Validates the product data and assigns a unique ID automatically.
     * The ID provided in the request body (if any) is ignored.
     * 
     * @param product the Product object to create (ID should not be provided)
     * @return ResponseEntity containing the created product with 201 Created status
     * @throws InvalidProductException if the product data fails validation
     * 
     * Example:
     * POST /api/v1/products
     * Request Body: {
     *   "name": "New Necklace",
     *   "description": "Beautiful necklace",
     *   "price": 199.99,
     *   "category": "Necklaces",
     *   "stockQuantity": 10,
     *   "imageUrl": "..."
     * }
     * Response: 201 Created with created product including assigned ID
     */
    @PostMapping
    public ResponseEntity<Product> createProduct(@RequestBody Product product) {
        Product createdProduct = productService.createProduct(product);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdProduct);
    }

    /**
     * Updates an entire product (PUT operation).
     * 
     * Replaces all fields of the product with the provided data.
     * All required fields must be provided in the request body.
     * 
     * @param id the unique identifier of the product to update
     * @param product the new Product data (all required fields must be present)
     * @return ResponseEntity containing the updated product with 200 OK status
     * @throws ProductNotFoundException if the product with the given ID is not found
     * @throws InvalidProductException if the product data fails validation
     * 
     * Example:
     * PUT /api/v1/products/1
     * Request Body: {
     *   "name": "Updated Bracelet",
     *   "description": "Updated description",
     *   "price": 349.99,
     *   "category": "Bracelets",
     *   "stockQuantity": 20,
     *   "imageUrl": "..."
     * }
     */
    @PutMapping("/{id}")
    public ResponseEntity<Product> updateProduct(
            @PathVariable Long id,
            @RequestBody Product product) {
        Product updatedProduct = productService.updateProduct(id, product);
        return ResponseEntity.ok(updatedProduct);
    }

    /**
     * Partially updates a product (PATCH operation).
     * 
     * Updates only the fields that are provided in the request body.
     * Fields that are null/empty in the request are not updated.
     * Other fields retain their current values.
     * 
     * @param id the unique identifier of the product to partially update
     * @param product the partial Product data with only fields to be updated
     * @return ResponseEntity containing the updated product with 200 OK status
     * @throws ProductNotFoundException if the product with the given ID is not found
     * @throws InvalidProductException if any provided data fails validation
     * 
     * Example:
     * PATCH /api/v1/products/1
     * Request Body: {
     *   "price": 299.99,
     *   "stockQuantity": 25
     * }
     * (Other fields are not changed)
     */
    @PatchMapping("/{id}")
    public ResponseEntity<Product> partialUpdateProduct(
            @PathVariable Long id,
            @RequestBody Product product) {
        Product updatedProduct = productService.partialUpdateProduct(id, product);
        return ResponseEntity.ok(updatedProduct);
    }

    /**
     * Deletes a product from the system.
     * 
     * Removes the product with the specified ID from the in-memory storage.
     * 
     * @param id the unique identifier of the product to delete
     * @return ResponseEntity with 204 No Content status (empty body)
     * @throws ProductNotFoundException if the product with the given ID is not found
     * 
     * Example:
     * DELETE /api/v1/products/1
     * Response: 204 No Content
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }

}
