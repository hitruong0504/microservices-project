# Microservices Project

## 📌 Overview
This project is a **Spring Boot-based microservices architecture** that includes authentication, API gateway, and multiple services for order processing, product management, and payments.

## 🏗 Architecture

### **🔹 Key Components:**
- **Clients:** Web & mobile users interact via the API Gateway.
- **API Gateway:** Routes requests to the appropriate microservices.
- **Okta Auth:** Handles user authentication & authorization.
- **Service Registry:** Manages microservices discovery and registration.
- **Microservices:**
  - **Order Service** 🛒 - Handles order creation & tracking (Order DB).
  - **Product Service** 📦 - Manages products & inventory (Product DB).
  - **Payment Service** 💳 - Handles transactions & payments (Payment DB).
  - **User Service** 👤 - Manages user data & authentication.
  - **Config Server** ⚙ - Centralized configuration management.
- **Databases:** Separate DBs for each service for better scalability.

## 🚀 Tech Stack
- **Backend:** Spring Boot, Spring Cloud (Eureka, Config Server, API Gateway)
- **Security:** Okta Auth (OAuth 2.0)
- **Database:** MySQL/PostgreSQL (JPA, Hibernate)
- **Containerization:** Docker
- **Version Control:** GitHub
``

### ** Access Services**
- API Gateway: `http://localhost:8080`
- Order Service: `http://localhost:8082`
- Product Service: `http://localhost:8083`
- Payment Service: `http://localhost:8084`
- Eureka Service Registry: `http://localhost:8761`

## 🔗 API Workflow Example (Order Processing)
1. **User logs in** via Okta Auth.
2. **Places an order** via API Gateway.
3. **Order Service** checks product availability.
4. **Payment Service** processes payment.
5. **Order is stored** in Order DB & confirmed to the user.


---

**🚀 Happy Coding!** 😊
