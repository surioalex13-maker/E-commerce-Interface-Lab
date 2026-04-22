# E-commerce API - Spring Boot Backend

A fully functional RESTful API backend for an e-commerce product management system, built with Spring Boot 4.0.5. This API demonstrates HTTP fundamentals, REST principles, and proper application architecture with in-memory data storage.

## Project Overview

This is a complete implementation of Laboratory 7 - HTTP Fundamentals and Spring Boot. The API provides full CRUD operations for managing products with proper HTTP status codes, error handling, and comprehensive filtering capabilities.

### Key Features

- ✅ Full CRUD operations for products (Create, Read, Update, Delete)
- ✅ Multiple filtering options (by category, name, and price range)
- ✅ Proper HTTP status codes (200, 201, 204, 400, 404, 500)
- ✅ Comprehensive error handling with consistent response format
- ✅ Input validation with meaningful error messages
- ✅ In-memory data storage with sample products
- ✅ RESTful API design with semantic endpoints
- ✅ Complete Javadoc documentation
- ✅ Production-ready code structure

## Technology Stack

- **Framework**: Spring Boot 4.0.5
- **Build Tool**: Maven
- **Language**: Java 25+
- **Libraries**: Lombok (for reducing boilerplate)
- **Data Storage**: In-memory List<Product>

## Project Structure

```
EcommerceApi/
├── pom.xml                                 # Maven configuration
├── src/
│   ├── main/
│   │   ├── java/com/ws101/
│   │   │   ├── EcommerceApiApplication.java       # Main Spring Boot application
│   │   │   ├── controller/
│   │   │   │   └── ProductController.java         # REST endpoints
│   │   │   ├── service/
│   │   │   │   └── ProductService.java            # Business logic
│   │   │   ├── model/
│   │   │   │   └── Product.java                   # Data entity
│   │   │   └── exception/
│   │   │       ├── ProductNotFoundException.java
│   │   │       ├── InvalidProductException.java
│   │   │       ├── ErrorResponse.java
│   │   │       └── GlobalExceptionHandler.java
│   │   └── resources/
│   │       └── application.properties              # Spring config
│   └── test/
│       └── java/
└── README.md                               # This file
```

## Setup Instructions

### Prerequisites

- Java 25 or higher
- Maven 3.6+
- Git
- IDE: IntelliJ IDEA, VSCode, or Eclipse

### Installation Steps

1. **Clone the repository**
   ```bash
   git clone <repository-url>
   cd EcommerceApi
   ```

2. **Build the project**
   ```bash
   mvn clean install
   ```

3. **Run the application**
   ```bash
   mvn spring-boot:run
   ```
   
   Or compile and run directly:
   ```bash
   mvn clean compile exec:java -Dexec.mainClass="com.ws101.EcommerceApiApplication"
   ```

4. **Verify the application**
   - The API will be available at: `http://localhost:8080`
   - Health check: `http://localhost:8080/api/v1/products`

## API Endpoint Reference

### Base URL
```
http://localhost:8080/api/v1
```

### 1. Get All Products

**Endpoint**: `GET /api/v1/products`

**Description**: Retrieves a list of all products in the system.

**Status Code**: `200 OK`

**Example Request**:
```bash
curl -X GET http://localhost:8080/api/v1/products
```

**Example Response**:
```json
[
  {
    "id": 1,
    "name": "Gold Bracelet",
    "description": "Elegant 14k gold bracelet with intricate design",
    "price": 299.99,
    "category": "Bracelets",
    "stockQuantity": 15,
    "imageUrl": "https://via.placeholder.com/200?text=Gold+Bracelet"
  },
  {
    "id": 2,
    "name": "Silver Necklace",
    "description": "Classic sterling silver chain necklace",
    "price": 149.99,
    "category": "Necklaces",
    "stockQuantity": 25,
    "imageUrl": "https://via.placeholder.com/200?text=Silver+Necklace"
  }
]
```

---

### 2. Get Product by ID

**Endpoint**: `GET /api/v1/products/{id}`

**Description**: Retrieves a single product by its unique identifier.

**Parameters**:
- `id` (path parameter, required): The unique identifier of the product

**Status Codes**:
- `200 OK` - Product found
- `404 Not Found` - Product not found

**Example Request**:
```bash
curl -X GET http://localhost:8080/api/v1/products/1
```

**Example Response** (200 OK):
```json
{
  "id": 1,
  "name": "Gold Bracelet",
  "description": "Elegant 14k gold bracelet with intricate design",
  "price": 299.99,
  "category": "Bracelets",
  "stockQuantity": 15,
  "imageUrl": "https://via.placeholder.com/200?text=Gold+Bracelet"
}
```

**Example Error Response** (404 Not Found):
```json
{
  "timestamp": "2024-04-22T10:30:00",
  "status": 404,
  "errorCode": "PRODUCT_NOT_FOUND",
  "message": "Product with ID 999 not found",
  "path": "/api/v1/products/999"
}
```

---

### 3. Filter Products

**Endpoint**: `GET /api/v1/products/filter`

**Description**: Filters products based on specified criteria.

**Query Parameters**:
- `filterType` (required): Type of filter - `category`, `name`, or `price`
- `filterValue` (required): The value to filter by
  - For `category`: exact category name (e.g., "Bracelets")
  - For `name`: partial product name (e.g., "Bracelet")
  - For `price`: price range as "minPrice-maxPrice" (e.g., "100-500")

**Status Codes**:
- `200 OK` - Filter successful (may return empty list)
- `400 Bad Request` - Invalid filter parameters

**Example Requests**:

#### Filter by Category
```bash
curl -X GET "http://localhost:8080/api/v1/products/filter?filterType=category&filterValue=Bracelets"
```

#### Filter by Name
```bash
curl -X GET "http://localhost:8080/api/v1/products/filter?filterType=name&filterValue=Bracelet"
```

#### Filter by Price Range
```bash
curl -X GET "http://localhost:8080/api/v1/products/filter?filterType=price&filterValue=100-500"
```

**Example Response**:
```json
[
  {
    "id": 1,
    "name": "Gold Bracelet",
    "description": "Elegant 14k gold bracelet with intricate design",
    "price": 299.99,
    "category": "Bracelets",
    "stockQuantity": 15,
    "imageUrl": "https://via.placeholder.com/200?text=Gold+Bracelet"
  }
]
```

---

### 4. Create Product

**Endpoint**: `POST /api/v1/products`

**Description**: Creates a new product in the system.

**Request Body**: JSON object with the following fields (ID is auto-generated):
```json
{
  "name": "string (required, min length 2)",
  "description": "string (required)",
  "price": "number (required, must be > 0)",
  "category": "string (required)",
  "stockQuantity": "number (required, must be >= 0)",
  "imageUrl": "string (optional)"
}
```

**Status Codes**:
- `201 Created` - Product successfully created
- `400 Bad Request` - Invalid product data

**Example Request**:
```bash
curl -X POST http://localhost:8080/api/v1/products \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Platinum Ring",
    "description": "Elegant platinum ring with diamond stone",
    "price": 5999.99,
    "category": "Rings",
    "stockQuantity": 5,
    "imageUrl": "https://via.placeholder.com/200?text=Platinum+Ring"
  }'
```

**Example Response** (201 Created):
```json
{
  "id": 13,
  "name": "Platinum Ring",
  "description": "Elegant platinum ring with diamond stone",
  "price": 5999.99,
  "category": "Rings",
  "stockQuantity": 5,
  "imageUrl": "https://via.placeholder.com/200?text=Platinum+Ring"
}
```

**Example Error Response** (400 Bad Request):
```json
{
  "timestamp": "2024-04-22T10:35:00",
  "status": 400,
  "errorCode": "INVALID_PRODUCT_DATA",
  "message": "Price must be a positive number",
  "path": "/api/v1/products"
}
```

---

### 5. Update Product (Full)

**Endpoint**: `PUT /api/v1/products/{id}`

**Description**: Replaces the entire product with new data.

**Parameters**:
- `id` (path parameter, required): The unique identifier of the product

**Request Body**: JSON object with all fields (same as POST):
```json
{
  "name": "string (required, min length 2)",
  "description": "string (required)",
  "price": "number (required, must be > 0)",
  "category": "string (required)",
  "stockQuantity": "number (required, must be >= 0)",
  "imageUrl": "string (optional)"
}
```

**Status Codes**:
- `200 OK` - Product successfully updated
- `404 Not Found` - Product not found
- `400 Bad Request` - Invalid product data

**Example Request**:
```bash
curl -X PUT http://localhost:8080/api/v1/products/1 \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Updated Gold Bracelet",
    "description": "Updated elegant 14k gold bracelet",
    "price": 349.99,
    "category": "Bracelets",
    "stockQuantity": 20,
    "imageUrl": "https://via.placeholder.com/200?text=Gold+Bracelet"
  }'
```

**Example Response** (200 OK):
```json
{
  "id": 1,
  "name": "Updated Gold Bracelet",
  "description": "Updated elegant 14k gold bracelet",
  "price": 349.99,
  "category": "Bracelets",
  "stockQuantity": 20,
  "imageUrl": "https://via.placeholder.com/200?text=Gold+Bracelet"
}
```

---

### 6. Partially Update Product

**Endpoint**: `PATCH /api/v1/products/{id}`

**Description**: Updates only the specified fields of a product. Other fields remain unchanged.

**Parameters**:
- `id` (path parameter, required): The unique identifier of the product

**Request Body**: JSON object with only the fields to update (all optional):
```json
{
  "name": "string (optional, min length 2)",
  "description": "string (optional)",
  "price": "number (optional, must be > 0)",
  "category": "string (optional)",
  "stockQuantity": "number (optional, must be >= 0)",
  "imageUrl": "string (optional)"
}
```

**Status Codes**:
- `200 OK` - Product successfully updated
- `404 Not Found` - Product not found
- `400 Bad Request` - Invalid data provided

**Example Request** (only updating price and stock):
```bash
curl -X PATCH http://localhost:8080/api/v1/products/1 \
  -H "Content-Type: application/json" \
  -d '{
    "price": 299.99,
    "stockQuantity": 25
  }'
```

**Example Response** (200 OK):
```json
{
  "id": 1,
  "name": "Gold Bracelet",
  "description": "Elegant 14k gold bracelet with intricate design",
  "price": 299.99,
  "category": "Bracelets",
  "stockQuantity": 25,
  "imageUrl": "https://via.placeholder.com/200?text=Gold+Bracelet"
}
```

---

### 7. Delete Product

**Endpoint**: `DELETE /api/v1/products/{id}`

**Description**: Deletes a product from the system.

**Parameters**:
- `id` (path parameter, required): The unique identifier of the product

**Status Codes**:
- `204 No Content` - Product successfully deleted
- `404 Not Found` - Product not found

**Example Request**:
```bash
curl -X DELETE http://localhost:8080/api/v1/products/1
```

**Example Response** (204 No Content):
```
(empty response body)
```

---

## HTTP Status Codes Reference

| Scenario | Status Code | Description |
|----------|------------|-------------|
| Successful GET | 200 OK | Resource retrieved successfully |
| Successful POST (creation) | 201 Created | New resource created successfully |
| Successful DELETE | 204 No Content | Resource deleted successfully; no response body |
| Successful PUT/PATCH | 200 OK | Resource updated successfully |
| Product not found | 404 Not Found | Requested product does not exist |
| Invalid request data | 400 Bad Request | Request contains invalid or incomplete data |
| Server error | 500 Internal Server Error | Unexpected server error occurred |

## Input Validation

All product inputs are validated according to these rules:

| Field | Validation Rules |
|-------|-----------------|
| `name` | Required; minimum 2 characters |
| `description` | Required; cannot be empty |
| `price` | Required; must be positive number (> 0) |
| `category` | Required; cannot be empty |
| `stockQuantity` | Required; must be non-negative integer (>= 0) |
| `imageUrl` | Optional; can be null or empty string |

### Validation Error Examples

**Missing Required Field**:
```json
{
  "timestamp": "2024-04-22T10:45:00",
  "status": 400,
  "errorCode": "INVALID_PRODUCT_DATA",
  "message": "Product name is required",
  "path": "/api/v1/products"
}
```

**Price Must Be Positive**:
```json
{
  "timestamp": "2024-04-22T10:45:00",
  "status": 400,
  "errorCode": "INVALID_PRODUCT_DATA",
  "message": "Price must be a positive number",
  "path": "/api/v1/products"
}
```

**Invalid Stock Quantity**:
```json
{
  "timestamp": "2024-04-22T10:45:00",
  "status": 400,
  "errorCode": "INVALID_PRODUCT_DATA",
  "message": "Stock quantity must be a non-negative integer",
  "path": "/api/v1/products"
}
```

## In-Memory Data Storage

### How It Works

- Products are stored in a `List<Product>` within the `ProductService` class
- Sample data is automatically initialized when the application starts
- Data persists during the application's runtime
- All data is lost when the application restarts

### Initial Sample Data

The application comes pre-loaded with 12 sample products including:
- 3 Bracelets (Gold, Rose Gold, Jade)
- 4 Necklaces (Silver, Sapphire, Crystal, Turquoise)
- 3 Rings (Diamond, Moonstone, Emerald)
- 2 Earrings (Pearl, Gold Hoop)

### ID Generation Strategy

- IDs are generated using a counter (`nextId`) starting from 1
- Each new product receives the next available ID in sequence
- IDs are globally unique and never reused
- IDs remain stable until application restart

## Code Documentation

### Javadoc Comments

All public classes, methods, and significant logic are documented with Javadoc comments including:
- Purpose and description of the component
- Parameter descriptions with constraints
- Return value descriptions
- Exception declarations
- Usage examples where applicable

### Code Structure

The application follows a layered architecture:

```
HTTP Request
    ↓
ProductController (HTTP layer)
    ↓
ProductService (Business logic layer)
    ↓
Product (Data model)
    ↓
Exception Handlers (Error response formatting)
```

## Known Limitations

1. **No Persistence**: Data is stored in memory only and is lost on application restart
2. **Single Thread**: Not optimized for high concurrent load
3. **No Authentication**: All endpoints are publicly accessible
4. **No Rate Limiting**: No protection against request floods
5. **No Pagination**: All products returned in a single response
6. **No Database**: Cannot scale beyond application memory constraints
7. **No Transactions**: No multi-operation rollback support
8. **No Caching**: Every request hits the data layer

### Future Improvements

To deploy this API to production, consider:
- Adding a relational database (PostgreSQL, MySQL)
- Implementing Spring Data JPA for persistence
- Adding authentication and authorization (Spring Security)
- Implementing pagination and sorting
- Adding API rate limiting
- Setting up caching (Redis)
- Adding comprehensive unit and integration tests
- Implementing API versioning
- Adding API documentation (Swagger/OpenAPI)

## Testing the API

### Using Postman

1. Import the API endpoints listed above
2. Test each endpoint with the provided examples
3. Verify correct status codes and response formats

### Using cURL

All cURL examples are provided in the API Endpoint Reference section above.

### Using Thunder Client (VSCode)

1. Install Thunder Client extension in VSCode
2. Create a new collection
3. Add requests for each endpoint
4. Test with the provided examples

### Test Scenarios

The lab requires testing:
- ✅ Create at least 3 products via POST
- ✅ Retrieve all products via GET
- ✅ Retrieve individual products via GET with ID
- ✅ Update a product via PUT
- ✅ Partially update a product via PATCH
- ✅ Delete a product via DELETE
- ✅ Filter products by different criteria
- ✅ Test error cases (invalid ID, missing data)

## Git Workflow

This project follows the Git Feature Branch workflow:

```bash
# Create feature branch
git checkout -b feat/product-filtering

# Work on feature
git add .
git commit -m "feat: implemented product filtering by price range"

# Push to remote
git push origin feat/product-filtering

# Create Pull Request and merge to main
git checkout main
git merge feat/product-filtering
git push origin main

# Delete feature branch
git branch -d feat/product-filtering
```

## Commit Message Format

Follow the "Action + Result" format:
```
<Type>: <action phrase describing what was implemented>
```

Examples:
- `feat: implemented product filtering by price range`
- `feat: created ProductService with CRUD operations`
- `fix: resolved getAllProducts() returning null values`
- `docs: added comprehensive API documentation`

## Code Quality Standards

### Naming Conventions

- Classes: PascalCase (e.g., `ProductService`)
- Methods: camelCase (e.g., `getProductById()`)
- Constants: UPPER_SNAKE_CASE (e.g., `MAX_PRODUCT_PRICE`)
- Variables: camelCase (e.g., `productList`)

### Avoid Magic Numbers and Strings

Bad:
```java
if (price > 10000) { ... }  // What is 10000?
```

Good:
```java
private static final double PREMIUM_PRODUCT_THRESHOLD = 10000.0;
if (price > PREMIUM_PRODUCT_THRESHOLD) { ... }
```

### Use Descriptive Method Names

Bad:
```java
public List<Product> filter() { ... }
```

Good:
```java
public List<Product> filterProductsByPriceRange(double minPrice, double maxPrice) { ... }
```

## Dependencies

### Maven Dependencies (pom.xml)
- **spring-boot-starter-web**: Web MVC and REST support
- **lombok**: Boilerplate reduction (@Data, @Getter, @Setter, etc.)
- **spring-boot-starter-test**: Testing framework

### No External APIs
This application has no external API dependencies and works completely standalone.

## Troubleshooting

### Port Already in Use
```bash
# Kill process on port 8080
# Windows: netstat -ano | findstr :8080
# Linux/Mac: lsof -i :8080
```

### Build Errors
```bash
# Clean and rebuild
mvn clean install -U
```

### IDE Configuration Issues
- Ensure Java 25+ is configured in your IDE
- Rebuild the project and invalidate caches
- Check that Lombok annotation processor is enabled

## Contact & Support

This project was completed as part of Laboratory 7: HTTP Fundamentals and Spring Boot.

**Pair Programming Teams**: Both partners must understand all implementations.

---

**Last Updated**: April 22, 2024
**Version**: 1.0.0
**Status**: Complete and tested

This README covers all aspects of the Spring Boot REST API including architecture, endpoints, data validation, error handling, and testing procedures. All code includes comprehensive Javadoc documentation for future maintenance and understanding.
