# ShopEase Full-Stack Setup & Integration Guide

## Laboratory 8: Database Integration and Consuming RESTful Web Services with Fetch API

This guide provides step-by-step instructions to set up, configure, and test the complete ShopEase e-commerce platform with database integration and Fetch API integration.

## Project Structure Overview

```
C:\Users\admin\OneDrive\Documents\
├── E-commerce Interface Lab\        # Frontend (HTML/CSS/JavaScript)
│   ├── app.js                      # Fetch API integration
│   ├── TASK1.html - TASK6.html     # Pages
│   └── README.md                   # Frontend documentation
│
└── shopease-backend\               # Spring Boot backend
    ├── pom.xml                     # Maven configuration
    ├── src/main/java/com/shopease/ # Java source code
    ├── src/main/resources/         # Configuration files
    └── README.md                   # Backend documentation
```

## Prerequisites

### System Requirements

- **Java Development Kit**: JDK 17 or higher
  - Download: https://www.oracle.com/java/technologies/downloads/
  - Verify: `java -version` in terminal

- **Maven**: 3.8.0 or higher
  - Download: https://maven.apache.org/download.cgi
  - Verify: `mvn -version` in terminal

- **MySQL**: 8.0 or higher
  - Download: https://dev.mysql.com/downloads/mysql/
  - Or use: MySQL Workbench, DBeaver, or similar tool

- **Code Editor**: Visual Studio Code or similar
  - Recommended extensions: Spring Boot Extension Pack, MySQL, REST Client

- **Web Browser**: Chrome, Firefox, or Safari (with Developer Tools)

### IDE Setup (Optional but Recommended)

- **IntelliJ IDEA Community Edition**: https://www.jetbrains.com/idea/
- **Spring Tool Suite**: https://spring.io/tools
- **VS Code with Java Extension Pack**

## Step-by-Step Setup

### Phase 1: Database Setup

#### Step 1.1: Start MySQL Server

**Windows (using MySQL Command Line):**
```bash
# Open terminal and connect to MySQL
mysql -u root -p
# Enter your password when prompted
```

**macOS (using Homebrew):**
```bash
brew services start mysql
mysql -u root -p
```

#### Step 1.2: Create Database and Schema

In MySQL command line:

```sql
-- Create database
CREATE DATABASE shopease_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- Use the database
USE shopease_db;

-- Verify (should show shopease_db)
SELECT DATABASE();
```

#### Step 1.3: Verify Connection

```bash
# From terminal (replace 'root' with your username if different)
mysql -u root -p shopease_db -e "SHOW TABLES;"
```

You may see no tables initially - Hibernate will create them on first run.

### Phase 2: Backend Setup

#### Step 2.1: Navigate to Backend Directory

```bash
cd "C:\Users\admin\OneDrive\Documents\shopease-backend"
```

#### Step 2.2: Configure Database Connection

Edit `src/main/resources/application.yaml`:

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/shopease_db?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true
    username: root                    # Change if different
    password: your_password           # CHANGE THIS to your MySQL password
    driver-class-name: com.mysql.cj.jdbc.Driver
  
  jpa:
    hibernate:
      ddl-auto: update               # Creates/updates tables automatically
    show-sql: true                    # Shows SQL queries in console
```

#### Step 2.3: Build Backend

```bash
# Download dependencies and compile
mvn clean install
```

**Expected output:**
```
[INFO] BUILD SUCCESS
[INFO] Total time: XX.XXX s
```

#### Step 2.4: Start Backend Server

```bash
# Start Spring Boot application
mvn spring-boot:run
```

**Expected output:**
```
  .   ____          _            __ _ _
 /\\ / ___'_ __ _ _(_)_ __  __ _ \ \ \ \
( ( )\___ | '_ | '_| | '_ \/ _` | \ \ \ \
 \\/  ___)| |_)| | | | | || (_| |  ) ) ) )
  '  |____| ._ \_| |_|_| \_ \__, | / / / /
 =========|_|==============|___/=/_/_/_/

Started ShopEaseApplication in X.XXX seconds (JVM running for X.XXX)
```

**Note:** Leave this terminal running. The backend is now available at `http://localhost:8080`

#### Step 2.5: Verify Backend is Running

Open a NEW terminal and test:

```bash
# Test if backend is responding
curl http://localhost:8080/api/products

# Expected: Should return JSON (empty array initially)
# []
```

### Phase 3: Frontend Setup

#### Step 3.1: Open Frontend Project

In a NEW terminal:

```bash
cd "C:\Users\admin\OneDrive\Documents\E-commerce Interface Lab"
```

#### Step 3.2: Verify Frontend Files

```bash
# List files to verify structure
dir

# Should show: TASK1.html, TASK2.html, ... TASK6.html, app.js, stylesheet.css, README.md
```

#### Step 3.3: Start Web Server

**Option A: Using Python (Built-in on Windows 10+)**

```bash
# Python 3.x
python -m http.server 5500

# Or Python 2.x (if 3.x not available)
python -m SimpleHTTPServer 5500
```

**Option B: Using Node.js**

```bash
# Install globally (one-time)
npm install -g http-server

# Run server
http-server -p 5500
```

**Option C: Using VS Code Live Server**

1. Install "Live Server" extension in VS Code
2. Right-click `TASK1.html`
3. Select "Open with Live Server"
4. Browser opens automatically on `http://localhost:5500`

**Expected output:**
```
Serving HTTP on 0.0.0.0 port 5500 ...
```

#### Step 3.4: Access Frontend

Open browser and navigate to:
```
http://localhost:5500/TASK1.html
```

You should see the ShopEase home page.

### Phase 4: Full-Stack Integration Testing

#### Step 4.1: Populate Initial Data (Optional)

**Method 1: Using REST Client (VS Code)**

1. Install "REST Client" extension in VS Code
2. Create file `test.http` with:

```http
### Create Category - Necklaces
POST http://localhost:8080/api/categories
Content-Type: application/json

{
  "name": "necklace",
  "description": "Elegant necklaces and pendants"
}

### Create Category - Bracelets
POST http://localhost:8080/api/categories
Content-Type: application/json

{
  "name": "bracelet",
  "description": "Stylish bracelets and bangles"
}

### Create Product - Gold Necklace
POST http://localhost:8080/api/products
Content-Type: application/json

{
  "name": "Gold Necklace",
  "price": 9999,
  "originalPrice": 11999,
  "description": "A premium necklace with a warm gold finish",
  "tagline": "A polished 18K-inspired statement piece",
  "badge": "Best Seller",
  "image": "images/necklace.jpg",
  "category": { "id": 1 }
}

### Create Product - Silver Bracelet
POST http://localhost:8080/api/products
Content-Type: application/json

{
  "name": "Silver Bracelet",
  "price": 5000,
  "originalPrice": 6500,
  "description": "A sterling-silver-inspired bracelet",
  "tagline": "A lightweight silver accessory",
  "badge": "Limited Offer",
  "image": "images/bracelet.jpg",
  "category": { "id": 2 }
}
```

3. Click "Send Request" on each request

**Method 2: Using curl**

```bash
# Create category
curl -X POST http://localhost:8080/api/categories \
  -H "Content-Type: application/json" \
  -d '{"name":"necklace","description":"Elegant necklaces"}'

# Create product
curl -X POST http://localhost:8080/api/products \
  -H "Content-Type: application/json" \
  -d '{"name":"Gold Necklace","price":9999,"originalPrice":11999,"category":{"id":1}}'
```

#### Step 4.2: Test Frontend Data Loading

1. Refresh browser page at `http://localhost:5500/TASK1.html`
2. **Open Developer Tools** (F12 or Right-click → Inspect)
3. Go to **Console** tab
4. Check for messages:
   - ✅ Should see: `"Fetching products from backend..."`
   - ✅ Should see: `"Successfully fetched X products"`
   - ❌ Should NOT see CORS errors

#### Step 4.3: Verify Products Appear

- Featured Products section should show products from database
- Products page (TASK2.html) should display products

#### Step 4.4: Test Complete Shopping Flow

**Test 1: Add to Cart**
1. Navigate to TASK2.html (Products)
2. Click "Add to Cart" on any product
3. Verify cart count increases
4. Go to TASK4.html (Cart)
5. Should see product in cart

**Test 2: Checkout**
1. Click "Proceed to Checkout"
2. Fill in customer details:
   - Name: Test Customer
   - Email: test@shopease.local
   - Street: 123 Main St
   - City: Manila
   - ZIP: 1200
3. Select payment method
4. Click "Place Order"

**Test 3: Verify Order in Database**

In backend terminal, check MySQL logs to see INSERT query:
```sql
INSERT INTO orders (customer_email, delivery_address, ...) VALUES (...)
```

Or query directly:
```bash
mysql -u root -p shopease_db -e "SELECT * FROM orders;"
```

**Test 4: Verify Order History**
1. Browser redirects to TASK6.html (Orders)
2. Should see latest order displayed
3. All order details (items, total, address) should be present

#### Step 4.5: Database Verification

Open MySQL Workbench or DBeaver:

```sql
USE shopease_db;

-- View all tables
SHOW TABLES;

-- View products
SELECT * FROM products;

-- View orders
SELECT * FROM orders;

-- View order items
SELECT * FROM order_items;

-- View categories
SELECT * FROM categories;
```

### Phase 5: CORS Verification

#### Step 5.1: Check CORS Headers

1. Open browser Developer Tools (F12)
2. Go to Network tab
3. Refresh page
4. Click on first request to `/api/products`
5. Go to Response Headers tab
6. Should see:
   ```
   Access-Control-Allow-Origin: http://localhost:5500
   Access-Control-Allow-Methods: GET, POST, PUT, DELETE, OPTIONS
   Access-Control-Allow-Headers: Authorization, Content-Type
   ```

#### Step 5.2: Check for Errors

In Console tab, verify:
- ✅ No red CORS errors
- ✅ Products fetch successfully
- ✅ API responses logged correctly

### Phase 6: Data Persistence Testing

#### Step 6.1: Verify Data Persists

1. Create an order (from Phase 4.4 Test 2)
2. Stop backend server (Ctrl+C)
3. Wait 2-3 seconds
4. Restart backend: `mvn spring-boot:run`
5. Wait for startup (should be faster - tables already exist)
6. Query database: `SELECT * FROM orders;`
7. **Previous order should still exist** ✓

#### Step 6.2: Verify Relationships

```sql
-- View product with category relationship
SELECT p.id, p.name, c.name as category 
FROM products p 
JOIN categories c ON p.category_id = c.id;

-- View order with items and products
SELECT o.id, oi.quantity, p.name 
FROM orders o 
JOIN order_items oi ON o.id = oi.order_id 
JOIN products p ON oi.product_id = p.id;
```

## API Testing Examples

### Using Postman

1. Install Postman from https://www.postman.com/downloads/
2. Create requests:

| Method | URL | Purpose |
|--------|-----|---------|
| GET | `http://localhost:8080/api/products` | Get all products |
| GET | `http://localhost:8080/api/products/discounted` | Get discounted items |
| GET | `http://localhost:8080/api/products/search?term=gold` | Search products |
| GET | `http://localhost:8080/api/categories` | Get all categories |
| POST | `http://localhost:8080/api/orders` | Create order (use JSON body) |

### Using Browser Fetch

In browser Console (F12):

```javascript
// Fetch all products
fetch('http://localhost:8080/api/products')
  .then(r => r.json())
  .then(data => console.log('Products:', data));

// Create category
fetch('http://localhost:8080/api/categories', {
  method: 'POST',
  headers: { 'Content-Type': 'application/json' },
  body: JSON.stringify({ name: 'rings', description: 'Beautiful rings' })
})
  .then(r => r.json())
  .then(data => console.log('Created:', data));
```

## Troubleshooting

### Problem: "Connection refused" when accessing backend

**Cause:** Backend server not running

**Solution:**
```bash
# In backend directory
mvn spring-boot:run

# Wait for "Started ShopEaseApplication"
```

### Problem: "CORS error" in browser console

**Cause:** CORS not configured correctly on backend

**Solution:**
1. Verify `WebConfig.java` has correct allowed origins
2. Check that frontend URL matches (http://localhost:5500)
3. Clear browser cache (Ctrl+Shift+Delete)
4. Hard refresh (Ctrl+Shift+R)

### Problem: Database connection error "Access denied"

**Cause:** Wrong username/password in application.yaml

**Solution:**
```yaml
spring:
  datasource:
    username: root      # Your MySQL username
    password: password  # Your MySQL password (if any)
```

### Problem: "Table doesn't exist" error

**Cause:** Database not created or Hibernate ddl-auto failed

**Solution:**
```bash
# Create database manually
mysql -u root -p -e "CREATE DATABASE shopease_db;"

# Restart backend - Hibernate will create tables
mvn spring-boot:run
```

### Problem: Products not loading on frontend

**Cause:** Backend not running or API URL incorrect

**Solution:**
1. Verify backend running: `curl http://localhost:8080/api/products`
2. Check browser Console for specific error
3. Verify API_BASE_URL in app.js: `http://localhost:8080/api`

### Problem: Port 5500 already in use

**Solution:**
```bash
# Use different port
python -m http.server 8000

# Then access: http://localhost:8000/TASK1.html
```

### Problem: Port 8080 already in use

**Solution:**
```bash
# Change port in application.yaml
server:
  port: 8081  # Use different port

# Then access: http://localhost:8081/api/products
```

## Performance Optimization Tips

### Backend

1. **Use Pagination** for large datasets
   ```java
   @GetMapping
   public ResponseEntity<Page<Product>> getAllProducts(Pageable pageable) {
       return ResponseEntity.ok(productRepository.findAll(pageable));
   }
   ```

2. **Add Indexing** on frequently queried columns
   ```java
   @Column(columnDefinition = "VARCHAR(255) UNIQUE")
   private String name;
   ```

3. **Cache Results** for expensive queries
   ```java
   @Cacheable("products")
   public List<Product> getAllProducts() { ... }
   ```

### Frontend

1. **Lazy Load Images**
   ```html
   <img loading="lazy" src="...">
   ```

2. **Minimize API Calls** by caching PRODUCTS array

3. **Use Debouncing** for search
   ```javascript
   const searchDebounce = debounce(searchFunction, 300);
   ```

## Production Deployment Checklist

- [ ] Change MySQL password from default
- [ ] Set `spring.jpa.hibernate.ddl-auto=validate` (not `update`)
- [ ] Disable `spring.jpa.show-sql=false`
- [ ] Configure proper CORS origins (not `*`)
- [ ] Enable HTTPS on frontend and backend
- [ ] Set up database backups
- [ ] Configure logging to external system
- [ ] Add API rate limiting
- [ ] Implement authentication/authorization
- [ ] Set up monitoring and alerting

## GIT Workflow (Version Control)

### Setup Git (if not already done)

```bash
cd shopease-backend
git init
git add .
git commit -m "Initial commit: Backend with JPA and CORS"

cd ../E-commerce\ Interface\ Lab
git init
git add .
git commit -m "Initial commit: Frontend with Fetch API integration"
```

### Creating Feature Branches

```bash
# For database feature
git checkout -b feat/db-integration
# Make changes...
git commit -m "feat: Add database configuration and entities"
git push

# Merge to main
git checkout main
git merge feat/db-integration
```

## Summary

You have successfully:

✅ Set up MySQL database  
✅ Configured Spring Boot backend with JPA entities  
✅ Created REST API endpoints with CRUD operations  
✅ Implemented global error handling with CORS  
✅ Updated frontend to use Fetch API instead of hardcoded data  
✅ Tested full-stack integration from database to browser  
✅ Verified data persistence across server restarts  
✅ Documented entire system  

## Next Steps

1. **Add Authentication**: Implement Spring Security
2. **Add Validation**: Use @Valid and custom validators
3. **Add Documentation**: Generate Swagger/OpenAPI docs
4. **Add Testing**: Unit tests for services, integration tests
5. **Add Caching**: Redis for frequently accessed data
6. **Add Monitoring**: Application Performance Monitoring
7. **Deploy**: Use Docker containers for easy deployment

## References

- [Spring Boot Documentation](https://spring.io/projects/spring-boot)
- [Spring Data JPA](https://spring.io/projects/spring-data-jpa)
- [Fetch API MDN Docs](https://developer.mozilla.org/en-US/docs/Web/API/Fetch_API)
- [MySQL Documentation](https://dev.mysql.com/doc/)
- [REST API Best Practices](https://restfulapi.net/)

## Support

For issues or questions:
1. Check the troubleshooting section above
2. Review backend logs: `mvn spring-boot:run` console output
3. Check browser console logs: F12 → Console tab
4. Query database directly to verify data persistence
5. Use Postman to test individual API endpoints

---

**Laboratory 8 Complete!** 🎉

You have successfully implemented a production-ready e-commerce platform with:
- **Persistent database** using MySQL and JPA/Hibernate
- **RESTful API** with Spring Boot
- **Dynamic frontend** consuming the API with Fetch API
- **Full error handling** and CORS configuration
- **Complete shopping experience** from browsing to checkout
