# Warehouse Management System

A comprehensive warehouse management solution built with Spring Boot and Angular, designed to efficiently manage inventory, products, categories, and warehouse operations.

## Overview

This Warehouse Management System provides a robust platform for tracking and managing inventory across multiple warehouses. It implements Domain-Driven Design (DDD) principles to ensure a clean, maintainable architecture that accurately reflects business processes.

## Features

- **Product Management**: Create, update, delete, and search products with category classification
- **Category Management**: Organize products into hierarchical categories
- **Warehouse Management**: Track inventory across multiple warehouse locations
- **User Management**: Role-based access control for different user types
- **Caching**: Redis-based caching for improved performance
- **RESTful API**: Well-documented API for easy integration

## Technology Stack

### Backend
- Java 23
- Spring Boot 3.4.0
- Spring Data JPA
- Spring Security
- Redis for caching
- MySQL 8.0 database
- Flyway for database migrations
- Gradle 8.11.1 for build management
- Swagger/OpenAPI for API documentation

### Frontend
- Angular 19
- TypeScript
- RxJS
- Angular Router
- Angular CLI

### DevOps
- Docker & Docker Compose
- GitHub Actions for CI/CD

## Getting Started

### Prerequisites
- JDK 23 or higher
- Docker and Docker Compose
- Node.js and npm
- Angular CLI
- MySQL (Docker container)
- Redis (Docker container)

### Installation

1. Clone the repository:
   ```bash
   git clone https://github.com/yourusername/warehouse-management-system.git
   cd warehouse-management-system
   ```

2. Start the required services using Docker Compose:
   ```bash
   docker-compose up -d
   ```

5. Access the application:
   - Frontend: http://localhost:4200
   - Backend API: http://localhost:8080
   - Swagger UI: http://localhost:8080/swagger-ui.html
