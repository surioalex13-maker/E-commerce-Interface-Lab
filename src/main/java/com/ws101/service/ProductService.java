package com.ws101.service;

import com.ws101.exception.InvalidProductException;
import com.ws101.exception.ProductNotFoundException;
import com.ws101.model.Product;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductService {
    private final List<Product> productList = new ArrayList<>();
    private Long nextId = 1L;

    public ProductService() {
        initializeSampleData();
    }

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

    public List<Product> getAllProducts() {
        return new ArrayList<>(productList);
    }

    public Product getProductById(Long id) {
        return productList.stream()
                .filter(product -> product.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new ProductNotFoundException("Product with ID " + id + " not found"));
    }

    public Product createProduct(Product product) {
        validateProduct(product);
        product.setId(nextId++);
        productList.add(product);
        return product;
    }

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

    public void deleteProduct(Long id) {
        Product product = getProductById(id);
        productList.remove(product);
    }

    public List<Product> filterProductsByCategory(String category) {
        if (category == null || category.isEmpty()) {
            throw new InvalidProductException("Category filter value cannot be empty");
        }
        return productList.stream()
                .filter(product -> product.getCategory().equalsIgnoreCase(category))
                .collect(Collectors.toList());
    }

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

    public List<Product> filterProductsByName(String name) {
        if (name == null || name.isEmpty()) {
            throw new InvalidProductException("Name filter value cannot be empty");
        }
        return productList.stream()
                .filter(product -> product.getName().toLowerCase().contains(name.toLowerCase()))
                .collect(Collectors.toList());
    }

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
                        throw new InvalidProductException("Price filter format should be: minPrice-maxPrice (e.g., 100-500)");
                    }
                    Double minPrice = Double.parseDouble(prices[0]);
                    Double maxPrice = Double.parseDouble(prices[1]);
                    yield filterProductsByPrice(minPrice, maxPrice);
                } catch (NumberFormatException e) {
                    throw new InvalidProductException("Price values must be valid numbers");
                }
            }
            default -> throw new InvalidProductException("Invalid filter type. Supported types: category, name, price");
        };
    }

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
