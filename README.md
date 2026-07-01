# 🏦 Banking Management System

A secure Banking Management System built using Spring Boot, Spring Security (JWT), MySQL, and JPA. The project implements authentication, role-based authorization, account management, transaction processing, audit logging, refresh token authentication, and Swagger documentation.

---

# 🚀 Features

## Authentication
- User Registration
- User Login
- JWT Authentication
- Refresh Token Authentication
- BCrypt Password Encryption

## User Management
- Create User
- Update User
- Delete User
- Get User By Id
- Pagination
- Search
- Sorting

## Account Management
- Create Account
- Delete Account
- Account Status (ACTIVE / BLOCKED / CLOSED)
- Balance Check

## Transactions
- Deposit
- Withdraw
- Transfer
- Mini Statement
- Bank Statement

## Security
- Role Based Access (ADMIN / USER)
- JWT Filter
- Spring Security

## Logging
- SLF4J Logging
- Audit Logging

## Documentation
- Swagger UI

---

# 🛠 Tech Stack

- Java 21
- Spring Boot 3
- Spring Security
- Spring Data JPA
- Hibernate
- MySQL
- JWT
- Lombok
- Maven
- Swagger (OpenAPI)

---

# 📂 Project Structure

```
controller
service
repository
entity
dto
config
exception
util
```

---

# 🔐 Roles

## ADMIN

- Manage Users
- Create/Delete Accounts
- View Audit Logs

## USER

- Deposit
- Withdraw
- Transfer
- View Balance
- View Statements

---

# 📖 API Modules

- Authentication APIs
- User APIs
- Account APIs
- Transaction APIs
- Audit APIs

---

# ⚙️ Future Enhancements

- Docker Support
- Live Deployment
- Email Notification
- Unit Testing
- CI/CD Pipeline

---

# 👨‍💻 Developer

Lavkesh Khare

Java Backend Developer

Spring Boot | MySQL | JWT | REST API