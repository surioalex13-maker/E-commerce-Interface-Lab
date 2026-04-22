package com.ws101.service;

import com.ws101.exception.InvalidProductException;
import com.ws101.exception.ProductNotFoundException;
import com.ws101.model.Product;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service class for product-related operations.
 * 
 * Provides business logic for filtering, searching, and managing products.
 * This class acts as an intermediary between the API controller and the
 * in-memory data storage layer. All product operations are performed on
 * a shared List<Product> that persists during application runtime.
 * 
 * @author Pair Programming Team
 * @see Product
 */
@Service
public class ProductService {

    // In-memory data storage for products
    private final List<Product> productList = new ArrayList<>();
    private Long nextId = 1L;

    /**
     * Constructor that initializes the service with sample products.
     * This demonstrates the in-memory storage approach and provides
     * test data for API testing.
     */
    public ProductService() {
        initializeSampleData();
    }

    /**
     * Initializes the product list with sample data.
     * At least 10 products representing various categories and price points.
     */
    private void initializeSampleData() {
        productList.add(new Product(1L, "Gold Bracelet", "Elegant 14k gold bracelet with intricate design", 299.99, "Bracelets", 15, "https://via.placeholder.com/200?text=Gold+Bracelet"));
        productList.add(new Product(2L, "Silver Necklace", "Classic sterling silver chain necklace", 149.99, "Necklaces", 25, "https://via.placeholder.com/200?text=Silver+Necklace"));
        productList.add(new Product(3L, "Pearl Earrings", "Freshwater pearl stud earrings", 199.99, "Earrings", 20, "https://via.placeholder.com/200?text=Pearl+Earrings"));
        productList.add(new Product(4L, "Diamond Ring", "1 carat diamond engagement ring", 4999.99, "Rings", 5, "https://via.placeholder.com/200?text=Diamond+Ring"));
        productList.add(new Product(5L, "Rose Gold Bracelet", "Rose gold link bracelet", 349.99, "Bracelets", 12, "https://via.placeholder.com/200?text=Rose+Gold+Bracelet"));
        productList.add(new Product(6L, "Sapphire Necklace", "Blue sapphire pendant on 18k gold chain", 799.99, "Necklaces", 8, "https://via.placeholder.com/200?text=Sapphire+Necklace"));
        productList.add(new Product(7L, "Moonstone Ring", "Moonstone cocktail ring", 249.99, "Rings", 18, "https://via.placeholder.com/200?text=Moonstone+Ring"));
        productList.add(new Product(8L, "Crystal Pendant", "Swarovski crystal pendant", 89.99, "Necklaces", 35, "https://via.placeholder.com/200?text=Crystal+Pendant"));
        productList.add(new Product(9L, "Gold Hoop Earrings", "14k gold hoop earrings", 179.99, "Earrings", 22, "https://via.placeholder.com/200?text=Gold+Hoop+Earrings"));
        productList.add(new Product(10L, "Jade Bracelet", "Traditional jade bracelet", 129.99, "Bracelets", 28, "https://via.placeholder.com/200?text=Jade+Bracelet"));
        productList.add(new Product(11L, "Emerald Ring", "Natural emerald with diamond accents", 1299.99, "Rings", 3, "https://via.placeholder.com/200?text=Emerald+Ring"));
        productList.add(new Product(12L, "Turquoise Necklace", "Native American turquoise necklace", 199.99, "Necklaces", 14, "https://via.placeholder.com/200?text=Turquoise+Necklace"));

        nextId = 13L;
    }

    /**
     * Retrieves all products from the in-memory storage.
     * 
     * @return a List of all Product objects in the system
     */
    public List<Product> getAllProducts() {
        return new ArrayList<>(productList);
    }

    /**
     * Finds a product by its unique identifier.
     * 
     * @param id the unique identifier of the product to retrieve
     * @return the Product object with the specified ID
     * @throws ProductNotFoundException if no product with the given ID exists
     */
    public Product getProductById(Long id) {
        return productList.stream()
                .filter(product -> product.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new ProductNotFoundException(
                        "Product with ID " + id + " not found"));
    }

    /**
     * Creates a new product and adds it to the in-memory storage.
     * 
     * Validates that required fields are present and meet constraints before creation.
     * Automatically assigns a unique ID to the new product.
     * 
     * @param product the Product object to create (ID is ignored if provided)
     * @return the created Product object with an assigned ID
     * @throws InvalidProductException if the product data fails validation
     */
    public Product createProduct(Product product) {
        validateProduct(product);

        product.setId(nextId++);
        productList.add(product);
        return product;
    }

    /**
     * Updates an existing product entirely (PUT operation).
     * 
     * Replaces the entire product with the new data provided.
     * All fields are validated before updating.
     * 
     * @param id the ID of the product to update
     * @param product the new Product data (ID field is ignored, uses path ID)
     * @return the updated Product object
     * @throws ProductNotFoundException if no product with the given ID exists
     * @throws InvalidProductException if the product data fails validation
     */
    public Product updateProduct(Long id, Product product) {
        validateProduct(product);

        Product existingProduct = getProductById(id);

        existingProduct.setName(product.getName());
        existingProduct.setDescription(product.getDescription());
        existingProduct.setPrice(product.getPrice());
        existingProduct.setCategory(product.getCategory());
        existingProduct.setStockQuantity(product.getStockQuantity());
        existingProduct.setImageUrl(product.getImageUrl());

        return existingProduct;
    }

    /**
     * Partially updates a product (PATCH operation).
     * 
     * Only updates fields that are provided (non-null) in the input,
     * leaving other fields unchanged.
     * 
     * @param id the ID of the product to partially update
     * @param product the partial Product data with fields to update
     * @return the updated Product object
     * @throws ProductNotFoundException if no product with the given ID exists
     * @throws InvalidProductException if any provided data fails validation
     */
    public Product partialUpdateProduct(Long id, Product product) {
        Product existingProduct = getProductById(id);

        if (product.getName() != null && !product.getName().isEmpty()) {
            if (product.getName().length() < 2) {
                throw new InvalidProductException("Product name must be at least 2 characters long");
            }
            existingProduct.setName(product.getName());
        }

        if (product.getDescription() != null && !product.getDescription().isEmpty()) {
            existingProduct.setDescription(product.getDescription());
        }

        if (product.getPrice() != null) {
            if (product.getPrice() <= 0) {
                throw new InvalidProductException("Price must be a positive number");
            }
            existingProduct.setPrice(product.getPrice());
        }

        if (product.getCategory() != null && !product.getCategory().isEmpty()) {
            existingProduct.setCategory(product.getCategory());
        }

        if (product.getStockQuantity() != null) {
            if (product.getStockQuantity() < 0) {
                throw new InvalidProductException("Stock quantity cannot be negative");
            }
            existingProduct.setStockQuantity(product.getStockQuantity());
        }

        if (product.getImageUrl() != null && !product.getImageUrl().isEmpty()) {
            existingProduct.setImageUrl(product.getImageUrl());
        }

        return existingProduct;
    }

    /**
     * Deletes a product from the in-memory storage.
     * 
     * @param id the ID of the product to delete
     * @throws ProductNotFoundException if no product with the given ID exists
     */
    public void deleteProduct(Long id) {
        Product product = getProductById(id);
        productList.remove(product);
    }

    /**
     * Filters products by category.
     * 
     * @param category the category name to filter by
     * @return a List of all products in the specified category
     * @throws InvalidProductException if category is null or empty
     */
    public List<Product> filterProductsByCategory(String category) {
        if (category == null || category.isEmpty()) {
            throw new InvalidProductException("Category filter value cannot be empty");
        }

        return productList.stream()
                .filter(product -> product.getCategory().equalsIgnoreCase(category))
                .collect(Collectors.toList());
    }

    /**
     * Filters products by price range.
     * 
     * Retrieves all products where the price falls within the specified range,
     * inclusive of both boundaries. Products are returned in the order they
     * appear in the underlying data source.
     * 
     * @param minPrice the minimum price threshold (inclusive).
     *                 Must be non-negative and less than or equal to maxPrice.
     * @param maxPrice the maximum price threshold (inclusive).
     *                 Must be non-negative and greater than or equal to minPrice.
     * @return a List containing all products with price within [minPrice, maxPrice].
     *         Returns an empty list if no products match the criteria.
     * @throws InvalidProductException if minPrice is negative, maxPrice is negative,
     *                                  or minPrice > maxPrice
     */
    public List<Product> filterProductsByPrice(Double minPrice, Double maxPrice) {
        if (minPrice == null || maxPrice == null) {
            throw new InvalidProductException("Price filter values cannot be null");
        }

        if (minPrice < 0 || maxPrice < 0) {
            throw new InvalidProductException("Price values cannot be negative");
        }

        if (minPrice > maxPrice) {
            throw new InvalidProductException("Minimum price cannot be greater than maximum price");
        }

        return productList.stream()
                .filter(product -> product.getPrice() >= minPrice && product.getPrice() <= maxPrice)
                .collect(Collectors.toList());
    }

    /**
     * Filters products by name (partial match).
     * 
     * Performs case-insensitive search for products containing the search term
     * in their name.
     * 
     * @param name the search term to filter products by name
     * @return a List of all products whose names contain the search term
     * @throws InvalidProductException if name is null or empty
     */
    public List<Product> filterProductsByName(String name) {
        if (name == null || name.isEmpty()) {
            throw new InvalidProductException("Name filter value cannot be empty");
        }

        return productList.stream()
                .filter(product -> product.getName().toLowerCase().contains(name.toLowerCase()))
                .collect(Collectors.toList());
    }

    /**
     * Generic filter method that delegates to specific filter methods.
     * 
     * @param filterType the type of filter to apply (category, price, name)
     * @param filterValue the value to filter by
     * @return a List of products matching the filter criteria
     * @throws InvalidProductException if filterType is invalid or filterValue is invalid
     */
    public List<Product> filterProducts(String filterType, String filterValue) {
        if (filterType == null || filterType.isEmpty()) {
            throw new InvalidProductException("Filter type cannot be empty");
        }

        if (filterValue == null || filterValue.isEmpty()) {
            throw new InvalidProductException("Filter value cannot be empty");
        }

        return switch (filterType.toLowerCase()) {
            case "category" -> filterProductsByCategory(filterValue);
            case "name" -> filterProductsByName(filterValue);
            case "price" -> {
                try {
                    String[] prices = filterValue.split("-");
                    if (prices.length != 2) {
                        throw new InvalidProductException(
                                "Price filter format should be: minPrice-maxPrice (e.g., 100-500)");
                    }
                    Double minPrice = Double.parseDouble(prices[0]);
                    Double maxPrice = Double.parseDouble(prices[1]);
                    yield filterProductsByPrice(minPrice, maxPrice);
                } catch (NumberFormatException e) {
                    throw new InvalidProductException("Price values must be valid numbers");
                }
            }
            default -> throw new InvalidProductException(
                    "Invalid filter type. Supported types: category, name, price");
        };
    }

    /**
     * Validates product data before creation or update.
     * 
     * Checks that all required fields are present and meet their constraints:
     * - Name: required, minimum 2 characters
     * - Description: required, cannot be empty
     * - Price: required, must be positive
     * - Category: required, cannot be empty
     * - Stock Quantity: required, must be non-negative
     * 
     * @param product the Product to validate
     * @throws InvalidProductException if any validation rule is violated
     */
    private void validateProduct(Product product) {
        if (product.getName() == null || product.getName().isEmpty()) {
            throw new InvalidProductException("Product name is required");
        }

        if (product.getName().length() < 2) {
            throw new InvalidProductException("Product name must be at least 2 characters long");
        }

        if (product.getDescription() == null || product.getDescription().isEmpty()) {
            throw new InvalidProductException("Product description is required");
        }

        if (product.getPrice() == null || product.getPrice() <= 0) {
            throw new InvalidProductException("Price must be a positive number");
        }

        if (product.getCategory() == null || product.getCategory().isEmpty()) {
            throw new InvalidProductException("Product category is required");
        }

        if (product.getStockQuantity() == null || product.getStockQuantity() < 0) {
            throw new InvalidProductException("Stock quantity must be a non-negative integer");
        }
    }

}
