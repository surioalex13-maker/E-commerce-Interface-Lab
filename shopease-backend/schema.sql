-- ShopEase Database Schema and Sample Data
-- Run this script to initialize the database with tables and sample data

-- Create Categories table
CREATE TABLE IF NOT EXISTS categories (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE,
    description TEXT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Create Users table
CREATE TABLE IF NOT EXISTS users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    full_name VARCHAR(100),
    phone VARCHAR(20),
    address TEXT,
    enabled BOOLEAN DEFAULT TRUE,
    role VARCHAR(50) DEFAULT 'USER'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Create Products table
CREATE TABLE IF NOT EXISTS products (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    tagline VARCHAR(255),
    price DECIMAL(10,2) NOT NULL,
    original_price DECIMAL(10,2),
    image VARCHAR(500),
    badge VARCHAR(100),
    specs JSON,
    reviews JSON,
    category_id BIGINT NOT NULL,
    stock INT NOT NULL DEFAULT 100,
    FOREIGN KEY (category_id) REFERENCES categories(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Create Orders table
CREATE TABLE IF NOT EXISTS orders (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    customer_email VARCHAR(255) NOT NULL,
    status VARCHAR(50) DEFAULT 'PENDING',
    total_amount DECIMAL(10,2) NOT NULL,
    shipping_fee DECIMAL(10,2),
    delivery_address TEXT,
    created_at DATETIME NOT NULL,
    updated_at DATETIME NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Create OrderItems table
CREATE TABLE IF NOT EXISTS order_items (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    order_id BIGINT NOT NULL,
    product_id BIGINT NOT NULL,
    quantity INT NOT NULL,
    price DECIMAL(10,2) NOT NULL,
    FOREIGN KEY (order_id) REFERENCES orders(id) ON DELETE CASCADE,
    FOREIGN KEY (product_id) REFERENCES products(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Insert Sample Categories
INSERT INTO categories (name, description) VALUES
('Necklaces', 'Elegant and stylish necklaces for all occasions'),
('Bracelets', 'Beautiful bracelets made with premium materials'),
('Earrings', 'Stunning earrings to complement your style'),
('Rings', 'Exquisite rings perfect for special moments'),
('Anklets', 'Delicate anklets for a fashionable look');

-- Insert Sample Products
INSERT INTO products (name, description, tagline, price, original_price, image, badge, category_id, stock) VALUES
('Gold Necklace', 'Elegant 14k gold chain necklace with pendant', 'Timeless elegance', 299.99, 399.99, 'https://via.placeholder.com/300?text=Gold+Necklace', 'Best Seller', 1, 15),
('Silver Chain', 'Classic sterling silver chain for everyday wear', 'Pure elegance', 149.99, 199.99, 'https://via.placeholder.com/300?text=Silver+Chain', 'Popular', 1, 25),
('Diamond Pendant', 'Stunning diamond pendant necklace', 'Premium quality', 799.99, 999.99, 'https://via.placeholder.com/300?text=Diamond+Pendant', 'Premium', 1, 8),
('Pearl Necklace', 'Beautiful freshwater pearl necklace', 'Sophisticated style', 189.99, 249.99, 'https://via.placeholder.com/300?text=Pearl+Necklace', NULL, 1, 20),
('Gold Bracelet', 'Elegant 14k gold bracelet with intricate design', 'Luxury wear', 349.99, 449.99, 'https://via.placeholder.com/300?text=Gold+Bracelet', 'Best Seller', 2, 12),
('Silver Bracelet', 'Classic sterling silver bracelet', 'Timeless beauty', 129.99, 179.99, 'https://via.placeholder.com/300?text=Silver+Bracelet', 'Popular', 2, 30),
('Diamond Bracelet', 'Tennis bracelet with brilliant diamonds', 'Luxurious', 1299.99, 1599.99, 'https://via.placeholder.com/300?text=Diamond+Bracelet', 'Premium', 2, 5),
('Pearl Earrings', 'Freshwater pearl stud earrings', 'Elegant simplicity', 199.99, 249.99, 'https://via.placeholder.com/300?text=Pearl+Earrings', NULL, 3, 20),
('Diamond Earrings', '1 carat diamond stud earrings', 'Brilliant sparkle', 1499.99, 1999.99, 'https://via.placeholder.com/300?text=Diamond+Earrings', 'Premium', 3, 6),
('Gold Hoops', 'Classic 14k gold hoop earrings', 'Timeless style', 249.99, 349.99, 'https://via.placeholder.com/300?text=Gold+Hoops', 'Popular', 3, 18),
('Diamond Ring', '1 carat diamond engagement ring', 'Forever love', 4999.99, 6999.99, 'https://via.placeholder.com/300?text=Diamond+Ring', 'Premium', 4, 5),
('Gold Ring', 'Beautiful 14k gold band ring', 'Classic design', 399.99, 599.99, 'https://via.placeholder.com/300?text=Gold+Ring', 'Best Seller', 4, 22),
('Silver Ring', 'Elegant sterling silver ring', 'Simple elegance', 149.99, 199.99, 'https://via.placeholder.com/300?text=Silver+Ring', NULL, 4, 28),
('Sapphire Ring', 'Stunning sapphire ring with diamond accents', 'Luxurious', 799.99, 1099.99, 'https://via.placeholder.com/300?text=Sapphire+Ring', 'Premium', 4, 7),
('Gold Anklet', 'Delicate 14k gold anklet', 'Summer style', 199.99, 299.99, 'https://via.placeholder.com/300?text=Gold+Anklet', 'Popular', 5, 16),
('Silver Anklet', 'Beautiful sterling silver anklet', 'Perfect for summer', 99.99, 149.99, 'https://via.placeholder.com/300?text=Silver+Anklet', NULL, 5, 32);

-- Insert Sample Users
INSERT INTO users (email, password, full_name, phone, address, enabled, role) VALUES
('admin@shopease.com', '$2a$10$xX7XqXqXqXqXqXqXqXqXqX', 'Admin User', '1234567890', '123 Admin St, City', TRUE, 'ADMIN'),
('customer@shopease.com', '$2a$10$xX7XqXqXqXqXqXqXqXqXqX', 'John Customer', '9876543210', '456 Customer Ave, Town', TRUE, 'USER');

-- Create indexes for better query performance
CREATE INDEX idx_products_category ON products(category_id);
CREATE INDEX idx_orders_customer ON orders(customer_email);
CREATE INDEX idx_orders_status ON orders(status);
CREATE INDEX idx_order_items_order ON order_items(order_id);
CREATE INDEX idx_order_items_product ON order_items(product_id);
CREATE INDEX idx_users_email ON users(email);
