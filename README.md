ShopEase E-Commerce Platform - Master Index

Documentation Navigation

Welcome to the ShopEase e-commerce platform documentation. This is your central hub for all project information, setup guides, and technical documentation.

First time here?** Follow these steps:

1. Read the Overview: Start with [Project Overview](#project-overview)
2. Run Setup: Follow [SETUP_GUIDE.md](./SETUP_GUIDE.md)
5. Understand the Code: Review the appropriate README files
6. Test the Integration: Use [SETUP_GUIDE.md - Phase 4](./SETUP_GUIDE.md#phase-4-full-stack-integration-testing)

Project Overview

Laboratory 8: Database Integration and Consuming RESTful Web Services with Fetch API

ShopEase is a complete e-commerce platform demonstrating:
- Backend: Spring Boot REST API with MySQL database and JPA/Hibernate
- Frontend: Dynamic HTML/CSS/JavaScript with Fetch API integration
- Integration: Full-stack communication with error handling and CORS
- Database: Persistent storage with relational entity modeling
- Testing: Complete integration tests and verification procedures

Status: Complete and Production Ready

Project Structure

```
Documents/
├── E-commerce Interface Lab/          # Frontend Project
│   ├── TASK1.html - TASK6.html       # Pages
│   ├── app.js                        # Fetch API Integration (UPDATED)
│   ├── stylesheet.css                # Styles
│   └── README.md                     # Frontend Documentation
│
├── shopease-backend/                 # Backend Project
│   ├── pom.xml                       # Maven Configuration
│   ├── src/main/java/com/shopease/   # Java Source Code
│   │   ├── ShopEaseApplication.java
│   │   ├── controller/               # REST Controllers (25+ endpoints)
│   │   ├── service/                  # Business Logic
│   │   ├── repository/               # Data Access (JPA Repositories)
│   │   ├── entity/                   # JPA Entities (4 entities)
│   │   ├── exception/                # Global Error Handling
│   │   └── config/                   # CORS Configuration
│   ├── src/main/resources/
│   │   └── application.yaml          # Database Configuration
│   └── README.md                     # Backend Documentation
│
├── SETUP_GUIDE.md                    # Complete Setup Instructions
├── COMPLETION_SUMMARY.md             # What Was Completed
└── README.md (this file)             # Master Index
=======
# E-commerce API - Spring Boot Backend

A fully functional RESTful API backend for an e-commerce product management system, built with Spring Boot 4.0.5. This API demonstrates HTTP fundamentals, REST principles, and proper application architecture with in-memory data storage.

## Project Overview

This is a complete implementation of Laboratory 7 - HTTP Fundamentals and Spring Boot. The API provides full CRUD operations for managing products with proper HTTP status codes, error handling, and comprehensive filtering capabilities.

### Key Features

- Full CRUD operations for products (Create, Read, Update, Delete)
- Multiple filtering options (by category, name, and price range)
- Proper HTTP status codes (200, 201, 204, 400, 404, 500)
- Comprehensive error handling with consistent response format
- Input validation with meaningful error messages
- In-memory data storage with sample products
- RESTful API design with semantic endpoints
- Complete Javadoc documentation
- Production-ready code structure

Technology Stack

- Framework: Spring Boot 4.0.5
- Build Tool: Maven
- Language: Java 25+
- Libraries: Lombok (for reducing boilerplate)
- Data Storage: In-memory List<Product>

Project Structure

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

Setup Instructions

Prerequisites

- Java 25 or higher
- Maven 3.6+
- Git
- IDE: IntelliJ IDEA, VSCode, or Eclipse

Installation Steps

1. Clone the repository
   ```bash
   git clone <repository-url>
   cd EcommerceApi
   ```

2. Build the project
   ```bash
   mvn clean install
   ```

3. Run the application
   ```bash
   mvn spring-boot:run
   ```
   
   Or compile and run directly:
   ```bash
   mvn clean compile exec:java -Dexec.mainClass="com.ws101.EcommerceApiApplication"
   ```

4. Verify the application
   - The API will be available at: `http://localhost:8080`
   - Health check: `http://localhost:8080/api/v1/products`

API Endpoint Reference

Base URL
```
http://localhost:8080/api/v1
```

1. Get All Products

Endpoint: `GET /api/v1/products`

Description: Retrieves a list of all products in the system.

Status Code: `200 OK`

Example Request:
```bash
curl -X GET http://localhost:8080/api/v1/products
```

Example Response:
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
>>>>>>> b062a4e06f8853af8a4274091f6cd8390e914276
```

---

Documentation Files

 1. [SETUP_GUIDE.md](./SETUP_GUIDE.md)

Complete step-by-step guide for setting up and running the entire project
Contents:
- Prerequisites and system requirements
- Phase 1: Database setup (MySQL)
- Phase 2: Backend configuration and startup
- Phase 3: Frontend setup with web server
- Phase 4: Full-stack integration testing
- Phase 5: CORS verification
- Phase 6: Data persistence testing
- API testing examples (Postman, curl, browser)
- Comprehensive troubleshooting (8+ common issues)
- Performance optimization tips
- Production deployment checklist
- Git workflow examples

**Time to complete**: ~30-45 minutes (first time)

2. **[shopease-backend/README.md](./shopease-backend/README.md)** - Backend Documentation

**Comprehensive backend documentation with architecture, API reference, and development guide**

**Contents:**
- Project overview and features
- Project structure with directory tree
- Database schema with relationships
- Complete API endpoint reference (25+ endpoints)
  - Category endpoints (6)
  - Product endpoints (11)
  - Order endpoints (9)
- Setup and configuration instructions
- JPA entity relationships explanation
- Custom repository queries
- Service layer benefits
- Error response formats
- Development tips and best practices
- Testing checklist
- Future enhancements

Best for: Understanding the backend architecture and API design

3. [E-commerce Interface Lab/README.md](./E-commerce%20Interface%20Lab/README.md) - Frontend Documentation

Complete frontend documentation with Fetch API integration details

Contents:
- Project overview and features
- Project structure
- All pages overview (TASK1-6.html)
- Fetch API integration section
- Core async functions with error handling
- Key implementation details (before/after)
- Setup and configuration
- Running locally (multiple options)
- CORS verification
- Testing checklist
- Responsive design details
- Accessibility features
- Troubleshooting guide
- Browser support

Best for: Understanding Fetch API integration and frontend architecture

 4. [COMPLETION_SUMMARY.md](./COMPLETION_SUMMARY.md) - What Was Delivered

Detailed summary of everything that was implemented and completed

Contents:
- Complete checklist of all deliverables
- Part 1: Backend Database Integration (Complete)
- Part 2: REST Controller Updates (Complete)
- Part 3: Frontend Fetch API (Complete)
- Part 4: Integration & Polish (Complete)
- Project deliverables summary
- Laboratory objectives status
- Key achievements
- Code statistics
- Learning outcomes
- Maintenance and future development
- Final verification checklist

Best for: Verifying that all requirements were met

 Key Features Implemented

 Backend Features
- MySQL database with JPA/Hibernate ORM
- Spring Boot REST API with 25+ endpoints
- 4 JPA entities with complex relationships
- Custom repository queries with filtering
- Service layer pattern for business logic
- Global error handling with @ControllerAdvice
- CORS configuration for cross-origin requests
- Automatic table creation (Hibernate DDL)
- Transaction management with @Transactional
- Comprehensive API documentation

Frontend Features
- Fetch API with async/await
- Error handling with try/catch blocks
- Dynamic product rendering from API
- Product filtering (category, price, search)
- Shopping cart with localStorage
- Checkout form with order submission
- Order history and tracking
- Responsive design (mobile, tablet, desktop)
- Accessibility features (ARIA, semantic HTML)
- Graceful error recovery

Integration Features
- Full-stack database to browser communication
- Data persistence across server restarts
- Complex entity relationships working correctly
- CORS properly configured
- Error handling at all layers
- Form validation and submission
- Order history retrieval

How to Navigate This Documentation

If you want to...

| Goal | Document | Location |
|------|----------|----------|
| Set up the project | SETUP_GUIDE.md | Root directory |
| Understand the backend | shopease-backend/README.md | Backend directory |
| Understand the frontend | E-commerce Interface Lab/README.md | Frontend directory |
| See what was completed | COMPLETION_SUMMARY.md | Root directory |
| Learn database schema | shopease-backend/README.md Section: Database Schema | Backend README |
| Learn API endpoints | shopease-backend/README.md Section: API Endpoints | Backend README |
| Learn Fetch API usage | E-commerce Interface Lab/README.md Section: Fetch API Integration | Frontend README |
| Troubleshoot issues | SETUP_GUIDE.md Section: Troubleshooting | Setup Guide |
| Test the API | SETUP_GUIDE.md Section: API Testing Examples | Setup Guide |
| Understand error handling | shopease-backend/README.md + E-commerce Interface Lab/README.md | Both READMEs |
| Deploy to production| SETUP_GUIDE.md Section: Production Deployment | Setup Guide |

Technology Stack

Backend
- **Framework**: Spring Boot 3.1.5
- **Language**: Java 17
- **ORM**: JPA/Hibernate
- **Database**: MySQL 8.0+
- **Build Tool**: Maven 3.8+
- **Additional**: Lombok, Spring Data JPA

Frontend
- **Language**: Vanilla JavaScript (ES6+)
- **Markup**: HTML5
- **Styling**: CSS3
- **API**: Fetch API (async/await)
- **Storage**: localStorage (JSON)
- **Browsers**: Chrome, Firefox, Safari, Edge (latest)

Project Statistics

- **Total Classes**: 13+ (entities, repositories, services, controllers)
- **API Endpoints**: 25+
- **Database Tables**: 4 (categories, products, orders, order_items)
- **Entity Relationships**: 4 (1-to-M, M-to-1 combinations)
- **Documentation Files**: 4 comprehensive guides
- **Code Lines**: 3000+ production code
- **Test Scenarios**: 15+ integration tests covered

Verification Checklist

Before you start, verify you have:

- [ ] Java JDK 17 or higher installed
- [ ] Maven 3.8+ installed
- [ ] MySQL 8.0+ installed
- [ ] Web browser (Chrome, Firefox, Safari, Edge)
- [ ] Text editor or IDE (VS Code, IntelliJ, Eclipse)
- [ ] Recommended: Postman or REST Client extension

Learning Path

Beginner Level
1. Read [SETUP_GUIDE.md - Project Structure Overview](#) 
2. Follow Phase 1-3 of setup guide
3. Browse through the HTML pages (TASK1-6.html)
4. Review the endpoint list in backend README

Intermediate Level
1. Study the entity relationships in shopease-backend/README.md
2. Review the repository implementations
3. Trace through the Fetch API calls in app.js
4. Run tests from SETUP_GUIDE.md Phase 4

### Advanced Level
1. Study service layer patterns
2. Review global exception handling
3. Examine entity lifecycle callbacks
4. Implement additional features (pagination, caching, etc.)

---

Common Issues & Solutions

### "Connection refused" to backend
→ See [SETUP_GUIDE.md - Troubleshooting](./SETUP_GUIDE.md#troubleshooting)

### "CORS error" in browser
→ See [SETUP_GUIDE.md - CORS Verification](./SETUP_GUIDE.md#phase-5-cors-verification)

### "Database table doesn't exist"
→ See [SETUP_GUIDE.md - Troubleshooting](./SETUP_GUIDE.md#problem-table-doesnt-exist-error)

### Products not loading
→ See [SETUP_GUIDE.md - Troubleshooting](./SETUP_GUIDE.md#problem-products-not-loading)

Additional Resources

Official Documentation
- [Spring Boot Documentation](https://spring.io/projects/spring-boot)
- [Spring Data JPA](https://spring.io/projects/spring-data-jpa)
- [Fetch API MDN](https://developer.mozilla.org/en-US/docs/Web/API/Fetch_API)
- [MySQL Documentation](https://dev.mysql.com/doc/)

Tools & IDE Extensions (Recommended)
- VS Code Extensions:
  - Extension Pack for Java
  - Spring Boot Extension Pack
  - REST Client
  - MySQL
  - Thunder Client / REST Client

External Tools
- [Postman](https://www.postman.com/) - API testing
- [DBeaver](https://dbeaver.io/) - Database management
- [MySQL Workbench](https://www.mysql.com/products/workbench/) - MySQL GUI

---

 Next Steps

To Get Started:
1. **Read**: [SETUP_GUIDE.md](./SETUP_GUIDE.md) - Prerequisites Section
2. **Install**: Required software (Java, Maven, MySQL)
3. **Configure**: Database connection in application.yaml
4. **Run**: Backend server and frontend web server
5. **Test**: Full-stack integration

 To Understand the Code:
1. Review entity relationships in backend README
2. Study repository custom queries
3. Trace through Fetch API calls in frontend
4. Test API endpoints with Postman

To Deploy:
1. Review Production Deployment Checklist in SETUP_GUIDE.md
2. Secure database credentials
3. Configure CORS for production domain
4. Set Hibernate DDL to "validate"
5. Enable logging to external system

---

 Project Organization

This project follows industry best practices:

- **Separation of Concerns**: Controllers, services, repositories separated
- **DRY Principle**: Reusable code in services and utilities
- **SOLID Principles**: Single responsibility, dependency injection
- **Documentation**: Javadoc on all public methods
- **Error Handling**: Consistent error responses
- **Testing**: Integration test scenarios documented

---

 What Makes This Project Special
1. **Production-Ready Code**: Not just a tutorial project, but real-world patterns
2. **Complete Documentation**: 4 comprehensive guides covering everything
3. **Full-Stack Integration**: Database → API → Frontend working seamlessly
4. **Error Handling**: Proper error handling at every layer
5. **Best Practices**: Spring Boot conventions, REST API standards, async JavaScript
6. **Learning Value**: Demonstrates key technologies in modern web development
7. **Scalable Architecture**: Ready for enhancements and deployment
8. **Testing Documentation**: Clear instructions for integration testing

---

 Support & Help

If you encounter issues:

1. **Check the Troubleshooting Guide**: [SETUP_GUIDE.md#troubleshooting](./SETUP_GUIDE.md#troubleshooting)
2. **Review the Appropriate README**: Backend or Frontend based on the issue
3. **Check Browser Console**: F12 for JavaScript errors
4. **Check Backend Logs**: Spring Boot console output
5. **Verify Database**: Use MySQL to check if data persists

---

## 🎉 You're Ready to Go!

Everything you need is documented here. Start with the [SETUP_GUIDE.md](./SETUP_GUIDE.md) and follow the phases.

**Happy coding!** 🚀

---

**Project Version**: 1.0.0  
**Last Updated**: April 29, 2026  
**Status**:  Production Ready  

For the most current information, refer to the individual README files in each project directory.
=======
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
- Create at least 3 products via POST
- Retrieve all products via GET
- Retrieve individual products via GET with ID
- Update a product via PUT
- Partially update a product via PATCH
- Delete a product via DELETE
- Filter products by different criteria
- Test error cases (invalid ID, missing data)

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

