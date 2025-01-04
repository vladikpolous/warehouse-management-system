-- Create a role table
CREATE TABLE roles
(
    role_id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    role_name        VARCHAR(100) NOT NULL UNIQUE,
    role_description VARCHAR(255)
);

-- Create a user table
CREATE TABLE users
(
    user_id      BIGINT AUTO_INCREMENT PRIMARY KEY,
    name         VARCHAR(100) NOT NULL,
    email        VARCHAR(150) NOT NULL UNIQUE,
    password     VARCHAR(255) NOT NULL,
    role_id      BIGINT       NOT NULL,
    created_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (role_id) REFERENCES roles (role_id)
);

-- Create a warehouse table
CREATE TABLE warehouses
(
    warehouse_id        BIGINT AUTO_INCREMENT PRIMARY KEY,
    warehouse_name      VARCHAR(150) NOT NULL,
    location            VARCHAR(255),
    assigned_manager_id BIGINT,
    FOREIGN KEY (assigned_manager_id) REFERENCES users (user_id) ON DELETE SET NULL
);

-- Create a table of product categories
CREATE TABLE categories
(
    category_id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    category_name        VARCHAR(100) NOT NULL UNIQUE,
    category_description VARCHAR(255)
);

-- Create a product table
CREATE TABLE products
(
    product_id   BIGINT AUTO_INCREMENT PRIMARY KEY,
    product_name VARCHAR(150) NOT NULL,
    description  VARCHAR(255),
    category_id  BIGINT       NOT NULL,
    created_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (category_id) REFERENCES categories (category_id)
);

-- Create a table for accounting for goods in warehouses
CREATE TABLE warehouse_products
(
    warehouse_product_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    warehouse_id         BIGINT NOT NULL,
    product_id           BIGINT NOT NULL,
    quantity             INT    NOT NULL DEFAULT 0 CHECK (quantity >= 0),
    FOREIGN KEY (warehouse_id) REFERENCES warehouses (warehouse_id),
    FOREIGN KEY (product_id) REFERENCES products (product_id)
);