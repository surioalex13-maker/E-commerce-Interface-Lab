const PRODUCTS = [
  {
    id: "necklace",
    name: "Gold Necklace",
    image: "images/necklace.jpg",
    category: "necklace",
    price: 9999,
    originalPrice: 11999,
    badge: "Best Seller",
    tagline: "A polished 18K-inspired statement piece for formal wear.",
    description: "A premium necklace with a warm gold finish, heart pendant detail, and elegant chain styling.",
    specs: [
      ["Karat", "18K-inspired finish"],
      ["Gold Content", "91.6% tone simulation"],
      ["Color Tone", "Yellow Gold"],
      ["Pendant Style", "Heart-shaped"]
    ],
    reviews: [
      { name: "Alex S.", text: "Great product and very elegant in person." },
      { name: "Lyza C.", text: "Excellent value for the price." }
    ]
  },
  {
    id: "bracelet",
    name: "Silver Bracelet",
    image: "images/bracelet.jpg",
    category: "bracelet",
    price: 5000,
    originalPrice: 6500,
    badge: "Limited Offer",
    tagline: "A lightweight silver accessory for daily wear or special occasions.",
    description: "A sterling-silver-inspired bracelet with a bright finish and comfortable fit for all-day styling.",
    specs: [
      ["Material", "Sterling silver-inspired"],
      ["Length", "7 inches"],
      ["Clasp", "Secure lobster clasp"],
      ["Finish", "Polished silver tone"]
    ],
    reviews: [
      { name: "Mara T.", text: "Simple, classy, and easy to pair with outfits." },
      { name: "John P.", text: "Comfortable fit and looks even better up close." }
    ]
  }
];

const STORAGE_KEYS = {
  cart: "shopease-cart",
  orders: "shopease-orders"
};

const SHIPPING_FEE = 99;

document.addEventListener("DOMContentLoaded", () => {
  const page = document.body.dataset.page;

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
    default:
      break;
  }
});

function renderHome() {
  renderProductCards(document.querySelector('[data-list="featured"]'), PRODUCTS);
  renderProductCards(
    document.querySelector('[data-list="discounted"]'),
    PRODUCTS.filter((product) => product.originalPrice > product.price),
    { showOriginalPrice: true }
  );
}

function initProductListing() {
  const form = document.getElementById("filter-form");
  const grid = document.getElementById("catalog-grid");
  const summary = document.getElementById("filter-summary");

  function updateCatalog() {
    const selectedCategories = Array.from(
      form.querySelectorAll('input[name="category"]:checked'),
      (input) => input.value
    );
    const priceFilter = form.querySelector('input[name="price"]:checked')?.value || "all";
    const searchTerm = form.search.value.trim().toLowerCase();

    const filtered = PRODUCTS.filter((product) => {
      const matchesCategory =
        selectedCategories.length === 0 || selectedCategories.includes(product.category);
      const matchesSearch =
        searchTerm === "" ||
        product.name.toLowerCase().includes(searchTerm) ||
        product.description.toLowerCase().includes(searchTerm);
      const matchesPrice =
        priceFilter === "all" ||
        (priceFilter === "under-7000" && product.price <= 7000) ||
        (priceFilter === "over-7000" && product.price > 7000);

      return matchesCategory && matchesSearch && matchesPrice;
    });

    renderProductCards(grid, filtered, { showOriginalPrice: true });
    summary.textContent = `${filtered.length} product(s) shown.`;
  }

  form.addEventListener("input", updateCatalog);
  form.addEventListener("change", updateCatalog);
  updateCatalog();
}

function initDetailsPage() {
  const params = new URLSearchParams(window.location.search);
  const product = getProductById(params.get("item")) || PRODUCTS[0];
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

  document.getElementById("spec-table").innerHTML = product.specs
    .map(
      ([label, value]) =>
        `<tr><th scope="row">${escapeHtml(label)}</th><td>${escapeHtml(value)}</td></tr>`
    )
    .join("");

  document.getElementById("review-list").innerHTML = product.reviews
    .map(
      (review) => `
        <article>
          <h3>${escapeHtml(review.name)}</h3>
          <blockquote>${escapeHtml(review.text)}</blockquote>
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
        const product = getProductById(item.id);
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
    message.textContent = "";
    return true;
  }

  form.addEventListener("submit", (event) => {
    event.preventDefault();

    if (!renderSummary()) {
      return;
    }

    if (!form.reportValidity()) {
      message.textContent = "Please complete all required checkout fields.";
      return;
    }

    const cart = getCart();
    const subtotal = getCartSubtotal(cart);
    const total = subtotal + SHIPPING_FEE;
    const order = {
      id: `ORD-${Date.now()}`,
      createdAt: new Date().toLocaleString(),
      customer: {
        fullname: form.fullname.value.trim(),
        street: form.street.value.trim(),
        city: form.city.value.trim(),
        zip: form.zip.value.trim(),
        payment: form.querySelector('input[name="payment"]:checked')?.value || ""
      },
      items: cart.map((item) => {
        const product = getProductById(item.id);
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
    };

    const orders = getOrders();
    orders.unshift(order);
    localStorage.setItem(STORAGE_KEYS.orders, JSON.stringify(orders));
    saveCart([]);
    window.location.href = "TASK6.html";
  });

  renderSummary();
}

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

function renderProductCards(container, products, options = {}) {
  if (!container) {
    return;
  }

  if (products.length === 0) {
    container.innerHTML = '<article><h3>No matching products found.</h3><p>Try a different filter combination.</p></article>';
    return;
  }

  container.innerHTML = products
    .map((product) => {
      const priceHtml = options.showOriginalPrice && product.originalPrice > product.price
        ? `<p><del>${formatCurrency(product.originalPrice)}</del> ${formatCurrency(product.price)}</p>`
        : `<p>${formatCurrency(product.price)}</p>`;

      return `
        <article>
          <img src="${getProductImage(product)}" alt="${escapeHtml(product.name)}">
          <span class="product-badge">${escapeHtml(product.badge)}</span>
          <h3>${escapeHtml(product.name)}</h3>
          <p class="product-meta">${escapeHtml(product.tagline)}</p>
          ${priceHtml}
          <div class="product-actions">
            <a href="TASK3.html?item=${product.id}" role="button">View Details</a>
            <button type="button" data-add-id="${product.id}">Add to Cart</button>
          </div>
        </article>
      `;
    })
    .join("");

  container.querySelectorAll("[data-add-id]").forEach((button) => {
    button.addEventListener("click", () => addToCart(button.dataset.addId, 1));
  });

  container.querySelectorAll("img[data-product-id]").forEach((image) => {
    const product = getProductById(image.dataset.productId);
    if (product) {
      applyProductImage(image, product);
    }
  });
}

function renderOrderCard(order, expanded) {
  const items = order.items
    .map(
      (item) =>
        `<li>${escapeHtml(item.name)} x ${item.quantity} - ${formatCurrency(item.price * item.quantity)}</li>`
    )
    .join("");

  return `
    <article class="order-card${expanded ? " order-card-latest" : ""}">
      <h3>${escapeHtml(order.id)}</h3>
      <p><strong>Placed:</strong> ${escapeHtml(order.createdAt)}</p>
      <p><strong>Customer:</strong> ${escapeHtml(order.customer.fullname)}</p>
      <p><strong>Payment:</strong> ${escapeHtml(order.customer.payment)}</p>
      <p><strong>Shipping Address:</strong> ${escapeHtml(
        `${order.customer.street}, ${order.customer.city}, ${order.customer.zip}`
      )}</p>
      <ul>${items}</ul>
      <p><strong>Total:</strong> ${formatCurrency(order.total)}</p>
    </article>
  `;
}

function getCart() {
  return readStorageArray(STORAGE_KEYS.cart);
}

function getOrders() {
  return readStorageArray(STORAGE_KEYS.orders);
}

function addToCart(productId, quantity) {
  const cart = getCart();
  const existing = cart.find((item) => item.id === productId);

  if (existing) {
    existing.quantity += quantity;
  } else {
    cart.push({ id: productId, quantity });
  }

  saveCart(cart);
}

function updateCartQuantity(productId, quantity) {
  const cart = getCart().map((item) =>
    item.id === productId ? { ...item, quantity } : item
  );
  saveCart(cart);
}

function removeFromCart(productId) {
  saveCart(getCart().filter((item) => item.id !== productId));
}

function saveCart(cart) {
  localStorage.setItem(STORAGE_KEYS.cart, JSON.stringify(cart));
}

function getCartSubtotal(cart) {
  return cart.reduce((sum, item) => {
    const product = getProductById(item.id);
    return sum + (product ? product.price * item.quantity : 0);
  }, 0);
}

function getCartCount(cart) {
  return cart.reduce((sum, item) => sum + item.quantity, 0);
}

function getProductById(id) {
  return PRODUCTS.find((product) => product.id === id);
}

function readStorageArray(key) {
  try {
    const parsed = JSON.parse(localStorage.getItem(key) || "[]");
    return Array.isArray(parsed) ? parsed : [];
  } catch (error) {
    return [];
  }
}

function getProductImage(product) {
  return product.image || buildPlaceholderImage(product.name);
}

function buildPlaceholderImage(name) {
  const svg = `
    <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 600 420">
      <rect width="600" height="420" fill="#e9eef2"/>
      <circle cx="300" cy="155" r="70" fill="#c8d4dc"/>
      <rect x="170" y="250" width="260" height="20" rx="10" fill="#b3c0c8"/>
      <text x="300" y="340" font-family="Segoe UI, Arial, sans-serif" font-size="34" text-anchor="middle" fill="#2c3e50">${name}</text>
    </svg>
  `.trim();
  return `data:image/svg+xml;charset=UTF-8,${encodeURIComponent(svg)}`;
}

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

function formatCurrency(value) {
  return `Php${value.toLocaleString("en-PH")}`;
}

function escapeHtml(value) {
  return String(value)
    .replaceAll("&", "&amp;")
    .replaceAll("<", "&lt;")
    .replaceAll(">", "&gt;")
    .replaceAll('"', "&quot;")
    .replaceAll("'", "&#39;");
}
