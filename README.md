# Ecommerce Monolith

A simple monolithic E-Commerce backend application built with Java 17 and Spring Boot.

Project structure

com.example.ecommerce
├── controller
├── service
├── repository
├── entity
├── dto
├── exception
└── config

Features
- User Management (CRUD)
- Product Management (CRUD)
- Order Management (create, list, get, update status, delete)
- Validation with javax validation
- Global exception handling
- Uses DTOs for API input/output
- Spring Boot Actuator /actuator/health

Getting started

1. Build

mvn clean package

2. Run

Provide the MySQL environment variables or use defaults:
- DB_HOST (default: localhost)
- DB_PORT (default: 3306)
- DB_NAME (default: ecommerce)
- DB_USER (default: root)
- DB_PASS (default: password)

Example:

export DB_HOST=localhost
export DB_PORT=3306
export DB_NAME=ecommerce
export DB_USER=root
export DB_PASS=secret

Then run:

java -jar target/ecommerce-monolith-0.0.1-SNAPSHOT.jar

API Endpoints

Users
- POST /api/users
  - Create user
  - Request body (JSON):

    {
      "name": "John Doe",
      "email": "john@example.com"
    }

  - Responses: 201 Created

- GET /api/users
  - Get all users
  - Response: 200 OK

- GET /api/users/{id}
  - Get user by ID
  - Response: 200 OK or 404 Not Found

- PUT /api/users/{id}
  - Update user
  - Request body: same as create
  - Response: 200 OK

- DELETE /api/users/{id}
  - Delete user
  - Response: 204 No Content

Products
- POST /api/products
  - Create product
  - Request body (JSON):

    {
      "name": "T-Shirt",
      "description": "100% cotton",
      "price": 19.99,
      "quantity": 100
    }

  - Response: 201 Created

- GET /api/products
  - Get all products
  - Response: 200 OK

- GET /api/products/{id}
  - Get product by ID

- PUT /api/products/{id}
  - Update product

- DELETE /api/products/{id}
  - Delete product

Orders
- POST /api/orders
  - Create order
  - Request body (JSON):

    {
      "userId": 1,
      "items": [
        { "productId": 1, "quantity": 2 },
        { "productId": 2, "quantity": 1 }
      ]
    }

  - Response: 201 Created

- GET /api/orders
  - Get all orders

- GET /api/orders/{id}
  - Get order by ID

- PUT /api/orders/{id}/status
  - Update order status
  - Request body (JSON):

    {
      "orderStatus": "PROCESSING"
    }

  - Valid statuses: PENDING, PROCESSING, COMPLETED, CANCELLED

- DELETE /api/orders/{id}
  - Delete order

Actuator
- GET /actuator/health

Notes
- This project intentionally excludes authentication, payment, and any DevOps/configuration files.
- DTOs are used to avoid exposing JPA entities directly in APIs.

