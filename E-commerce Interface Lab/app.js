/**
 * ShopEase E-commerce Frontend
 * Uses Fetch API to communicate with the backend REST API
 * Base API URL points to the Spring Boot backend running on localhost:8080
 */

const API_BASE_URL = "http://localhost:8080/api/v1";
const STORAGE_KEYS = {
  cart: "shopease-cart",
  orders: "shopease-orders",
  sessionUser: "shopease-session-user",
  csrfToken: "shopease-csrf-token"
};
const SHIPPING_FEE = 99;

// Global cache for products to minimize API calls
let PRODUCTS = [];
const FALLBACK_PRODUCTS = normalizeProducts([
  {
    id: 1,
    name: "Gold Bracelet",
    description: "Elegant 14k gold bracelet with intricate design",
    price: 299.99,
    category: "Bracelets",
    stockQuantity: 15,
    imageUrl: "https://via.placeholder.com/200?text=Gold+Bracelet"
  },
  {
    id: 2,
    name: "Silver Necklace",
    description: "Classic sterling silver chain necklace",
    price: 149.99,
    category: "Necklaces",
    stockQuantity: 25,
    imageUrl: "https://via.placeholder.com/200?text=Silver+Necklace"
  },
  {
    id: 3,
    name: "Pearl Earrings",
    description: "Freshwater pearl stud earrings",
    price: 199.99,
    category: "Earrings",
    stockQuantity: 20,
    imageUrl: "https://via.placeholder.com/200?text=Pearl+Earrings"
  },
  {
    id: 4,
    name: "Diamond Ring",
    description: "1 carat diamond engagement ring",
    price: 4999.99,
    category: "Rings",
    stockQuantity: 5,
    imageUrl: "https://via.placeholder.com/200?text=Diamond+Ring"
  }
]);

/**
 * Utility function to make API calls using Fetch API
 * Implements error handling with try/catch and response.ok checking
 * 
 * @param {string} endpoint - The API endpoint (e.g., '/products')
 * @param {object} options - Fetch options (method, body, headers, etc.)
 * @returns {Promise<object>} - The parsed JSON response
 * @throws {Error} - Throws custom error if the request fails
 */
async function fetchApi(endpoint, options = {}) {
  try {
    const url = `${API_BASE_URL}${endpoint}`;
    const method = (options.method || "GET").toUpperCase();
    const csrfHeaders = method === "GET" || method === "HEAD" || method === "OPTIONS"
      ? {}
      : await getCsrfHeaders();
    const response = await fetch(url, {
      credentials: "include",
      headers: {
        "Content-Type": "application/json",
        ...csrfHeaders,
        ...options.headers
      },
      ...options
    });

    // Check response.ok manually to handle non-2xx status codes
    if (!response.ok) {
      const errorData = await response.json().catch(() => ({}));
      const validationErrors = Array.isArray(errorData.errors) ? errorData.errors.join("; ") : "";
      const errorMessage = validationErrors || errorData.message || `HTTP ${response.status}: ${response.statusText}`;
      
      if (response.status === 404) {
        console.error("Resource not found:", errorMessage);
        throw new Error(`Not found: ${errorMessage}`);
      } else if (response.status === 400) {
        console.error("Bad request:", errorMessage);
        throw new Error(`Bad request: ${errorMessage}`);
      } else if (response.status === 500) {
        console.error("Server error:", errorMessage);
        throw new Error("Server error. Please try again later.");
      } else if (response.status === 401) {
        console.error("Authentication required:", errorMessage);
        localStorage.removeItem(STORAGE_KEYS.sessionUser);
        throw new Error("Authentication required. Please log in first.");
      } else if (response.status === 403) {
        console.error("Access denied:", errorMessage);
        throw new Error("Your account does not have permission for this action.");
      }
      
      throw new Error(errorMessage);
    }

    return await response.json();
  } catch (error) {
    console.error("Fetch API error:", error);
    throw error;
  }
}

async function fetchCsrfToken() {
  try {
    const response = await fetch(`${API_BASE_URL}/auth/csrf`, {
      credentials: "include",
      headers: { Accept: "application/json" }
    });
    const data = await response.json().catch(() => ({}));
    const token = data.token || data.csrfToken || data._csrf || "";
    if (response.ok && token) {
      localStorage.setItem(STORAGE_KEYS.csrfToken, token);
      return token;
    }
  } catch (error) {
    console.warn("CSRF token endpoint is unavailable. Continuing for compatible backends.", error);
  }
  return localStorage.getItem(STORAGE_KEYS.csrfToken) || "";
}

async function getCsrfHeaders() {
  const token = localStorage.getItem(STORAGE_KEYS.csrfToken) || await fetchCsrfToken();
  return token ? { "X-CSRF-TOKEN": token } : {};
}

/**
 * Fetch all products from the backend API
 * Caches the results in the PRODUCTS global variable
 * 
 * @returns {Promise<Array>} - Array of product objects from the database
 */
async function fetchProducts() {
  try {
    console.log("Fetching products from backend...");
    const products = normalizeProducts(await fetchApi("/products"));
    PRODUCTS = products;
    console.log(`Successfully fetched ${products.length} products`);
    return products;
  } catch (error) {
    console.error("Failed to fetch products:", error);
    PRODUCTS = FALLBACK_PRODUCTS;
    return PRODUCTS;
  }
}

/**
 * Fetch a single product by ID
 * 
 * @param {number} id - The product ID
 * @returns {Promise<object|null>} - The product object or null if not found
 */
async function fetchProductById(id) {
  try {
    return normalizeProduct(await fetchApi(`/products/${id}`));
  } catch (error) {
    console.error(`Failed to fetch product ${id}:`, error);
    return null;
  }
}

/**
 * Fetch products by category name
 * 
 * @param {string} categoryName - The category name
 * @returns {Promise<Array>} - Array of products in the category
 */
async function fetchProductsByCategory(categoryName) {
  try {
    return await fetchApi(`/products/category/${categoryName}`);
  } catch (error) {
    console.error(`Failed to fetch products for category ${categoryName}:`, error);
    return [];
  }
}

/**
 * Fetch discounted products from the backend
 * 
 * @returns {Promise<Array>} - Array of discounted products
 */
async function fetchDiscountedProducts() {
  try {
    return normalizeProducts(await fetchApi("/products/discounted"));
  } catch (error) {
    console.error("Failed to fetch discounted products:", error);
    return PRODUCTS.slice(0, 4);
  }
}

/**
 * Search products by name on the backend
 * 
 * @param {string} searchTerm - The search term
 * @returns {Promise<Array>} - Array of matching products
 */
async function searchProducts(searchTerm) {
  try {
    return normalizeProducts(await fetchApi(`/products/filter?filterType=name&filterValue=${encodeURIComponent(searchTerm)}`));
  } catch (error) {
    console.error(`Failed to search for products with term "${searchTerm}":`, error);
    return [];
  }
}

/**
 * Create an order on the backend
 * 
 * @param {object} orderData - The order data to create
 * @returns {Promise<object>} - The created order from the backend
 */
async function createOrder(orderData) {
  try {
    console.log("Creating order on backend...");
    const order = await fetchApi("/orders", {
      method: "POST",
      body: JSON.stringify(orderData)
    });
    console.log("Order created successfully:", order);
    return order;
  } catch (error) {
    console.error("Failed to create order:", error);
    throw error;
  }
}

async function registerAccount(username, password, role = "CUSTOMER") {
  return await fetchApi("/auth/register", {
    method: "POST",
    body: JSON.stringify({ username, password, role })
  });
}

async function loginAccount(username, password) {
  const csrfToken = await fetchCsrfToken();
  const response = await fetch(`${API_BASE_URL}/auth/login`, {
    method: "POST",
    credentials: "include",
    headers: {
      "Content-Type": "application/x-www-form-urlencoded",
      Accept: "application/json",
      ...(csrfToken ? { "X-CSRF-TOKEN": csrfToken } : {})
    },
    body: new URLSearchParams({ username, password, _csrf: csrfToken })
  });

  const data = await response.json().catch(() => ({}));
  if (!response.ok) {
    throw new Error(data.message || `HTTP ${response.status}: ${response.statusText}`);
  }

  localStorage.setItem(STORAGE_KEYS.sessionUser, data.username || username);
  await fetchCsrfToken();
  return data;
}

async function logoutAccount() {
  const csrfHeaders = await getCsrfHeaders();
  const response = await fetch(`${API_BASE_URL}/auth/logout`, {
    method: "POST",
    credentials: "include",
    headers: {
      Accept: "application/json",
      ...csrfHeaders
    }
  });

  localStorage.removeItem(STORAGE_KEYS.sessionUser);
  localStorage.removeItem(STORAGE_KEYS.csrfToken);
  return await response.json().catch(() => ({ message: "Logout complete" }));
}

async function fetchSession() {
  return await fetchApi("/auth/me");
}

async function createProduct(productData) {
  return await fetchApi("/products", {
    method: "POST",
    body: JSON.stringify(productData)
  });
}

/**
 * Fetch all orders for a customer
 * 
 * @param {string} customerEmail - The customer email
 * @returns {Promise<Array>} - Array of customer's orders
 */
async function fetchCustomerOrders(customerEmail) {
  try {
    return await fetchApi(`/orders/customer/${encodeURIComponent(customerEmail)}`);
  } catch (error) {
    console.error(`Failed to fetch orders for customer ${customerEmail}:`, error);
    return [];
  }
}

/**
 * Initialize page on DOM load
 * Routes to different page initialization based on data-page attribute
 */
document.addEventListener("DOMContentLoaded", async () => {
  const page = document.body.dataset.page;

  // Load products from backend for all pages that might need them
  if (["home", "products", "details", "cart", "checkout"].includes(page)) {
    await fetchProducts();
  }

  switch (page) {
    case "home":
      renderHome();
      break;
    case "products":
      initProductListing();
      break;
    case "details":
      initDetailsPage();
      break;
    case "cart":
      initCartPage();
      break;
    case "checkout":
      initCheckoutPage();
      break;
    case "orders":
      initOrdersPage();
      break;
    case "login":
      initLoginPage();
      break;
    default:
      break;
  }
});

/**
 * Render home page with featured products and discounted products
 * Displays featured products from the entire catalog
 * Also fetches and displays discounted products separately
 */
async function renderHome() {
  const featuredContainer = document.querySelector('[data-list="featured"]');
  const discountedContainer = document.querySelector('[data-list="discounted"]');

  try {
    // Render featured products (all products or first batch)
    renderProductCards(featuredContainer, PRODUCTS);

    // Fetch and render discounted products separately
    const discounted = await fetchDiscountedProducts();
    renderProductCards(discountedContainer, discounted, { showOriginalPrice: true });
  } catch (error) {
    console.error("Error rendering home page:", error);
    renderProductCards(featuredContainer, PRODUCTS);
    renderProductCards(discountedContainer, []);
  }
}

/**
 * Initialize product listing page with filtering and search
 * Supports filtering by category, price range, and search term
 * Updates catalog dynamically when filters change
 */
function initProductListing() {
  const form = document.getElementById("filter-form");
  const grid = document.getElementById("catalog-grid");
  const summary = document.getElementById("filter-summary");

  async function updateCatalog() {
    try {
      const selectedCategories = Array.from(
        form.querySelectorAll('input[name="category"]:checked'),
        (input) => input.value
      );
      const priceFilter = form.querySelector('input[name="price"]:checked')?.value || "all";
      const searchTerm = form.search.value.trim().toLowerCase();

      let filtered = PRODUCTS;

      // Apply category filter
      if (selectedCategories.length > 0) {
        filtered = filtered.filter((product) =>
          selectedCategories.includes(normalizeCategory(product.category))
        );
      }

      // Apply search filter
      if (searchTerm !== "") {
        filtered = filtered.filter((product) =>
          product.name.toLowerCase().includes(searchTerm) ||
          (product.description && product.description.toLowerCase().includes(searchTerm))
        );
      }

      // Apply price filter
      if (priceFilter !== "all") {
        filtered = filtered.filter((product) => {
          const price = parseFloat(product.price);
          return (priceFilter === "under-7000" && price <= 7000) ||
                 (priceFilter === "over-7000" && price > 7000);
        });
      }

      renderProductCards(grid, filtered, { showOriginalPrice: true });
      summary.textContent = `${filtered.length} product(s) shown.`;
    } catch (error) {
      console.error("Error updating catalog:", error);
      summary.textContent = "Error loading products. Please try again.";
    }
  }

  form.addEventListener("input", updateCatalog);
  form.addEventListener("change", updateCatalog);
  updateCatalog();
}

/**
 * Initialize product details page
 * Fetches product by ID from URL parameter
 * Displays full product information, specs, and reviews
 */
async function initDetailsPage() {
  const params = new URLSearchParams(window.location.search);
  const productId = params.get("item");
  
  // Try to find product in cached PRODUCTS first, then fetch if needed
  let product = PRODUCTS.find((p) => p.id == productId);
  
  if (!product && productId) {
    product = await fetchProductById(productId);
  }
  
  if (!product) {
    product = PRODUCTS[0] || null;
  }

  if (!product) {
    document.body.innerHTML = "<h1>Product not found</h1>";
    return;
  }

  const form = document.getElementById("purchase-form");
  const quantityInput = document.getElementById("quantity");
  const message = document.getElementById("purchase-message");

  document.title = `ShopEase - ${product.name}`;
  document.getElementById("detail-title").textContent = product.name;
  document.getElementById("detail-price").textContent = formatCurrency(product.price);
  document.getElementById("detail-description").textContent = product.description;
  document.getElementById("detail-tagline").textContent = product.tagline;

  const image = document.getElementById("detail-image");
  applyProductImage(image, product);

  // Render specs if available (handle JSON or array format)
  let specs = product.specs || [];
  if (typeof specs === "string") {
    try {
      specs = JSON.parse(specs);
    } catch (e) {
      specs = [];
    }
  }
  
  document.getElementById("spec-table").innerHTML = (Array.isArray(specs) ? specs : [])
    .map(
      ([label, value]) =>
        `<tr><th scope="row">${escapeHtml(label)}</th><td>${escapeHtml(value)}</td></tr>`
    )
    .join("");

  // Render reviews if available (handle JSON or array format)
  let reviews = product.reviews || [];
  if (typeof reviews === "string") {
    try {
      reviews = JSON.parse(reviews);
    } catch (e) {
      reviews = [];
    }
  }
  
  document.getElementById("review-list").innerHTML = (Array.isArray(reviews) ? reviews : [])
    .map(
      (review) => `
        <article>
          <h3>${escapeHtml(review.name || "Anonymous")}</h3>
          <blockquote>${escapeHtml(review.text || review.comment || "")}</blockquote>
        </article>
      `
    )
    .join("");

  form.addEventListener("submit", (event) => {
    event.preventDefault();
    const quantity = Math.max(1, Number(quantityInput.value) || 1);
    addToCart(product.id, quantity);
    message.textContent = `${product.name} added to cart (${quantity}).`;
  });
}

/**
 * Initialize cart page
 * Displays cart items with ability to modify quantities and remove items
 * Supports clearing entire cart
 */
function initCartPage() {
  const list = document.getElementById("cart-items");
  const status = document.getElementById("cart-status");
  const subtotal = document.getElementById("cart-subtotal");
  const count = document.getElementById("cart-count");
  const checkoutLink = document.getElementById("checkout-link");
  const clearButton = document.getElementById("clear-cart");

  function render() {
    const cart = getCart();
    const cartItems = cart
      .map((item) => {
        const product = PRODUCTS.find((p) => p.id == item.id);
        return product ? { ...item, product } : null;
      })
      .filter(Boolean);

    if (cartItems.length === 0) {
      status.textContent = "Your cart is currently empty.";
      list.innerHTML = '<li class="cart-empty">Add products from the catalog to begin checkout.</li>';
      subtotal.textContent = formatCurrency(0);
      count.textContent = "0";
      checkoutLink.setAttribute("aria-disabled", "true");
      checkoutLink.style.pointerEvents = "none";
      checkoutLink.style.opacity = "0.6";
      return;
    }

    status.textContent = `You have ${cartItems.length} product(s) in your cart.`;
    list.innerHTML = cartItems
      .map(
        ({ id, quantity, product }) => `
          <li>
            <img src="${getProductImage(product)}" alt="${escapeHtml(product.name)}">
            <div class="cart-line">
              <h3>${escapeHtml(product.name)}</h3>
              <p>${formatCurrency(product.price)}</p>
              <label>
                Quantity
                <input type="number" min="1" value="${quantity}" data-qty-id="${id}">
              </label>
              <button type="button" data-remove-id="${id}">Remove</button>
            </div>
          </li>
        `
      )
      .join("");

    subtotal.textContent = formatCurrency(getCartSubtotal(cart));
    count.textContent = String(getCartCount(cart));
    checkoutLink.removeAttribute("aria-disabled");
    checkoutLink.style.pointerEvents = "";
    checkoutLink.style.opacity = "";
  }

  list.addEventListener("input", (event) => {
    const input = event.target.closest("[data-qty-id]");
    if (!input) {
      return;
    }
    updateCartQuantity(input.dataset.qtyId, Math.max(1, Number(input.value) || 1));
    render();
  });

  list.addEventListener("click", (event) => {
    const button = event.target.closest("[data-remove-id]");
    if (!button) {
      return;
    }
    removeFromCart(button.dataset.removeId);
    render();
  });

  clearButton.addEventListener("click", () => {
    saveCart([]);
    render();
  });

  render();
}

/**
 * Initialize checkout page
 * Displays order summary and form for customer information
 * Submits order to backend API on form submission
 */
function initCheckoutPage() {
  const form = document.getElementById("checkout-form");
  const summary = document.getElementById("checkout-summary");
  const message = document.getElementById("checkout-message");

  function renderSummary() {
    const cart = getCart();
    if (cart.length === 0) {
      summary.innerHTML = `
        <p><span>Items</span><strong>0</strong></p>
        <p><span>Subtotal</span><strong>${formatCurrency(0)}</strong></p>
        <p><span>Shipping</span><strong>${formatCurrency(0)}</strong></p>
        <p><span>Total</span><strong>${formatCurrency(0)}</strong></p>
      `;
      message.textContent = "Your cart is empty. Add products before placing an order.";
      return false;
    }

    const subtotal = getCartSubtotal(cart);
    const total = subtotal + SHIPPING_FEE;
    summary.innerHTML = `
      <p><span>Items</span><strong>${getCartCount(cart)}</strong></p>
      <p><span>Subtotal</span><strong>${formatCurrency(subtotal)}</strong></p>
      <p><span>Shipping</span><strong>${formatCurrency(SHIPPING_FEE)}</strong></p>
      <p><span>Total</span><strong>${formatCurrency(total)}</strong></p>
    `;
    if (isAuthenticated()) {
      message.textContent = "";
    }
    return true;
  }

  form.addEventListener("submit", async (event) => {
    event.preventDefault();

    if (!renderSummary()) {
      return;
    }

    if (!form.reportValidity()) {
      message.textContent = "Please complete all required checkout fields.";
      return;
    }

    if (!isAuthenticated()) {
      message.innerHTML = 'Please <a href="TASK7.html">sign in</a> before placing an order.';
      return;
    }

    try {
      const cart = getCart();
      const subtotal = getCartSubtotal(cart);
      const total = subtotal + SHIPPING_FEE;

      // Build order object to send to backend
      const order = {
        customerEmail: form.email?.value || form.fullname.value + "@shopease.local",
        deliveryAddress: `${form.street.value}, ${form.city.value}, ${form.zip.value}`,
        totalAmount: total,
        shippingFee: SHIPPING_FEE,
        status: "PENDING",
        orderItems: cart.map((item) => {
          const product = PRODUCTS.find((p) => p.id == item.id);
          return {
            productId: product?.id,
            quantity: item.quantity,
            unitPrice: product?.price || 0,
            totalPrice: (product?.price || 0) * item.quantity
          };
        })
      };

      console.log("Submitting order to backend:", order);
      const createdOrder = await createOrder(order);
      console.log("Order created successfully:", createdOrder);

      // Save to localStorage as backup
      const orders = getOrders();
      orders.unshift({
        id: createdOrder.id || `ORD-${Date.now()}`,
        createdAt: new Date().toLocaleString(),
        customer: {
          fullname: form.fullname.value.trim(),
          email: form.email?.value || "",
          street: form.street.value.trim(),
          city: form.city.value.trim(),
          zip: form.zip.value.trim(),
          payment: form.querySelector('input[name="payment"]:checked')?.value || ""
        },
        items: cart.map((item) => {
          const product = PRODUCTS.find((p) => p.id == item.id);
          return {
            id: item.id,
            name: product?.name || item.id,
            quantity: item.quantity,
            price: product?.price || 0
          };
        }),
        subtotal,
        shipping: SHIPPING_FEE,
        total
      });
      localStorage.setItem(STORAGE_KEYS.orders, JSON.stringify(orders));

      saveCart([]);
      message.textContent = "Order placed successfully! Redirecting...";
      setTimeout(() => {
        window.location.href = "TASK6.html";
      }, 1500);
    } catch (error) {
      console.error("Error placing order:", error);
      message.textContent = `Error: ${error.message}. Please try again.`;
    }
  });

  renderSummary();
  requireSessionForPage(message);
}

function initLoginPage() {
  const loginForm = document.getElementById("login-form");
  const registerForm = document.getElementById("register-form");
  const protectedForm = document.getElementById("protected-demo-form");
  const message = document.getElementById("login-message");
  const registerMessage = document.getElementById("register-message");
  const protectedMessage = document.getElementById("protected-demo-message");
  const validationMessage = document.getElementById("validation-demo-message");
  const currentUser = document.getElementById("current-user");
  const logoutButton = document.getElementById("logout-button");
  const refreshSessionButton = document.getElementById("refresh-session");
  const failWithoutSessionButton = document.getElementById("fail-without-session");
  const validationButton = document.getElementById("validation-demo");

  async function renderAuthState() {
    try {
      const csrfToken = await fetchCsrfToken();
      const csrfInput = document.getElementById("csrf-token");
      if (csrfInput) {
        csrfInput.value = csrfToken;
      }
      const session = await fetchSession();
      if (!session.authenticated) {
        localStorage.removeItem(STORAGE_KEYS.sessionUser);
        currentUser.textContent = "No active session cookie.";
        logoutButton.hidden = true;
        return;
      }

      localStorage.setItem(STORAGE_KEYS.sessionUser, session.username);
      currentUser.textContent = `Session active as ${session.username}.`;
      logoutButton.hidden = false;
    } catch (error) {
      currentUser.textContent = "You are not signed in.";
      logoutButton.hidden = true;
    }
  }

  if (registerForm) {
    registerForm.addEventListener("submit", async (event) => {
      event.preventDefault();
      registerMessage.textContent = "";

      try {
        const created = await registerAccount(
          getFormValue(registerForm, "register-username"),
          getFormValue(registerForm, "register-password"),
          getFormValue(registerForm, "register-role")
        );
        registerMessage.textContent = `${created.message}: ${created.username}`;
        registerForm.reset();
      } catch (error) {
        registerMessage.textContent = `Register failed: ${error.message}`;
      }
    });
  }

  loginForm.addEventListener("submit", async (event) => {
    event.preventDefault();
    message.textContent = "";

    try {
      const login = await loginAccount(getFormValue(loginForm, "username"), getFormValue(loginForm, "password"));
      message.textContent = `${login.message}. JSESSIONID cookie is now stored by the browser.`;
      loginForm.reset();
      await renderAuthState();
    } catch (error) {
      message.textContent = `Login failed: ${error.message}`;
    }
  });

  logoutButton.addEventListener("click", async () => {
    const result = await logoutAccount();
    message.textContent = result.message || "Signed out.";
    await renderAuthState();
  });

  refreshSessionButton.addEventListener("click", renderAuthState);

  if (failWithoutSessionButton && protectedForm) {
    failWithoutSessionButton.addEventListener("click", async () => {
      protectedMessage.textContent = "";
      try {
        await logoutAccount();
        await createProduct(buildDemoProduct(protectedForm));
        protectedMessage.textContent = "Unexpected success. Clear cookies and try again.";
      } catch (error) {
        protectedMessage.textContent = `Protected action failed without session: ${error.message}`;
      } finally {
        await renderAuthState();
      }
    });
  }

  if (protectedForm) {
    protectedForm.addEventListener("submit", async (event) => {
      event.preventDefault();
      protectedMessage.textContent = "";

      try {
        const product = await createProduct(buildDemoProduct(protectedForm));
        protectedMessage.textContent = `Protected action succeeded with session. Created product ID ${product.id}: ${product.name}`;
      } catch (error) {
        protectedMessage.textContent = `Protected action failed: ${error.message}`;
      }
    });
  }

  if (validationButton) {
    validationButton.addEventListener("click", async () => {
      validationMessage.textContent = "";
      try {
        await createProduct({
          name: "Invalid Price Demo",
          description: "This request intentionally uses a negative price.",
          price: -100,
          category: "Bracelets",
          stockQuantity: 3,
          imageUrl: "https://via.placeholder.com/200?text=Invalid"
        });
        validationMessage.textContent = "Unexpected success. The validation rule did not run.";
      } catch (error) {
        validationMessage.textContent = `Validation error returned: ${error.message}`;
      }
    });
  }

  renderAuthState();
}

function buildDemoProduct(form) {
  return {
    name: getFormValue(form, "product-name"),
    description: getFormValue(form, "product-description"),
    price: Number(getFormValue(form, "product-price")),
    category: getFormValue(form, "product-category"),
    stockQuantity: Number(getFormValue(form, "product-stock")),
    imageUrl: "https://via.placeholder.com/200?text=Session+Demo"
  };
}

function getFormValue(form, name) {
  return String(form.elements.namedItem(name)?.value || "").trim();
}

/**
 * Initialize orders page
 * Displays latest order prominently and order history
 * Falls back to localStorage if API orders aren't available
 */
function initOrdersPage() {
  const emptyState = document.getElementById("order-empty-state");
  const latestOrder = document.getElementById("latest-order");
  const history = document.getElementById("order-history");
  const orders = getOrders();

  if (orders.length === 0) {
    emptyState.textContent = "No orders have been placed yet. Complete checkout to populate this page.";
    latestOrder.innerHTML = "";
    history.innerHTML = '<p class="inline-note">Your future orders will appear here.</p>';
    return;
  }

  const [recent, ...rest] = orders;
  emptyState.textContent = "Your latest order was saved successfully.";
  latestOrder.innerHTML = renderOrderCard(recent, true);
  history.innerHTML = [recent, ...rest].map((order) => renderOrderCard(order, false)).join("");
}

/**
 * Render product cards in a grid
 * Handles empty state when no products are available
 * 
 * @param {HTMLElement} container - The container element
 * @param {Array} products - Array of product objects
 * @param {object} options - Rendering options (e.g., showOriginalPrice)
 */
function renderProductCards(container, products, options = {}) {
  if (!container) {
    return;
  }

  if (!products || products.length === 0) {
    container.innerHTML = '<article><h3>No matching products found.</h3><p>Try a different filter combination or check back later.</p></article>';
    return;
  }

  container.innerHTML = products
    .map((product) => {
      const price = parseFloat(product.price) || 0;
      const originalPrice = parseFloat(product.originalPrice) || 0;
      const priceHtml = options.showOriginalPrice && originalPrice > price && originalPrice > 0
        ? `<p><del>${formatCurrency(originalPrice)}</del> ${formatCurrency(price)}</p>`
        : `<p>${formatCurrency(price)}</p>`;

      return `
        <article>
          <img src="${getProductImage(product)}" alt="${escapeHtml(product.name)}">
          <span class="product-badge">${escapeHtml(product.badge || "New")}</span>
          <h3>${escapeHtml(product.name)}</h3>
          <p class="product-meta">${escapeHtml(product.tagline || "")}</p>
          ${priceHtml}
          <div class="product-actions">
            <a href="TASK3.html?item=${product.id}" role="button">View Details</a>
            <button type="button" data-add-id="${product.id}">Add to Cart</button>
          </div>
        </article>
      `;
    })
    .join("");

  // Attach event listeners for Add to Cart buttons
  container.querySelectorAll("[data-add-id]").forEach((button) => {
    button.addEventListener("click", () => {
      const productId = button.dataset.addId;
      addToCart(productId, 1);
      const feedback = `Added to cart (Qty: 1)`;
      button.textContent = feedback;
      setTimeout(() => {
        button.textContent = "Add to Cart";
      }, 2000);
    });
  });
}

/**
 * Render a single order card
 * Displays order information and items in card format
 * 
 * @param {object} order - The order object
 * @param {boolean} expanded - Whether to show expanded view
 * @returns {string} - HTML string for the order card
 */
function renderOrderCard(order, expanded) {
  const items = (order.items || [])
    .map(
      (item) =>
        `<li>${escapeHtml(item.name)} x ${item.quantity} - ${formatCurrency(item.price * item.quantity)}</li>`
    )
    .join("");

  const customer = order.customer || {};
  const address = `${customer.street || ""}, ${customer.city || ""}, ${customer.zip || ""}`.replace(/^, /, "").replace(/, $/, "");

  return `
    <article class="order-card${expanded ? " order-card-latest" : ""}">
      <h3>${escapeHtml(order.id)}</h3>
      <p><strong>Placed:</strong> ${escapeHtml(order.createdAt)}</p>
      <p><strong>Customer:</strong> ${escapeHtml(customer.fullname || "Unknown")}</p>
      <p><strong>Payment:</strong> ${escapeHtml(customer.payment || "Not specified")}</p>
      <p><strong>Shipping Address:</strong> ${escapeHtml(address || "Not provided")}</p>
      <ul>${items || "<li>No items</li>"}</ul>
      <p><strong>Total:</strong> ${formatCurrency(order.total || 0)}</p>
    </article>
  `;
}

/**
 * Get cart from localStorage
 * @returns {Array} - Array of cart items
 */
function getCart() {
  return readStorageArray(STORAGE_KEYS.cart);
}

/**
 * Get orders from localStorage
 * @returns {Array} - Array of orders
 */
function getOrders() {
  return readStorageArray(STORAGE_KEYS.orders);
}

/**
 * Add product to cart or increment quantity if already in cart
 * 
 * @param {number} productId - The product ID
 * @param {number} quantity - Quantity to add
 */
function addToCart(productId, quantity) {
  const cart = getCart();
  const existing = cart.find((item) => item.id == productId);

  if (existing) {
    existing.quantity += quantity;
  } else {
    cart.push({ id: productId, quantity });
  }

  saveCart(cart);
  console.log(`Added ${quantity} of product ${productId} to cart`);
}

/**
 * Update quantity of a product in the cart
 * 
 * @param {number} productId - The product ID
 * @param {number} quantity - New quantity
 */
function updateCartQuantity(productId, quantity) {
  const cart = getCart().map((item) =>
    item.id == productId ? { ...item, quantity } : item
  );
  saveCart(cart);
}

/**
 * Remove product from cart
 * 
 * @param {number} productId - The product ID to remove
 */
function removeFromCart(productId) {
  saveCart(getCart().filter((item) => item.id != productId));
}

/**
 * Save cart to localStorage
 * 
 * @param {Array} cart - The cart array to save
 */
function saveCart(cart) {
  localStorage.setItem(STORAGE_KEYS.cart, JSON.stringify(cart));
}

/**
 * Calculate cart subtotal
 * 
 * @param {Array} cart - The cart array
 * @returns {number} - Subtotal amount
 */
function getCartSubtotal(cart) {
  return cart.reduce((sum, item) => {
    const product = PRODUCTS.find((p) => p.id == item.id);
    const price = parseFloat(product?.price) || 0;
    return sum + (price * item.quantity);
  }, 0);
}

/**
 * Get total item count in cart
 * 
 * @param {Array} cart - The cart array
 * @returns {number} - Total item count
 */
function getCartCount(cart) {
  return cart.reduce((sum, item) => sum + item.quantity, 0);
}

/**
 * Find product by ID in cached PRODUCTS array
 * 
 * @param {number} id - The product ID
 * @returns {object|undefined} - The product object or undefined
 */
function getProductById(id) {
  return PRODUCTS.find((product) => product.id == id);
}

/**
 * Read array from localStorage, with fallback to empty array
 * 
 * @param {string} key - The localStorage key
 * @returns {Array} - Parsed array or empty array
 */
function readStorageArray(key) {
  try {
    const parsed = JSON.parse(localStorage.getItem(key) || "[]");
    return Array.isArray(parsed) ? parsed : [];
  } catch (error) {
    console.error(`Error reading from localStorage (${key}):`, error);
    return [];
  }
}

function isAuthenticated() {
  return Boolean(localStorage.getItem(STORAGE_KEYS.sessionUser));
}

async function requireSessionForPage(messageElement) {
  try {
    const session = await fetchSession();
    if (session.authenticated) {
      localStorage.setItem(STORAGE_KEYS.sessionUser, session.username);
      return true;
    }
  } catch (error) {
    console.warn("Session check failed:", error);
  }

  localStorage.removeItem(STORAGE_KEYS.sessionUser);
  if (messageElement) {
    messageElement.innerHTML = 'Please <a href="TASK7.html">sign in</a> before placing an order.';
  }
  setTimeout(() => {
    window.location.href = "TASK7.html";
  }, 1200);
  return false;
}

/**
 * Get image URL for a product
 * Falls back to placeholder SVG if image is not available
 * 
 * @param {object} product - The product object
 * @returns {string} - Image URL
 */
function getProductImage(product) {
  return product.image || product.imageUrl || buildPlaceholderImage(product.name);
}

function normalizeProducts(products) {
  return Array.isArray(products) ? products.map(normalizeProduct) : [];
}

function normalizeProduct(product) {
  if (!product) {
    return null;
  }

  return {
    ...product,
    image: product.image || product.imageUrl || "",
    category: normalizeCategory(product.category),
    tagline: product.tagline || product.description || "",
    badge: product.badge || (product.stockQuantity > 0 ? "In Stock" : "Sold Out")
  };
}

function normalizeCategory(category) {
  const value = String(category || "").trim().toLowerCase();
  if (value.endsWith("s")) {
    return value.slice(0, -1);
  }
  return value;
}

/**
 * Build a placeholder SVG image when product image is unavailable
 * 
 * @param {string} name - Product name to display
 * @returns {string} - Data URL for SVG placeholder
 */
function buildPlaceholderImage(name) {
  const svg = `
    <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 600 420">
      <rect width="600" height="420" fill="#e9eef2"/>
      <circle cx="300" cy="155" r="70" fill="#c8d4dc"/>
      <rect x="170" y="250" width="260" height="20" rx="10" fill="#b3c0c8"/>
      <text x="300" y="340" font-family="Segoe UI, Arial, sans-serif" font-size="34" text-anchor="middle" fill="#2c3e50">${escapeHtml(name)}</text>
    </svg>
  `.trim();
  return `data:image/svg+xml;charset=UTF-8,${encodeURIComponent(svg)}`;
}

/**
 * Apply product image to an image element with error handling
 * 
 * @param {HTMLImageElement} image - The image element
 * @param {object} product - The product object
 */
function applyProductImage(image, product) {
  image.src = getProductImage(product);
  image.alt = product.name;
  image.addEventListener(
    "error",
    () => {
      image.src = buildPlaceholderImage(product.name);
    },
    { once: true }
  );
}

/**
 * Format a number as Philippine Peso currency
 * 
 * @param {number} value - The value to format
 * @returns {string} - Formatted currency string
 */
function formatCurrency(value) {
  return `Php${parseFloat(value).toLocaleString("en-PH")}`;
}

/**
 * Escape HTML special characters to prevent XSS
 * 
 * @param {string} value - The value to escape
 * @returns {string} - Escaped HTML-safe string
 */
function escapeHtml(value) {
  return String(value)
    .replaceAll("&", "&amp;")
    .replaceAll("<", "&lt;")
    .replaceAll(">", "&gt;")
    .replaceAll('"', "&quot;")
    .replaceAll("'", "&#39;");
}
