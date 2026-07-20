# 🏦 Banking Management System

A secure Banking Management System built using Spring Boot, Spring Security (JWT), MySQL, and JPA. The project implements authentication, role-based authorization, account management, transaction processing, audit logging, refresh token authentication, and Swagger documentation.

---

# 🚀 Features

## Authentication
- User Registration & Login
- JWT Authentication & Refresh Token Authentication
- BCrypt Password Encryption

## User Management
- Create, Update, Delete Users, and Get User By ID
- Pagination, Search, and Sorting support

## Account Management
- Create & Delete Accounts
- Account Status tracking (ACTIVE / BLOCKED / CLOSED)
- Balance check

## Transactions
- Deposit, Withdraw, and Transfer between accounts
- Mini Statement & Bank Statement generation

## Security & Architecture
- Role-Based Access Control (ADMIN / USER)
- Custom JWT Filters & Spring Security integration
- SLF4J logging and Audit Logging

## Documentation
- Swagger UI (OpenAPI)

---

# 🛠 Tech Stack

- **Java Version:** Java 17 / 21
- **Framework:** Spring Boot 3.2.5
- **Database:** Aiven MySQL (Cloud Database)
- **ORM:** Spring Data JPA / Hibernate
- **Security:** Spring Security & JWT
- **Build Tool:** Maven
- **Containerization:** Docker (Multi-stage build)

---

# 📂 Project Structure

- `config`: Security, JWT and Swagger configurations
- `controller`: REST APIs entry points
- `dto`: Request and Response Data Transfer Objects
- `entity`: JPA entities mapping to database tables
- `exception`: Global exception handler and custom exceptions
- `repository`: Spring Data JPA repositories
- `service`: Business logic layer
- `util`: Utility helper classes

---


# 🏁 How to Run Locally

### Option A: Running with Maven
1. Ensure Java 17+ is installed.
2. Run the application using the Maven wrapper:
   ```bash
   ./mvnw spring-boot:run
   ```

### Option B: Running with Docker
1. Build the Docker image:
   ```bash
   docker build -t banking-system .
   ```
2. Run the Docker container:
   ```bash
   docker run -p 8080:10000 -e PORT=10000 banking-system
   ```

---

# 📖 API Documentation
Once the application starts, you can view the interactive Swagger API documentation at:
- **Local:** `http://localhost:8080/swagger-ui/index.html`

---

# 👨‍💻 Developer

**Goutam Soni** - Java FullStack Developer
**Lavkesh Khare** - Java Backend Developer
```
Spring Boot | MySQL | JWT | REST API | Docker
```