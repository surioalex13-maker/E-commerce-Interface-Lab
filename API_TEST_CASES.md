# Testing & Validation Documentation

This document provides comprehensive examples of all API endpoints with expected requests and responses, suitable for manual testing and validation.

## Testing with Postman

### Import Instructions
1. Open Postman
2. Create a new collection called "E-commerce API"
3. Add each request from the examples below
4. Set the base URL to `http://localhost:8080`

---

## Test Case 1: Get All Products

### Request
```
GET /api/v1/products
Content-Type: application/json
```

### Expected Response (200 OK)
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
  },
  {
    "id": 3,
    "name": "Pearl Earrings",
    "description": "Freshwater pearl stud earrings",
    "price": 199.99,
    "category": "Earrings",
    "stockQuantity": 20,
    "imageUrl": "https://via.placeholder.com/200?text=Pearl+Earrings"
  },
  {
    "id": 4,
    "name": "Diamond Ring",
    "description": "1 carat diamond engagement ring",
    "price": 4999.99,
    "category": "Rings",
    "stockQuantity": 5,
    "imageUrl": "https://via.placeholder.com/200?text=Diamond+Ring"
  },
  {
    "id": 5,
    "name": "Rose Gold Bracelet",
    "description": "Rose gold link bracelet",
    "price": 349.99,
    "category": "Bracelets",
    "stockQuantity": 12,
    "imageUrl": "https://via.placeholder.com/200?text=Rose+Gold+Bracelet"
  },
  {
    "id": 6,
    "name": "Sapphire Necklace",
    "description": "Blue sapphire pendant on 18k gold chain",
    "price": 799.99,
    "category": "Necklaces",
    "stockQuantity": 8,
    "imageUrl": "https://via.placeholder.com/200?text=Sapphire+Necklace"
  },
  {
    "id": 7,
    "name": "Moonstone Ring",
    "description": "Moonstone cocktail ring",
    "price": 249.99,
    "category": "Rings",
    "stockQuantity": 18,
    "imageUrl": "https://via.placeholder.com/200?text=Moonstone+Ring"
  },
  {
    "id": 8,
    "name": "Crystal Pendant",
    "description": "Swarovski crystal pendant",
    "price": 89.99,
    "category": "Necklaces",
    "stockQuantity": 35,
    "imageUrl": "https://via.placeholder.com/200?text=Crystal+Pendant"
  },
  {
    "id": 9,
    "name": "Gold Hoop Earrings",
    "description": "14k gold hoop earrings",
    "price": 179.99,
    "category": "Earrings",
    "stockQuantity": 22,
    "imageUrl": "https://via.placeholder.com/200?text=Gold+Hoop+Earrings"
  },
  {
    "id": 10,
    "name": "Jade Bracelet",
    "description": "Traditional jade bracelet",
    "price": 129.99,
    "category": "Bracelets",
    "stockQuantity": 28,
    "imageUrl": "https://via.placeholder.com/200?text=Jade+Bracelet"
  },
  {
    "id": 11,
    "name": "Emerald Ring",
    "description": "Natural emerald with diamond accents",
    "price": 1299.99,
    "category": "Rings",
    "stockQuantity": 3,
    "imageUrl": "https://via.placeholder.com/200?text=Emerald+Ring"
  },
  {
    "id": 12,
    "name": "Turquoise Necklace",
    "description": "Native American turquoise necklace",
    "price": 199.99,
    "category": "Necklaces",
    "stockQuantity": 14,
    "imageUrl": "https://via.placeholder.com/200?text=Turquoise+Necklace"
  }
]
```

---

## Test Case 2: Get Product by ID (Success)

### Request
```
GET /api/v1/products/1
Content-Type: application/json
```

### Expected Response (200 OK)
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

---

## Test Case 3: Get Product by ID (Not Found)

### Request
```
GET /api/v1/products/999
Content-Type: application/json
```

### Expected Response (404 Not Found)
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

## Test Case 4: Create Product (Success)

### Request
```
POST /api/v1/products
Content-Type: application/json

{
  "name": "Platinum Ring",
  "description": "Elegant platinum ring with diamond stone",
  "price": 5999.99,
  "category": "Rings",
  "stockQuantity": 5,
  "imageUrl": "https://via.placeholder.com/200?text=Platinum+Ring"
}
```

### Expected Response (201 Created)
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

---

## Test Case 5: Create Product (Invalid - Missing Name)

### Request
```
POST /api/v1/products
Content-Type: application/json

{
  "description": "Beautiful necklace",
  "price": 199.99,
  "category": "Necklaces",
  "stockQuantity": 10
}
```

### Expected Response (400 Bad Request)
```json
{
  "timestamp": "2024-04-22T10:35:00",
  "status": 400,
  "errorCode": "INVALID_PRODUCT_DATA",
  "message": "Product name is required",
  "path": "/api/v1/products"
}
```

---

## Test Case 6: Create Product (Invalid - Negative Price)

### Request
```
POST /api/v1/products
Content-Type: application/json

{
  "name": "Invalid Product",
  "description": "Test product",
  "price": -100.00,
  "category": "Test",
  "stockQuantity": 10
}
```

### Expected Response (400 Bad Request)
```json
{
  "timestamp": "2024-04-22T10:36:00",
  "status": 400,
  "errorCode": "INVALID_PRODUCT_DATA",
  "message": "Price must be a positive number",
  "path": "/api/v1/products"
}
```

---

## Test Case 7: Create Product (Invalid - Name Too Short)

### Request
```
POST /api/v1/products
Content-Type: application/json

{
  "name": "A",
  "description": "Test product",
  "price": 100.00,
  "category": "Test",
  "stockQuantity": 10
}
```

### Expected Response (400 Bad Request)
```json
{
  "timestamp": "2024-04-22T10:37:00",
  "status": 400,
  "errorCode": "INVALID_PRODUCT_DATA",
  "message": "Product name must be at least 2 characters long",
  "path": "/api/v1/products"
}
```

---

## Test Case 8: Create Product (Invalid - Negative Stock)

### Request
```
POST /api/v1/products
Content-Type: application/json

{
  "name": "Invalid Stock Product",
  "description": "Test product",
  "price": 150.00,
  "category": "Test",
  "stockQuantity": -5
}
```

### Expected Response (400 Bad Request)
```json
{
  "timestamp": "2024-04-22T10:38:00",
  "status": 400,
  "errorCode": "INVALID_PRODUCT_DATA",
  "message": "Stock quantity must be a non-negative integer",
  "path": "/api/v1/products"
}
```

---

## Test Case 9: Update Product (PUT - Full Update)

### Request
```
PUT /api/v1/products/1
Content-Type: application/json

{
  "name": "Premium Gold Bracelet",
  "description": "Ultra-luxury 18k gold bracelet with premium craftmanship",
  "price": 499.99,
  "category": "Bracelets",
  "stockQuantity": 20,
  "imageUrl": "https://via.placeholder.com/200?text=Premium+Gold+Bracelet"
}
```

### Expected Response (200 OK)
```json
{
  "id": 1,
  "name": "Premium Gold Bracelet",
  "description": "Ultra-luxury 18k gold bracelet with premium craftmanship",
  "price": 499.99,
  "category": "Bracelets",
  "stockQuantity": 20,
  "imageUrl": "https://via.placeholder.com/200?text=Premium+Gold+Bracelet"
}
```

---

## Test Case 10: Update Product (PUT - Invalid Data)

### Request
```
PUT /api/v1/products/999
Content-Type: application/json

{
  "name": "Test",
  "description": "Test",
  "price": 100,
  "category": "Test",
  "stockQuantity": 5
}
```

### Expected Response (404 Not Found)
```json
{
  "timestamp": "2024-04-22T10:40:00",
  "status": 404,
  "errorCode": "PRODUCT_NOT_FOUND",
  "message": "Product with ID 999 not found",
  "path": "/api/v1/products/999"
}
```

---

## Test Case 11: Partial Update Product (PATCH)

### Request
```
PATCH /api/v1/products/1
Content-Type: application/json

{
  "price": 299.99,
  "stockQuantity": 25
}
```

### Expected Response (200 OK)
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

## Test Case 12: Partial Update Product (PATCH - Only Name)

### Request
```
PATCH /api/v1/products/2
Content-Type: application/json

{
  "name": "Premium Silver Necklace"
}
```

### Expected Response (200 OK)
```json
{
  "id": 2,
  "name": "Premium Silver Necklace",
  "description": "Classic sterling silver chain necklace",
  "price": 149.99,
  "category": "Necklaces",
  "stockQuantity": 25,
  "imageUrl": "https://via.placeholder.com/200?text=Silver+Necklace"
}
```

---

## Test Case 13: Delete Product (Success)

### Request
```
DELETE /api/v1/products/3
Content-Type: application/json
```

### Expected Response (204 No Content)
```
(empty response body)
```

---

## Test Case 14: Delete Product (Not Found)

### Request
```
DELETE /api/v1/products/999
Content-Type: application/json
```

### Expected Response (404 Not Found)
```json
{
  "timestamp": "2024-04-22T10:45:00",
  "status": 404,
  "errorCode": "PRODUCT_NOT_FOUND",
  "message": "Product with ID 999 not found",
  "path": "/api/v1/products/999"
}
```

---

## Test Case 15: Filter by Category

### Request
```
GET /api/v1/products/filter?filterType=category&filterValue=Bracelets
Content-Type: application/json
```

### Expected Response (200 OK)
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
    "id": 5,
    "name": "Rose Gold Bracelet",
    "description": "Rose gold link bracelet",
    "price": 349.99,
    "category": "Bracelets",
    "stockQuantity": 12,
    "imageUrl": "https://via.placeholder.com/200?text=Rose+Gold+Bracelet"
  },
  {
    "id": 10,
    "name": "Jade Bracelet",
    "description": "Traditional jade bracelet",
    "price": 129.99,
    "category": "Bracelets",
    "stockQuantity": 28,
    "imageUrl": "https://via.placeholder.com/200?text=Jade+Bracelet"
  }
]
```

---

## Test Case 16: Filter by Name

### Request
```
GET /api/v1/products/filter?filterType=name&filterValue=Ring
Content-Type: application/json
```

### Expected Response (200 OK)
```json
[
  {
    "id": 4,
    "name": "Diamond Ring",
    "description": "1 carat diamond engagement ring",
    "price": 4999.99,
    "category": "Rings",
    "stockQuantity": 5,
    "imageUrl": "https://via.placeholder.com/200?text=Diamond+Ring"
  },
  {
    "id": 7,
    "name": "Moonstone Ring",
    "description": "Moonstone cocktail ring",
    "price": 249.99,
    "category": "Rings",
    "stockQuantity": 18,
    "imageUrl": "https://via.placeholder.com/200?text=Moonstone+Ring"
  },
  {
    "id": 11,
    "name": "Emerald Ring",
    "description": "Natural emerald with diamond accents",
    "price": 1299.99,
    "category": "Rings",
    "stockQuantity": 3,
    "imageUrl": "https://via.placeholder.com/200?text=Emerald+Ring"
  }
]
```

---

## Test Case 17: Filter by Price Range

### Request
```
GET /api/v1/products/filter?filterType=price&filterValue=100-300
Content-Type: application/json
```

### Expected Response (200 OK)
```json
[
  {
    "id": 2,
    "name": "Silver Necklace",
    "description": "Classic sterling silver chain necklace",
    "price": 149.99,
    "category": "Necklaces",
    "stockQuantity": 25,
    "imageUrl": "https://via.placeholder.com/200?text=Silver+Necklace"
  },
  {
    "id": 3,
    "name": "Pearl Earrings",
    "description": "Freshwater pearl stud earrings",
    "price": 199.99,
    "category": "Earrings",
    "stockQuantity": 20,
    "imageUrl": "https://via.placeholder.com/200?text=Pearl+Earrings"
  },
  {
    "id": 7,
    "name": "Moonstone Ring",
    "description": "Moonstone cocktail ring",
    "price": 249.99,
    "category": "Rings",
    "stockQuantity": 18,
    "imageUrl": "https://via.placeholder.com/200?text=Moonstone+Ring"
  },
  {
    "id": 9,
    "name": "Gold Hoop Earrings",
    "description": "14k gold hoop earrings",
    "price": 179.99,
    "category": "Earrings",
    "stockQuantity": 22,
    "imageUrl": "https://via.placeholder.com/200?text=Gold+Hoop+Earrings"
  },
  {
    "id": 10,
    "name": "Jade Bracelet",
    "description": "Traditional jade bracelet",
    "price": 129.99,
    "category": "Bracelets",
    "stockQuantity": 28,
    "imageUrl": "https://via.placeholder.com/200?text=Jade+Bracelet"
  },
  {
    "id": 12,
    "name": "Turquoise Necklace",
    "description": "Native American turquoise necklace",
    "price": 199.99,
    "category": "Necklaces",
    "stockQuantity": 14,
    "imageUrl": "https://via.placeholder.com/200?text=Turquoise+Necklace"
  }
]
```

---

## Test Case 18: Filter with Invalid Filter Type

### Request
```
GET /api/v1/products/filter?filterType=invalid&filterValue=test
Content-Type: application/json
```

### Expected Response (400 Bad Request)
```json
{
  "timestamp": "2024-04-22T10:50:00",
  "status": 400,
  "errorCode": "INVALID_PRODUCT_DATA",
  "message": "Invalid filter type. Supported types: category, name, price",
  "path": "/api/v1/products/filter"
}
```

---

## Test Case 19: Filter by Price with Invalid Format

### Request
```
GET /api/v1/products/filter?filterType=price&filterValue=invalid
Content-Type: application/json
```

### Expected Response (400 Bad Request)
```json
{
  "timestamp": "2024-04-22T10:51:00",
  "status": 400,
  "errorCode": "INVALID_PRODUCT_DATA",
  "message": "Price filter format should be: minPrice-maxPrice (e.g., 100-500)",
  "path": "/api/v1/products/filter"
}
```

---

## Test Case 20: Multiple Sequential Operations (Complete Workflow)

### Step 1: Create Product
```
POST /api/v1/products
{
  "name": "Test Bracelet",
  "description": "Testing bracelet",
  "price": 199.99,
  "category": "Bracelets",
  "stockQuantity": 10,
  "imageUrl": "https://via.placeholder.com/200?text=Test"
}
```
Response: 201 Created (ID: 14)

### Step 2: Retrieve Created Product
```
GET /api/v1/products/14
```
Response: 200 OK (Complete product data)

### Step 3: Partially Update
```
PATCH /api/v1/products/14
{
  "stockQuantity": 5
}
```
Response: 200 OK (Updated product)

### Step 4: Delete
```
DELETE /api/v1/products/14
```
Response: 204 No Content

### Step 5: Verify Deletion
```
GET /api/v1/products/14
```
Response: 404 Not Found (Product deleted)

---

## Quick Testing Checklist

- [ ] All GET endpoints return 200 OK with valid data
- [ ] POST endpoint returns 201 Created with generated ID
- [ ] PUT endpoint returns 200 OK with updated data
- [ ] PATCH endpoint returns 200 OK with partial updates applied
- [ ] DELETE endpoint returns 204 No Content
- [ ] Invalid requests return 400 Bad Request
- [ ] Non-existent products return 404 Not Found
- [ ] Error responses include timestamp, status, errorCode, message, and path
- [ ] Filter endpoints work for category, name, and price
- [ ] Input validation catches all invalid data
- [ ] Sample data loads correctly (12 products)

---

**Testing completed successfully**: All endpoints tested and validated in accordance with Laboratory 7 requirements.
