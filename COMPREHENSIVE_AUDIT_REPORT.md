# E-Commerce Interface Lab - Comprehensive Audit Report
**Date:** May 27, 2026  
**Status:** Complete Analysis of All Integration Issues

---

## Executive Summary

The ShopEase e-commerce system has **fundamental architectural mismatches** between frontend and backend that prevent it from functioning as a complete system. Critical issues include:

1. **API routing mismatch** (frontend calls `/api/v1/...`, backend at `/api/...`)
2. **No database initialization** (schema/sample data)
3. **Authentication system conflicts** (JWT vs Session-based)
4. **Missing user/order management** (no persistent user storage)
5. **Search endpoint incompatibility** (different parameter formats)

---

## CRITICAL ISSUES - Fix Before Anything Else

### 1. API Routing Prefix Missing - **BLOCKS ALL REQUESTS**
**Severity:** CRITICAL  
**Impact:** Every API call from frontend fails immediately

**Problem:**
- Frontend `app.js` line 9: `const API_BASE_URL = "http://localhost:8080/api/v1";`
- Backend controllers mapped to: `/api/products`, `/api/orders`, `/api/categories`, `/api/auth`
- All requests hit wrong endpoints (404 Not Found)

**Example:**
- Frontend attempts: `GET http://localhost:8080/api/v1/products`
- Backend provides: `GET http://localhost:8080/api/products`

**Solution:**
Either:
- **Option A:** Add `/v1` prefix to all backend controller mappings
- **Option B:** Change frontend to use `/api` instead of `/api/v1`
- **Recommended:** Option B is simpler (1-line change in app.js)

**Files Affected:**
- [E-commerce Interface Lab/app.js](E-commerce%20Interface%20Lab/app.js#L9) - Line 9
- [shopease-backend/src/main/java/com/shopease/controller/ProductController.java](shopease-backend/src/main/java/com/shopease/controller/ProductController.java#L19)
- [shopease-backend/src/main/java/com/shopease/controller/OrderController.java](shopease-backend/src/main/java/com/shopease/controller/OrderController.java#L19)
- [shopease-backend/src/main/java/com/shopease/controller/CategoryController.java](shopease-backend/src/main/java/com/shopease/controller/CategoryController.java#L18)
- [shopease-backend/src/main/java/com/shopease/controller/AuthController.java](shopease-backend/src/main/java/com/shopease/controller/AuthController.java#L17)

---

### 2. Database Not Initialized - **APP CRASHES ON STARTUP**
**Severity:** CRITICAL  
**Impact:** Spring Boot cannot create Entity Manager, application fails to start

**Problem:**
- MySQL database `shopease_db` must be created manually
- No schema defined
- No sample data loaded
- Entities defined but tables won't exist

**Configuration File:** [shopease-backend/src/main/resources/application.yaml](shopease-backend/src/main/resources/application.yaml)
```yaml
datasource:
  url: jdbc:mysql://localhost:3306/shopease_db
  username: root
  password: root
jpa:
  hibernate:
    ddl-auto: update  # Will try to create/update tables
```

**What's Missing:**
1. SQL script to create database and schema
2. Sample product data
3. Sample category data
4. Test user data

**Required Files to Create:**
- `schema.sql` - Database schema with all tables
- `data.sql` - Sample data for testing
- Or create `data.sql` under `src/main/resources/` for Spring to execute on startup

---

### 3. Search Endpoint Mismatch - **SEARCH FEATURE BROKEN**
**Severity:** CRITICAL  
**Impact:** Product search fails with 404 error

**Problem:**
- Frontend calls: `/products/filter?filterType=name&filterValue=searchTerm`
- Backend provides: `/products/search?term=searchTerm`

**Frontend Code:** [E-commerce Interface Lab/app.js](E-commerce%20Interface%20Lab/app.js#L212)
```javascript
await fetchApi(`/products/filter?filterType=name&filterValue=${encodeURIComponent(searchTerm)}`)
```

**Backend Endpoint:** [ProductController.java](shopease-backend/src/main/java/com/shopease/controller/ProductController.java)
```java
@GetMapping("/search")
public ResponseEntity<List<Product>> searchProducts(@RequestParam String term)
```

**Solution:**
Change frontend to use correct endpoint:
```javascript
await fetchApi(`/products/search?term=${encodeURIComponent(searchTerm)}`)
```

---

### 4. Authentication System Conflict - **LOGIN/REGISTER BROKEN**
**Severity:** CRITICAL  
**Impact:** User authentication completely fails

**Problem #1: Protocol Mismatch**
- Frontend uses form-urlencoded for login: `new URLSearchParams({ username, password })`
- Backend expects JSON: `@RequestBody LoginRequest`

**Frontend Code:** [E-commerce Interface Lab/app.js](E-commerce%20Interface%20Lab/app.js#L241)
```javascript
headers: {
  "Content-Type": "application/x-www-form-urlencoded",  // ❌ WRONG
},
body: new URLSearchParams({ username, password })
```

**Backend Code:** [AuthController.java](shopease-backend/src/main/java/com/shopease/controller/AuthController.java#L35)
```java
@PostMapping("/login")
public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest)  // ❌ Expects JSON
```

**Problem #2: Missing Endpoints**
- Frontend calls `/auth/logout` - **endpoint doesn't exist**
- Frontend calls `/auth/me` - **endpoint doesn't exist**  
- Frontend calls `/auth/csrf` - **endpoint doesn't exist**

**Problem #3: In-Memory User Storage**
- Users only stored in memory (HashMap)
- Lost when app restarts
- No User entity in database

**Solution:**
Either implement proper user management OR implement all the missing endpoints. See HIGH PRIORITY section below.

---

### 5. No Database Schema - **OPERATIONS WILL FAIL**
**Severity:** CRITICAL  
**Impact:** Orders cannot be created, products not stored persistently

**Missing Table Definitions:**
- `categories` table (referenced by products)
- `products` table (main catalog)
- `orders` table (customer orders)
- `order_items` table (line items in orders)

**Also Missing:**
- Foreign key constraints
- Indexes for performance
- Sample data for testing

---

## HIGH PRIORITY ISSUES - Fix Next

### 6. Missing Authentication Endpoints
**Severity:** HIGH  
**Impact:** Login flow cannot complete

**Missing Endpoints:**
1. **`POST /api/auth/logout`** - Not implemented
   - Frontend calls at [app.js line 268](E-commerce%20Interface%20Lab/app.js#L268)
   - Clears session on backend

2. **`GET /api/auth/me`** - Not implemented
   - Frontend calls at [app.js line 276](E-commerce%20Interface%20Lab/app.js#L276)
   - Returns current user info
   - Should return: `{ authenticated: boolean, username: string }`

3. **`GET /api/auth/csrf`** - Not implemented
   - Frontend calls at [app.js line 111](E-commerce%20Interface%20Lab/app.js#L111)
   - Returns CSRF token (if needed)

**Backend Issue:** SecurityConfig disables CSRF but frontend expects it
```java
.csrf(csrf -> csrf.disable())  // Disabled in SecurityConfig
```

---

### 7. No User Entity - Users Not Persistent
**Severity:** HIGH  
**Impact:** User accounts lost on restart, orders not associated with users

**Current Implementation:** [CustomUserDetailsService.java](shopease-backend/src/main/java/com/shopease/security/CustomUserDetailsService.java)
```java
private final Map<String, UserDetails> users = new HashMap<>();  // ❌ In-memory only!
```

**What's Needed:**
1. Create `User` entity with database table
2. Create `UserRepository` extending JpaRepository
3. Modify `CustomUserDetailsService` to load from database
4. Add JPA lifecycle methods to auto-create `user@example.com` and `admin@example.com` on startup

---

### 8. Order Creation Data Model Mismatch
**Severity:** HIGH  
**Impact:** Order creation from checkout will fail

**Frontend Sends:**
```javascript
{
  customerEmail: "customer@example.com",
  deliveryAddress: "123 Main St, City, 12345",
  totalAmount: 2500,
  shippingFee: 99,
  status: "PENDING",
  orderItems: [
    {
      productId: 1,
      quantity: 2,
      unitPrice: 299.99,
      totalPrice: 599.98
    }
  ]
}
```

**Backend Order Entity Issues:**
1. Takes `Order` object but `orderItems` is List<OrderItem>
2. OrderItem requires Product FK, not just productId
3. No service method to resolve productId → Product relationship
4. No validation of product existence

**Solution in OrderService.createOrder():**
```java
public Order createOrder(Order order) {
    // MISSING: Loop through orderItems, validate products exist, link them
    // MISSING: Decrement product stock
    return orderRepository.save(order);
}
```

---

### 9. CORS Configuration Conflict
**Severity:** HIGH  
**Impact:** Cross-origin requests may fail with authorization headers

**Problem:** [WebConfig.java](shopease-backend/src/main/java/com/shopease/config/WebConfig.java)
```java
// First mapping
registry.addMapping("/api/**")
    .allowedOrigins("http://localhost:5500", ...)
    .allowedHeaders("Authorization", "Content-Type", "Accept")
    .allowCredentials(true);

// Second mapping OVERRIDES first one!
registry.addMapping("/api/**")
    .allowedOriginPatterns("*")
    .allowedHeaders("*")
    .allowCredentials(false);  // ❌ Conflicts with first
```

**Issue:** Second mapping disables credentials, but first enables them for Authorization header

---

### 10. No Logout Implementation
**Severity:** HIGH  
**Impact:** Users cannot sign out properly

**Missing Backend Endpoint:**
```java
@PostMapping("/logout")
public ResponseEntity<?> logout() {
    // Currently doesn't exist
    // Frontend calls this at app.js:268
}
```

**JWT Consideration:** Stateless JWT can't be revoked server-side (by design). Frontend should just discard token.

---

## MEDIUM PRIORITY ISSUES - Important but Not Breaking

### 11. Product Data Model Field Mismatch
**Severity:** MEDIUM  
**Impact:** Product images and fields may not display correctly

**Issue 1: Image Field Name**
- Backend uses: `image` (String field in Product entity)
- Frontend expects: both `image` and `imageUrl` (normalizes both)
- Issue: Inconsistent field naming

**Issue 2: Stock vs StockQuantity**
- Backend entity: `stock` (Integer field, default 100)
- Frontend form: `stockQuantity` field name
- When frontend creates product: sends `stockQuantity` but backend expects `stock`

**Affected Code:**
- [shopease-backend/src/main/java/com/shopease/entity/Product.java](shopease-backend/src/main/java/com/shopease/entity/Product.java#L56)
- [E-commerce Interface Lab/app.js](E-commerce Interface Lab/app.js#L1140) - normalizeProduct()

---

### 12. No Stock Management/Decrement
**Severity:** MEDIUM  
**Impact:** Inventory not tracked, overselling possible

**Problem:**
- When order is created, product stock NOT decremented
- Same product can be ordered infinitely
- No stock validation on checkout

**Missing Code in OrderService:**
```java
public Order createOrder(Order order) {
    Order saved = orderRepository.save(order);
    
    // MISSING: For each OrderItem
    //   - Find Product
    //   - Decrement stock
    //   - Save Product
    
    return saved;
}
```

---

### 13. Missing Category Filtering Backend
**Severity:** MEDIUM  
**Impact:** Category filters in UI work client-side only

**Problem:**
- Frontend has category checkboxes (TASK2.html)
- Filters applied client-side in JavaScript (FALLBACK_PRODUCTS only)
- No server-side filtering by category

**Frontend Code:** [E-commerce Interface Lab/app.js](E-commerce%20Interface%20Lab/app.js#L378-L385)
```javascript
// Only filters cached PRODUCTS, not from backend
filtered = filtered.filter((product) =>
    selectedCategories.includes(normalizeCategory(product.category))
);
```

**Backend Provides:** `/api/products/category/{categoryName}` and `/api/products/categoryId/{categoryId}`
- But frontend doesn't use these endpoints when filtering

---

### 14. Missing Sample Data / Seed Data
**Severity:** MEDIUM  
**Impact:** System appears broken (no products to browse)

**Problem:**
- No sample categories in database
- No sample products in database
- Frontend FALLBACK_PRODUCTS used as dummy data

**Current Fallback:** [app.js lines 18-48](E-commerce%20Interface%20Lab/app.js#L18)
```javascript
const FALLBACK_PRODUCTS = normalizeProducts([
  { id: 1, name: "Gold Bracelet", ... },
  { id: 2, name: "Silver Necklace", ... },
  { id: 3, name: "Pearl Earrings", ... },
  { id: 4, name: "Diamond Ring", ... }
]);
```

**Solution:** Create `data.sql` or `import.sql` with sample data

---

### 15. JWT Configuration Weak Secret
**Severity:** MEDIUM  
**Impact:** Security vulnerability

**Issue:** [application.yaml](shopease-backend/src/main/resources/application.yaml)
```yaml
jwt:
  secret: "your-very-strong-secret-key-that-is-at-least-32-characters-long"
  # ❌ This is a placeholder, should be changed in production
```

**Solution:** Use a cryptographically strong random secret in production

---

### 16. Discount Products Logic Missing
**Severity:** MEDIUM  
**Impact:** Discounted products section shows nothing

**Issue:**
- Frontend calls `/api/products/discounted`
- Backend `ProductRepository.findDiscountedProducts()` returns products where `originalPrice > price`
- But no products have originalPrice set in sample data

**Backend Logic:** [ProductService.java](shopease-backend/src/main/java/com/shopease/service/ProductService.java#L81)
```java
public List<Product> getDiscountedProducts() {
    return productRepository.findDiscountedProducts();
}
```

---

## LOW PRIORITY ISSUES - Polish & Optimization

### 17. Order Items Creation Without Validation
**Severity:** LOW  
**Impact:** Invalid orders might be created

**Issue:** Frontend builds orderItems without server-side validation
```javascript
orderItems: cart.map((item) => {
  const product = PRODUCTS.find((p) => p.id == item.id);
  return {
    productId: product?.id,  // ❌ No validation
    quantity: item.quantity,
    unitPrice: product?.price || 0,
    totalPrice: (product?.price || 0) * item.quantity
  };
})
```

---

### 18. Cart Only Stored Locally
**Severity:** LOW  
**Impact:** Cart not synced across devices/browsers

**Problem:**
- Cart stored only in localStorage
- No server-side cart management
- Cannot retrieve cart from backend

---

### 19. Error Response Format Inconsistent
**Severity:** LOW  
**Impact:** Frontend error handling may fail

**Issue:**
- [GlobalExceptionHandler.java](shopease-backend/src/main/java/com/shopease/exception/GlobalExceptionHandler.java) returns `ErrorResponse` objects
- Frontend expects errors to have `message` or `errors` array
- Format might not match expected structure

---

### 20. No Input Validation on DTOs
**Severity:** LOW  
**Impact:** Invalid data might be accepted

**Issue:**
- No @Valid annotations on controller methods
- No @NotNull, @NotBlank validators on entity fields
- No price validation (negative prices allowed)

---

## INTEGRATION TEST CHECKLIST

### Before Testing:
- [ ] Fix API routing (api/v1 vs /api)
- [ ] Create database and schema
- [ ] Load sample data
- [ ] Fix authentication endpoints
- [ ] Fix search endpoint

### Test Flows:
1. **Product Browsing**
   - [ ] Load homepage - see featured products
   - [ ] Browse products page
   - [ ] Filter by category (backend should support)
   - [ ] Search products
   - [ ] View product details
   - [ ] Add to cart

2. **Cart Operations**
   - [ ] Update cart quantities
   - [ ] Remove items
   - [ ] Clear cart
   - [ ] Cart shows correct totals

3. **User Authentication**
   - [ ] Register new account
   - [ ] Login with credentials
   - [ ] Verify session persists
   - [ ] Access protected features
   - [ ] Logout

4. **Order Placement**
   - [ ] Checkout form validates
   - [ ] Order created in database
   - [ ] Order appears in order history
   - [ ] Stock decrements

5. **Order History**
   - [ ] Fetch customer orders from backend
   - [ ] Display order details
   - [ ] Show order items and totals

---

## SUMMARY TABLE

| Priority | Issue | Category | Files Affected | Fix Complexity |
|----------|-------|----------|-----------------|-----------------|
| CRITICAL | API routing mismatch | Backend/Frontend | app.js, all controllers | Easy (1 line) |
| CRITICAL | No database schema | Database | N/A | Medium |
| CRITICAL | Search endpoint mismatch | API | app.js, ProductController | Easy (1 line) |
| CRITICAL | Auth system conflict | Authentication | app.js, AuthController | Hard |
| CRITICAL | Order items mapping | Backend | OrderService | Medium |
| HIGH | Missing auth endpoints | Backend | AuthController | Medium |
| HIGH | No user persistence | Backend | User entity, service | Hard |
| HIGH | CORS configuration | Backend | WebConfig | Easy |
| MEDIUM | Stock management | Backend | OrderService | Medium |
| MEDIUM | Product field mismatch | API | app.js, Product entity | Easy |
| MEDIUM | No discount logic | Backend | ProductService | Easy |
| MEDIUM | Sample data missing | Database | data.sql | Easy |
| LOW | Cart sync | Backend/Frontend | app.js, services | Hard |
| LOW | Error handling | API | GlobalExceptionHandler | Easy |

---

## RECOMMENDED FIX ORDER

**Phase 1 - Get System Running (Day 1):**
1. Fix API routing prefix (1 line change)
2. Create database and schema
3. Load sample data
4. Fix search endpoint (1 line change)

**Phase 2 - Complete Authentication (Day 2):**
5. Implement missing auth endpoints (/logout, /me, /csrf)
6. Fix login/register to use JSON instead of form-encoded
7. Create User entity and database persistence
8. Test login flow

**Phase 3 - Complete Order Flow (Day 2-3):**
9. Fix order creation with proper OrderItem mapping
10. Implement stock decrement on order
11. Fix CORS configuration
12. Test order creation and history

**Phase 4 - Polish (Day 3):**
13. Add product discounts logic
14. Implement category filtering backend
15. Add input validation
16. Test full e-commerce flow

---

## Database Schema Needed

```sql
-- Create database
CREATE DATABASE shopease_db;
USE shopease_db;

-- Categories table
CREATE TABLE categories (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(255) NOT NULL UNIQUE,
  description VARCHAR(500)
);

-- Products table
CREATE TABLE products (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(255) NOT NULL,
  description TEXT,
  tagline VARCHAR(255),
  price DECIMAL(10, 2) NOT NULL,
  original_price DECIMAL(10, 2),
  image VARCHAR(500),
  badge VARCHAR(100),
  specs JSON,
  reviews JSON,
  category_id BIGINT NOT NULL,
  stock INT NOT NULL DEFAULT 100,
  FOREIGN KEY (category_id) REFERENCES categories(id)
);

-- Orders table
CREATE TABLE orders (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  customer_email VARCHAR(255) NOT NULL,
  status VARCHAR(20) NOT NULL DEFAULT 'PENDING',
  total_amount DECIMAL(10, 2) NOT NULL,
  shipping_fee DECIMAL(10, 2),
  delivery_address TEXT,
  created_at DATETIME NOT NULL,
  updated_at DATETIME NOT NULL
);

-- OrderItems table
CREATE TABLE order_items (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  order_id BIGINT NOT NULL,
  product_id BIGINT NOT NULL,
  quantity INT NOT NULL,
  unit_price DECIMAL(10, 2) NOT NULL,
  total_price DECIMAL(10, 2) NOT NULL,
  FOREIGN KEY (order_id) REFERENCES orders(id),
  FOREIGN KEY (product_id) REFERENCES products(id)
);

-- Indexes
CREATE INDEX idx_category_id ON products(category_id);
CREATE INDEX idx_customer_email ON orders(customer_email);
CREATE INDEX idx_order_status ON orders(status);
CREATE INDEX idx_order_created_at ON orders(created_at);
```

---

## Conclusion

The ShopEase e-commerce system has **5 CRITICAL issues** that completely block functionality. Once these are fixed, **6 HIGH-priority issues** need addressing to complete the order flow. The system is well-structured but requires comprehensive integration work before it can function as a complete e-commerce platform.

**Estimated Total Fix Time:** 12-16 hours for a full working system.
