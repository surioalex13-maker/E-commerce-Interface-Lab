# ShopEase E-Commerce Backend

## Project Overview

ShopEase Backend is a Spring Boot REST API for a full-stack e-commerce platform. It provides comprehensive product management, order processing, and category management with persistent database storage using JPA/Hibernate and MySQL.

### Key Features

- **Database-Backed Storage**: Persistent data storage with MySQL and JPA/Hibernate
- **RESTful API**: Complete CRUD operations for products, categories, and orders
- **Advanced Filtering**: Filter products by category, price range, and search terms
- **Order Management**: Full order lifecycle from creation to tracking
- **Entity Relationships**: Demonstrates One-to-Many and Many-to-One JPA relationships
- **Global Error Handling**: Consistent error responses with @ControllerAdvice
- **CORS Support**: Allows frontend communication from different origins
- **Transaction Management**: Proper database transaction handling with @Transactional

## Project Structure

```
shopease-backend/
├── pom.xml                          # Maven configuration with dependencies
├── src/main/java/com/shopease/
│   ├── ShopEaseApplication.java     # Main Spring Boot application
│   ├── controller/
│   │   ├── CategoryController.java  # Category CRUD endpoints
│   │   ├── ProductController.java   # Product CRUD and filtering endpoints
│   │   └── OrderController.java     # Order CRUD endpoints
│   ├── service/
│   │   ├── CategoryService.java     # Category business logic
│   │   ├── ProductService.java      # Product business logic
│   │   └── OrderService.java        # Order business logic
│   ├── repository/
│   │   ├── CategoryRepository.java  # Category JPA repository
│   │   ├── ProductRepository.java   # Product JPA repository with custom queries
│   │   ├── OrderRepository.java     # Order JPA repository
│   │   └── OrderItemRepository.java # OrderItem JPA repository
│   ├── entity/
│   │   ├── Category.java            # Category JPA entity
│   │   ├── Product.java             # Product JPA entity with relationships
│   │   ├── Order.java               # Order JPA entity
│   │   └── OrderItem.java           # OrderItem JPA entity (junction table)
│   ├── exception/
│   │   ├── ResourceNotFoundException.java  # Custom exception
│   │   ├── ErrorResponse.java              # Standardized error response
│   │   └── GlobalExceptionHandler.java     # Global @ControllerAdvice
│   └── config/
│       └── WebConfig.java           # CORS configuration
└── src/main/resources/
    └── application.yaml             # Database and JPA configuration
```

## Database Schema

### Entity Relationships

```
Category (1) ──────────── (*) Product
  - id (PK)                - id (PK)
  - name                   - name
  - description            - description
                           - price
                           - originalPrice
                           - category_id (FK)


Order (1) ──────────── (*) OrderItem
  - id (PK)              - id (PK)
  - customerEmail        - quantity
  - status               - unitPrice
  - totalAmount          - totalPrice
  - deliveryAddress      - order_id (FK)
  - createdAt            - product_id (FK)
  - updatedAt
                         
Product (1) ──────────── (*) OrderItem
  - id (PK)              (relationship above)
  - (all fields)
```

### Table Descriptions

| Table | Description |
|-------|-------------|
| `categories` | Stores product categories (e.g., necklace, bracelet) |
| `products` | Stores product information with FK to categories |
| `orders` | Stores customer orders with status tracking |
| `order_items` | Junction table linking products to orders |

## API Endpoints

### Category Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/categories` | Get all categories |
| GET | `/api/categories/{id}` | Get category by ID |
| GET | `/api/categories/name/{name}` | Get category by name |
| POST | `/api/categories` | Create new category |
| PUT | `/api/categories/{id}` | Update category |
| DELETE | `/api/categories/{id}` | Delete category |

### Product Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/products` | Get all products |
| GET | `/api/products/{id}` | Get product by ID |
| GET | `/api/products/category/{categoryName}` | Get products by category |
| GET | `/api/products/categoryId/{categoryId}` | Get products by category ID |
| GET | `/api/products/price-range?min={min}&max={max}` | Get products in price range |
| GET | `/api/products/discounted` | Get discounted products |
| GET | `/api/products/available` | Get products with stock > 0 |
| GET | `/api/products/search?term={term}` | Search products by name |
| POST | `/api/products` | Create new product |
| PUT | `/api/products/{id}` | Update product |
| DELETE | `/api/products/{id}` | Delete product |

### Order Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/orders` | Get all orders |
| GET | `/api/orders/{id}` | Get order by ID |
| GET | `/api/orders/customer/{email}` | Get orders by customer email |
| GET | `/api/orders/status/{status}` | Get orders by status |
| GET | `/api/orders/recent` | Get recent orders (30 days) |
| GET | `/api/orders/{orderId}/items` | Get items in an order |
| POST | `/api/orders` | Create new order |
| PUT | `/api/orders/{id}` | Update order |
| DELETE | `/api/orders/{id}` | Delete order |

## Setup & Configuration

### Prerequisites

- Java 17 or higher
- Maven 3.8+
- MySQL 8.0+

### Installation

1. **Clone the repository**
```bash
cd shopease-backend
```

2. **Update Database Configuration**

Edit `src/main/resources/application.yaml`:
```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/shopease_db?useSSL=false&serverTimezone=UTC
    username: root
    password: your_password
```

3. **Create MySQL Database**
```sql
CREATE DATABASE shopease_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

4. **Build the Project**
```bash
mvn clean install
```

5. **Run the Application**
```bash
mvn spring-boot:run
```

The backend will start on `http://localhost:8080`

## Key Features & Implementation

### 1. JPA Entity Relationships

**One-to-Many (Category → Product)**
```java
@OneToMany(mappedBy = "category", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
private List<Product> products;
```

**Many-to-One (Product → Category)**
```java
@ManyToOne(fetch = FetchType.LAZY)
@JoinColumn(name = "category_id", nullable = false)
private Category category;
```

**One-to-Many (Order → OrderItem)**
```java
@OneToMany(mappedBy = "order", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
private List<OrderItem> orderItems;
```

### 2. Custom Repository Queries

**Method Naming Convention**
```java
List<Product> findByCategory_Name(String categoryName);
boolean existsByName(String name);
```

**JPQL Custom Queries**
```java
@Query("SELECT p FROM Product p WHERE p.price BETWEEN :minPrice AND :maxPrice")
List<Product> findByPriceRange(@Param("minPrice") BigDecimal minPrice, @Param("maxPrice") BigDecimal maxPrice);
```

### 3. Global Exception Handling

The `GlobalExceptionHandler` class handles:
- `ResourceNotFoundException` → HTTP 404
- `DataIntegrityViolationException` → HTTP 400
- `IllegalArgumentException` → HTTP 400
- Generic `Exception` → HTTP 500

### 4. CORS Configuration

Configured in `WebConfig.java` to allow requests from:
- `http://localhost:5500`
- `http://localhost:3000`
- `http://127.0.0.1:5500`

## Service Layer Benefits

- **Separation of Concerns**: Business logic separated from controllers
- **Reusability**: Services can be used by multiple controllers
- **Testability**: Services are easy to unit test with mocks
- **Transaction Management**: Services handle complex multi-step operations
- **Consistent Error Handling**: Services throw standard exceptions

## Database Migration & Initialization

On first run, Hibernate with `ddl-auto=update` will:
1. Create all tables if they don't exist
2. Add new columns if entities change
3. Never drop existing data

To populate initial data, use the POST endpoints or create a `DataInitializer` bean.

## Error Response Format

All error responses follow this format:

```json
{
  "status": 404,
  "message": "Product not found with id: 999",
  "details": "The requested resource was not found in the database",
  "timestamp": 1704067200000
}
```

## Development Tips

### Enable SQL Logging
```yaml
logging:
  level:
    org.hibernate.SQL: DEBUG
    org.hibernate.type.descriptor.sql.BasicBinder: TRACE
```

### Use Postman for API Testing
1. Create a new request
2. Set method (GET, POST, PUT, DELETE)
3. Set endpoint URL (e.g., `http://localhost:8080/api/products`)
4. For POST/PUT, add JSON body in "Body" tab
5. Send and verify response

### Transaction Isolation
- Services use `@Transactional` for atomic operations
- LAZY fetch prevents N+1 query problems
- CascadeType.ALL ensures referential integrity

## Testing Checklist

- [ ] Database connection works
- [ ] All tables created automatically
- [ ] GET /api/products returns products
- [ ] POST /api/products creates new product
- [ ] PUT /api/products/{id} updates product
- [ ] DELETE /api/products/{id} removes product
- [ ] Restart server without losing data
- [ ] Filter endpoints work correctly
- [ ] Order creation saves to database
- [ ] CORS headers present in responses

## Future Enhancements

- [ ] Add pagination to list endpoints
- [ ] Implement authentication/authorization
- [ ] Add product images/file uploads
- [ ] Add inventory management
- [ ] Implement order status notifications
- [ ] Add API rate limiting
- [ ] Add comprehensive API documentation (Swagger)

## Version History

- **1.0.0** (Current) - Initial release with CRUD operations, relationships, and filtering

## License

This project is part of Laboratory 8: Database Integration and Consuming RESTful Web Services with Fetch API.
