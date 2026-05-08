# ShopEase Frontend

## Project Overview

ShopEase Frontend is a modern e-commerce interface built with vanilla HTML, CSS, and JavaScript. It consumes the ShopEase Backend REST API using the Fetch API for asynchronous data fetching, providing users with a seamless shopping experience with product browsing, filtering, cart management, and checkout functionality.

### Key Features

- **Fetch API Integration**: All product data loaded dynamically from backend
- **Error Handling**: Comprehensive try/catch blocks with specific error messages
- **Dynamic Rendering**: Products rendered from database, not hardcoded
- **Product Filtering**: Filter by category, price range, and search terms
- **Cart Management**: Add/remove items, update quantities with localStorage
- **Order Processing**: Complete checkout flow with order submission to backend
- **Spring Security Login**: Login/register page uses session cookies and CSRF-aware requests for protected API calls
- **Order History**: View past orders and latest order details
- **Responsive Design**: Mobile-friendly layout with CSS media queries (Lab 3)
- **Accessibility**: Semantic HTML with proper ARIA labels

## Project Structure

```
E-commerce Interface Lab/
├── TASK1.html               # Home page (featured & discounted products)
├── TASK2.html               # Products listing with filters
├── TASK3.html               # Product details page
├── TASK4.html               # Shopping cart
├── TASK5.html               # Checkout form
├── TASK6.html               # Orders history
├── TASK7.html               # Login page for Lab 9 Spring Security
├── detailbracelet.html      # (Legacy) Bracelet details
├── detailnecklace.html      # (Legacy) Necklace details
├── app.js                   # Main JavaScript with Fetch API integration
├── stylesheet.css           # Main stylesheet
└── stylesheet_old.css       # Legacy styles
```

## Pages Overview

### TASK1.html - Home Page
- **Featured Products**: Displays all available products
- **Discounted Products**: Shows products with discounts (originalPrice > price)
- **Call-to-Action**: Button to browse all products

### TASK2.html - Product Listing
- **Category Filter**: Filter products by category
- **Price Range Filter**: Under 7000 PHP or over 7000 PHP
- **Search**: Real-time search by product name or description
- **Dynamic Grid**: Products rendered from Fetch API response

### TASK3.html - Product Details
- **Product Information**: Full details including specs and reviews
- **Add to Cart**: Select quantity and add to cart
- **Image Gallery**: Product image or placeholder
- **Customer Reviews**: Display existing reviews (from JSON field)

### TASK4.html - Shopping Cart
- **Cart Items**: List of products with quantities
- **Modify Quantities**: Update item quantities inline
- **Remove Items**: Remove products from cart
- **Clear Cart**: Button to empty entire cart
- **Cart Summary**: Subtotal and item count

### TASK5.html - Checkout
- **Order Summary**: Itemized list and total
- **Customer Form**: Collect customer information
- **Address Details**: Street, city, zip code
- **Payment Method**: Select payment option
- **Submit Order**: Send to backend via Fetch API

### TASK6.html - Orders
- **Latest Order**: Most recent order highlighted
- **Order History**: All past orders with details
- **Order Details**: Items, totals, and customer info
- **Empty State**: Message when no orders exist

## Fetch API Integration

### API Configuration

```javascript
const API_BASE_URL = "http://localhost:8080/api";
```

### Core Functions

#### `fetchApi(endpoint, options)`
Utility function wrapping Fetch API with:
- Error handling with `try/catch`
- Manual `response.ok` checking
- Specific error logging for different status codes (404, 400, 500)
- Consistent header configuration

**Error Handling Logic:**
```javascript
if (!response.ok) {
  // Parse error response
  // Check status codes: 404 (not found), 400 (bad request), 500 (server error)
  // Throw custom error with specific message
}
```

#### `fetchProducts()`
- Fetches all products from `/api/products`
- Caches results in global `PRODUCTS` array
- Handles fetch failures gracefully with empty array fallback

#### `fetchProductById(id)`
- Fetches single product by ID
- Returns `null` on failure

#### `fetchDiscountedProducts()`
- Fetches products with discounts from backend
- Used on home page

#### `searchProducts(searchTerm)`
- Searches products by name on backend
- Returns matching products

#### `createOrder(orderData)`
- POST request to `/api/orders`
- Submits new order with all items
- Returns created order from backend

## Key Implementation Details

### 1. Page Initialization Flow

```javascript
document.addEventListener("DOMContentLoaded", async () => {
  // Load products for pages that need them
  if (page === "home" || page === "products" || page === "details") {
    await fetchProducts();  // async/await pattern
  }
  
  // Route to appropriate page initialization
  switch(page) { ... }
});
```

### 2. Dynamic Product Rendering

**Before (hardcoded):**
```javascript
const PRODUCTS = [{ id: "necklace", name: "Gold Necklace", ... }];
```

**After (Fetch API):**
```javascript
let PRODUCTS = [];  // Global cache

async function fetchProducts() {
  try {
    PRODUCTS = await fetchApi("/products");
    return PRODUCTS;
  } catch (error) {
    console.error("Failed to fetch products:", error);
    return [];  // Graceful fallback
  }
}
```

### 3. Error Handling Example

```javascript
async function fetchApi(endpoint, options = {}) {
  try {
    const response = await fetch(url, { ... });
    
    if (!response.ok) {
      const errorData = await response.json().catch(() => ({}));
      const errorMessage = errorData.message || `HTTP ${response.status}`;
      
      if (response.status === 404) {
        throw new Error(`Not found: ${errorMessage}`);
      } else if (response.status === 400) {
        throw new Error(`Bad request: ${errorMessage}`);
      }
      throw new Error(errorMessage);
    }
    
    return await response.json();
  } catch (error) {
    console.error("Fetch error:", error);
    throw error;  // Re-throw for caller to handle
  }
}
```

### 4. Order Submission

```javascript
async function initCheckoutPage() {
  form.addEventListener("submit", async (event) => {
    event.preventDefault();
    
    try {
      const order = {
        customerEmail: form.email.value,
        deliveryAddress: `${form.street.value}, ${form.city.value}`,
        totalAmount: subtotal + SHIPPING_FEE,
        shippingFee: SHIPPING_FEE,
        status: "PENDING",
        orderItems: cart.map(item => ({
          productId: item.id,
          quantity: item.quantity,
          unitPrice: product.price,
          totalPrice: product.price * item.quantity
        }))
      };
      
      const createdOrder = await createOrder(order);  // POST to /api/orders
      // Handle success...
    } catch (error) {
      // Handle error...
    }
  });
}
```

### 5. LocalStorage Fallback

Cart and orders are still saved to localStorage:
- **Cart persists** across page refreshes
- **Orders stored locally** as backup
- Synchronizes with backend on checkout

```javascript
function getCart() {
  return readStorageArray(STORAGE_KEYS.cart);
}

function saveCart(cart) {
  localStorage.setItem(STORAGE_KEYS.cart, JSON.stringify(cart));
}
```

## Setup & Configuration

## Lab 9 Spring Security

Lab 9 secures the e-commerce flow with session-based authentication, role-based authorization, CSRF-aware requests, and validation error handling.

### Security Architecture

- The browser signs in from `TASK7.html` and the backend sets a `JSESSIONID` cookie.
- Later `fetch` requests use `credentials: "include"` so the browser sends the session cookie automatically.
- State-changing requests include an `X-CSRF-TOKEN` header when the backend exposes `/api/v1/auth/csrf`.
- Protected checkout/order creation requires an authenticated session.
- Product creation is treated as an admin-only protected action in the demo.
- `app.js` handles `401 Unauthorized` by clearing the local session marker and asking the user to sign in.
- `app.js` handles `403 Forbidden` with an access denied message for authenticated users without the required role.

### Demo Accounts

| Username | Password | Role |
|----------|----------|------|
| `customer` | `customer123` | CUSTOMER |
| `admin` | `admin123` | ADMIN |

### Validation Rules

- Register: username must be 8 to 20 characters, password must be at least 8 characters, and role must be `CUSTOMER` or `ADMIN`.
- Product: name and description must not be blank, price must be positive, and stock quantity must be positive.
- Order: customer email must be valid, delivery address must not be blank, total amount must be positive, and at least one order item is required.
- Validation failures are displayed as user-friendly field messages returned by the API.

### API Reference

| Method | Endpoint | Access |
|--------|----------|--------|
| GET | `/api/v1/products` | Public |
| GET | `/api/v1/products/discounted` | Public |
| GET | `/api/v1/auth/csrf` | Public |
| POST | `/api/v1/auth/register` | Public |
| POST | `/api/v1/auth/login` | Public with CSRF |
| POST | `/api/v1/auth/logout` | Authenticated with CSRF |
| GET | `/api/v1/auth/me` | Public session check |
| POST | `/api/v1/orders` | Authenticated customer/admin with CSRF |
| POST | `/api/v1/products` | Admin only with CSRF |

### Testing Flow

1. Open `TASK7.html`, register a user or use the demo accounts.
2. Sign in as `customer` and verify that checkout can create an order.
3. Use `Try Without Session` and verify the protected product action fails with `401`.
4. Sign in as `customer` and try `Create Protected Product`; it should fail with `403`.
5. Sign in as `admin` and create a protected product; it should succeed.
6. While signed in as `admin`, click `Send Invalid Price`; the validation error should list the negative price rule.

### Prerequisites

- Modern web browser (Chrome, Firefox, Safari, Edge)
- ShopEase Backend running on `localhost:8080`
- Live Server or similar local web server

### Configuration

Update the API base URL in `app.js` if backend runs on different port:

```javascript
const API_BASE_URL = "http://localhost:8080/api";  // Change as needed
```

### Running Locally

**Option 1: Using VS Code Live Server**
1. Install "Live Server" extension
2. Right-click `TASK1.html` and select "Open with Live Server"
3. Browser opens on `http://localhost:5500`

**Option 2: Using Python HTTP Server**
```bash
python -m http.server 5500
# Visit http://localhost:5500
```

**Option 3: Using Node.js HTTP Server**
```bash
npx http-server -p 5500
```

### Verify CORS Configuration

1. Open browser Developer Tools (F12)
2. Go to Console tab
3. Check for CORS errors after fetching products
4. Products should load without "Access-Control-Allow-Origin" errors

## Testing Checklist

### Frontend Setup
- [ ] Backend running on `localhost:8080`
- [ ] Frontend running on `localhost:5500`
- [ ] Browser console shows no CORS errors
- [ ] Network tab shows successful API calls

### Product Loading
- [ ] Home page loads featured products from API
- [ ] Products page loads all products
- [ ] Search functionality works
- [ ] Category filters work
- [ ] Price range filters work

### Shopping Flow
- [ ] Can add products to cart
- [ ] Cart persists on page refresh
- [ ] Can modify quantities in cart
- [ ] Can remove items from cart
- [ ] Can clear entire cart

### Checkout
- [ ] Checkout form displays order summary
- [ ] Can submit checkout form
- [ ] Order appears in database (check backend)
- [ ] Redirects to orders page after successful checkout
- [ ] Order appears in orders history

### Error Scenarios
- [ ] Inspect console for error logs
- [ ] Network errors display user-friendly messages
- [ ] 404 errors handled gracefully
- [ ] Empty product lists show appropriate message

## Console Logging

The frontend logs various operations for debugging:

```javascript
console.log("Fetching products from backend...");
console.log(`Successfully fetched ${products.length} products`);
console.log("Creating order on backend...");
console.error("Failed to fetch products:", error);
```

**View logs in browser Developer Tools → Console tab**

## Responsive Design

Mobile-friendly breakpoints maintained from Lab 3:

```css
/* Tablet and below */
@media (max-width: 768px) {
  .products-grid {
    grid-template-columns: repeat(2, 1fr);
  }
}

/* Mobile */
@media (max-width: 480px) {
  .products-grid {
    grid-template-columns: 1fr;
  }
}
```

## Browser Support

- Chrome/Edge 90+
- Firefox 88+
- Safari 14+
- Mobile browsers (iOS Safari, Chrome Mobile)

## Accessibility Features

- Semantic HTML (`<main>`, `<nav>`, `<section>`)
- ARIA labels for navigation
- Form labels properly associated with inputs
- Image alt text for all product images
- Keyboard navigation support
- Proper heading hierarchy

## Future Enhancements

- [ ] Add product image gallery
- [ ] Implement wish list feature
- [ ] Add user authentication
- [ ] Implement product reviews submission
- [ ] Add pagination for product lists
- [ ] Implement advanced filters (rating, availability)
- [ ] Add order tracking page
- [ ] Implement saved addresses
- [ ] Add coupon/discount code support

## Troubleshooting

### Products Not Loading

**Check:**
1. Backend running on `localhost:8080`
2. Network tab shows API calls
3. Console shows specific error message
4. Check CORS configuration on backend

**Solution:**
```javascript
// Add this to check connection
fetchApi("/products")
  .then(products => console.log("Connected:", products))
  .catch(error => console.error("Connection failed:", error));
```

### CORS Errors

**Error:** "Access to XMLHttpRequest blocked by CORS policy"

**Solution:** Ensure `WebConfig.java` on backend has correct origins:
```java
.allowedOrigins("http://localhost:5500", "http://127.0.0.1:5500")
```

### Cart Items Disappearing

**Check:** Browser allows localStorage
**Solution:** Check browser Storage settings and clear cache if needed

### Orders Not Submitting

**Check:**
1. All form fields filled
2. Backend running
3. Network tab shows POST request
4. Check console for error messages

## Version History

- **1.0.0** (Current) - Fetch API integration with backend database

## License

This project is part of Laboratory 8: Database Integration and Consuming RESTful Web Services with Fetch API.
