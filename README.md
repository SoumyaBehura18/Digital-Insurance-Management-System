# 🛡️ InsureCore

> A comprehensive digital insurance management system designed to simplify policy management for both customers and insurance providers.

[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-6DB33F?style=for-the-badge&logo=spring-boot&logoColor=white)](https://spring.io/projects/spring-boot)
[![Vue.js](https://img.shields.io/badge/Vue.js-35495E?style=for-the-badge&logo=vue.js&logoColor=4FC08D)](https://vuejs.org/)
[![PostgreSQL](https://img.shields.io/badge/PostgreSQL-316192?style=for-the-badge&logo=postgresql&logoColor=white)](https://www.postgresql.org/)
[![Tailwind CSS](https://img.shields.io/badge/Tailwind_CSS-38B2AC?style=for-the-badge&logo=tailwind-css&logoColor=white)](https://tailwindcss.com/)
[![JWT](https://img.shields.io/badge/JWT-000000?style=for-the-badge&logo=JSON%20web%20tokens&logoColor=white)](https://jwt.io/)

---

## 📋 Table of Contents

- [🎯 Overview](#overview)
- [✨ Features](#features)
- [🛠️ Technologies Used](#technologies-used)
- [📐 Class Diagram](#class-diagram)
- [🏛️ System Architecture](#system-architecture)
- [🗃️ Database Schema](#database-schema)
- [🚀 Getting Started](#getting-started)
- [📚 API Documentation](#api-documentation)
- [👥 Team](#team)
- [🔮 Future Enhancements](#future-enhancements)
- [🤝 Contributing](#contributing)
- [✉️ Contact](#contact)

---

## 🎯 Overview

InsureCore is a comprehensive digital insurance management system that provides seamless end-to-end functionality including user registration, policy browsing and purchase, claims management, and support ticket resolution. Built with modern technologies and best practices, it offers a robust solution for insurance providers and their customers.

---

## ✨ Features

### 👥 For Customers

- 🔐 **Secure Authentication** - JWT-based registration and login
- 🔍 **Policy Discovery** - Browse insurance products across Life, Health, and Vehicle domains
- 💳 **Policy Management** - Purchase, renew, and cancel policies with ease
- ⏰ **Real-time Tracking** - Monitor policy validity and expiration dates
- 📝 **Claims Processing** - Submit and track insurance claims
- 🎫 **Support System** - Create and monitor support tickets

### 🔧 For Administrators

- 📊 **Management Dashboards** - Comprehensive views for users and policies
- ✅ **Claims Approval** - Review and approve/reject insurance claims
- 📈 **Policy Monitoring** - Track policy statuses and renewals
- 🎟️ **Ticket Management** - Manage and resolve support tickets
- 👤 **User Oversight** - Comprehensive user account management

---

## 🛠️ Technologies Used

### Frontend

| Technology | Description | Badge |
|------------|-------------|-------|
| **Vue.js 3** | Reactive, modular UI components (Composition API) | ![Vue.js](https://img.shields.io/badge/Vue.js-35495E?style=flat-square&logo=vue.js&logoColor=4FC08D) |
| **Vuex** | Centralized state management | ![Vuex](https://img.shields.io/badge/Vuex-35495E?style=flat-square&logo=vue.js&logoColor=4FC08D) |
| **Vue Router** | Navigation and route guards | ![Vue Router](https://img.shields.io/badge/Vue_Router-35495E?style=flat-square&logo=vue.js&logoColor=4FC08D) |
| **Tailwind CSS** | Modern, utility-first styling | ![Tailwind](https://img.shields.io/badge/Tailwind_CSS-38B2AC?style=flat-square&logo=tailwind-css&logoColor=white) |
| **Lucide Vue Next** | Lightweight icon library | ![Icons](https://img.shields.io/badge/Lucide-F56565?style=flat-square&logo=lucide&logoColor=white) |
| **Axios** | HTTP client for API communication | ![Axios](https://img.shields.io/badge/Axios-5A29E4?style=flat-square&logo=axios&logoColor=white) |

### Backend

| Technology | Description | Badge |
|------------|-------------|-------|
| **Spring Boot** | RESTful API framework | ![Spring Boot](https://img.shields.io/badge/Spring_Boot-6DB33F?style=flat-square&logo=spring-boot&logoColor=white) |
| **Spring Security** | Authentication & authorization | ![Spring Security](https://img.shields.io/badge/Spring_Security-6DB33F?style=flat-square&logo=spring-security&logoColor=white) |
| **JWT** | Token-based authentication | ![JWT](https://img.shields.io/badge/JWT-000000?style=flat-square&logo=JSON%20web%20tokens&logoColor=white) |
| **Hibernate (JPA)** | ORM & persistence layer | ![Hibernate](https://img.shields.io/badge/Hibernate-59666C?style=flat-square&logo=hibernate&logoColor=white) |

### Database & Storage

| Technology | Description | Badge |
|------------|-------------|-------|
| **PostgreSQL** | Primary relational database | ![PostgreSQL](https://img.shields.io/badge/PostgreSQL-316192?style=flat-square&logo=postgresql&logoColor=white) |
| **Supabase** | Managed Postgres service | ![Supabase](https://img.shields.io/badge/Supabase-3ECF8E?style=flat-square&logo=supabase&logoColor=white) |

### Testing

| Technology | Description | Badge |
|------------|-------------|-------|
| **JUnit** | Backend unit testing | ![JUnit](https://img.shields.io/badge/JUnit-25A162?style=flat-square&logo=junit5&logoColor=white) |
| **Mockito** | Mocking dependencies | ![Mockito](https://img.shields.io/badge/Mockito-C5D9C8?style=flat-square) |
| **Vitest** | Frontend unit testing | ![Vitest](https://img.shields.io/badge/Vitest-6E9F18?style=flat-square&logo=vitest&logoColor=white) |
| **Vue Test Utils** | Vue component testing | ![Vue Test Utils](https://img.shields.io/badge/Vue_Test_Utils-35495E?style=flat-square&logo=vue.js&logoColor=4FC08D) |

### Build Tools

| Technology | Description | Badge |
|------------|-------------|-------|
| **Maven** | Backend dependency management | ![Maven](https://img.shields.io/badge/Maven-C71A36?style=flat-square&logo=apache-maven&logoColor=white) |
| **npm** | Frontend dependency management | ![npm](https://img.shields.io/badge/npm-CB3837?style=flat-square&logo=npm&logoColor=white) |

---

## 📐 Class Diagram

<img width="1920" height="1610" alt="InsureCore Class Diagram" src="https://github.com/user-attachments/assets/7d29a19f-dc45-49bc-bbcf-50e355c4149e" />

### 📘 Class Diagram Overview

This class diagram illustrates the object-oriented architecture of the insurance management system. It captures the relationships between core components such as users, policies, claims, and support workflows. The design supports multiple insurance types and integrates premium calculations, customer service interactions, and policy tracking.

**Key Highlights:**
- 🏗️ Modular architecture with clear separation of concerns
- 🔄 Flexible relationships supporting multiple insurance types
- 📊 Scalable design suitable for enterprise-level applications
- 🎯 Domain-driven design principles

---

## 🏛️ System Architecture

<img width="1958" height="1198" alt="InsureCore System Architecture" src="https://github.com/user-attachments/assets/1dd28ece-f45f-4b3f-8396-7f0615c5d58c" />


**Architecture Highlights:**
- 🎨 **Presentation Layer**: Vue.js 3 SPA with reactive components
- ⚙️ **Business Logic Layer**: Spring Boot RESTful services
- 🗄️ **Data Layer**: PostgreSQL with Hibernate ORM
- 🔒 **Security**: JWT-based authentication with Spring Security
- ☁️ **Cloud Storage**: Supabase for file uploads and management

---

## 🗃️ Database Schema

<img width="1881" height="981" alt="InsureCore Database Schema" src="https://github.com/user-attachments/assets/c89b9350-4fd8-4694-a0f3-07275866f5fc" />

### Core Entities

| Entity | Icon | Description |
|--------|------|-------------|
| **User** | 👤 | Customer, Admin, and Agent accounts |
| **Policy** | 📋 | Master policy definitions (Health, Life, Vehicle) |
| **UserPolicy** | 🔗 | Junction table linking users to purchased policies |
| **Claim** | 📝 | Insurance claim requests and approvals |
| **SupportTicket** | 🎫 | Customer support and queries |
| **Message** | 💬 | Ticket communication thread |


---

## 🚀 Getting Started

### Prerequisites

Before you begin, ensure you have the following installed:

| Tool | Version | Icon |
|------|---------|------|
| Java | 17+ | ☕ |
| Node.js | 18+ | 🟢 |
| PostgreSQL | 13+ | 🐘 |
| Maven | 3.8+ | 📦 |
| npm | Latest | 📮 |

### Installation

#### Clone the Repository

```bash
git clone https://github.com/SoumyaBehura18/Digital-Insurance-Management-System
cd Digital-Insurance-Management-System
```
---

#### Backend Setup

```bash
# Navigate to backend directory
cd backend

# Update database credentials
nano src/main/resources/application.properties
```

Backend runs at http://localhost:9090

 ---

### Frontend Setup
```
# Navigate to frontend directory
cd frontend

# Install dependencies
npm install

# Run development server
npm run serve
```

 Frontend runs at http://localhost:8080

 ---

### Database Setup
```
-- Create database
CREATE DATABASE insurecore;
```
---

## Update src/main/resources/application.properties:
```
#server properties/configuration
server.port=9090

# PostgreSQL Configuration
spring.datasource.url=jdbc:postgresql://localhost:5432/insurecore
spring.datasource.username=your_username
spring.datasource.password=your_password
spring.datasource.driver-class-name=org.postgresql.Driver

# JPA & Hibernate
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.database-platform=org.hibernate.dialect.PostgreSQLDialect

# JWT Secret Key
spring.app.jwtSecret = you-jwt--secret-key

# Supabase Configuration
supabase.url=https://exhhnhdfkmwxluwhvyvq.supabase.co
supabase.anon.key=eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6ImV4aGhuaGRma213eGx1d2h2eXZxIiwicm9sZSI6ImFub24iLCJpYXQiOjE3NTkxNDA5NTEsImV4cCI6MjA3NDcxNjk1MX0.Vf4aUCUDj1F-grXYztUrUFn4t-sgAupuFBwRybLDhBw
supabase.bucket.name=insurance-management-system
```
---

### Environment Variables

## Backend (.env)
```
DB_URL=jdbc:postgresql://localhost:5432/insurecore
DB_USER=your_username
DB_PASS=your_password
JWT_SECRET=your-secret-key
SERVER_PORT=9090
```
---

## Frontend (.env)
```
VUE_APP_API_BASE_URL=http://localhost:9090
```
---

### Project Structure Frontend
```
markdown## Project Structure

### Frontend Structure
frontend/
├── public/
│   ├── favicon.ico
│   ├── icon.png
│   └── index.html
│
├── src/
│   ├── assets/                    # Static assets (images, fonts, etc.)
│   │
│   ├── components/                # Reusable Vue components
│   ├── router/                    # Vue Router configuration
│   │   └── index.js
│   │
│   ├── store/                     # Vuex store modules
│   │   ├── modules/
│   │   └── index.js
│   │
│   ├── utils/                     # Utility functions
│   │   ├── api.js
│   │   └── auth.js
│   │
│   ├── views/                     # Page-level components
│   │   ├── Admin/
│   │   ├── User/
│   │   ├── LandingPage.vue
│   │   ├── LoginPage.vue
│   │   ├── RegisterPage.vue
│   │   └── NotFound.vue
│   │
│   ├── App.vue                    # Root component
│   └── main.js                    # Application entry point
│
├── tests/    
│
├── .eslintrc.js                   # ESLint configuration
├── .gitignore                     # Git ignore rules
├── babel.config.js                # Babel configuration
├── jsconfig.json                  # JavaScript configuration
├── package-lock.json              # npm lock file
├── package.json                   # npm dependencies
├── postcss.config.js              # PostCSS configuration
├── tailwind.config.js             # Tailwind CSS configuration
├── vitest.config.js               # Vitest configuration
└── vue.config.js                  # Vue CLI configuration

```
---

### Backend Jar File
```
```
---

### Testing
## Backend Tests
```
cd backend
./mvnw test
```
---

### Frontend Tests
```
cd frontend
npx vitest
```
---

#### Deployment
## Build Backend
```
bashcd backend
./mvnw clean package -DskipTests
```
 Output: target/digital-insurance-management-system-0.0.1-SNAPSHOT.jar

## Build Frontend
```
bashcd frontend
npm run serve
```
 Output: Static files in /dist

### Production Deployment

- Backend: Deploy JAR to cloud platforms (AWS, GCP, Azure)
- Frontend: Serve via Nginx or deploy to Vercel/Netlify
- Database: Use managed PostgreSQL (AWS RDS, GCP Cloud SQL, Azure)

---

 
## 📚 API Documentation

### Base URL
```
http://localhost:9090
```

### Authentication Endpoints

| Method | Endpoint | Description | Icon |
|--------|----------|-------------|------|
| POST | `/auth/register` | Register new user | 📝 |
| POST | `/auth/login` | User login | 🔐 |
| POST | `/auth/refresh` | Refresh JWT token | 🔄 |

### Policy Endpoints

| Method | Endpoint | Description | Icon |
|--------|----------|-------------|------|
| GET | `/policies` | Get all policies | 📋 |
| GET | `/policies/{id}` | Get policy by ID | 🔍 |
| POST | `/policies` | Create new policy (Admin) | ➕ |
| PUT | `/policies/{id}` | Update policy (Admin) | ✏️ |
| DELETE | `/policies/{id}` | Delete policy (Admin) | 🗑️ |

### User Policy Endpoints

| Method | Endpoint | Description | Icon |
|--------|----------|-------------|------|
| GET | `/user-policies` | Get user's policies | 📑 |
| POST | `/user-policies/purchase` | Purchase policy | 💳 |
| PUT | `/user-policies/{id}/renew` | Renew policy | 🔄 |
| DELETE | `/user-policies/{id}/cancel` | Cancel policy | ❌ |

### Claims Endpoints

| Method | Endpoint | Description | Icon |
|--------|----------|-------------|------|
| GET | `/claims` | Get all claims | 📝 |
| GET | `/claims/{id}` | Get claim by ID | 🔍 |
| POST | `/claims` | Submit new claim | ➕ |
| PUT | `/claims/{id}/approve` | Approve claim (Admin) | ✅ |
| PUT | `/claims/{id}/reject` | Reject claim (Admin) | ❌ |

### Support Ticket Endpoints

| Method | Endpoint | Description | Icon |
|--------|----------|-------------|------|
| GET | `/tickets` | Get all tickets | 🎫 |
| GET | `/tickets/{id}` | Get ticket by ID | 🔍 |
| POST | `/tickets` | Create ticket | ➕ |
| PUT | `/tickets/{id}/close` | Close ticket | ✅ |
| POST | `/tickets/{id}/messages` | Add message | 💬 |

---

## 👥 Team

| Name | Role | Responsibilities |
|------|------|-----------------|
| **Soumya Behura** | Authentication & Admin Management | User registration/login, JWT authentication, Admin policy management, Role-based access control |
| **Deep Parekh** | Policy Management & Dashboards | Policy catalog, Purchase/renewal/cancellation, User & admin dashboards, Policy lifecycle management |
| **SK Hussain** | Claims Management | Claims submission & tracking, Approval workflow, Admin claims dashboard, Claim validation |
| **Parth Verma** | Support & Ticketing System | Support ticket creation, User query management, Admin ticket dashboard, Issue resolution |

---

## 🔮 Future Enhancements

### 📱 User Experience
- 📱 Mobile app development for Android and iOS
- 🌐 Multi-language support for wider accessibility
- ⚡ Progressive Web App (PWA) for offline access
- 🌙 Dark mode and accessibility improvements

### 📋 Policy Management
- 🤖 AI-driven policy recommendations based on user profile
- 🔁 Automated policy renewal with payment integration
- 🎁 Bundled insurance packages (Health + Life + Vehicle)
- 🧮 Premium calculator with real-time quotes

### 🧾 Claims Processing
- 📄 OCR integration for automatic document reading
- 🕵️ AI-based fraud detection using ML models
- ✍️ Digital signature support for secure approvals
- 🎥 Video claim verification
- 💸 Instant claim settlement for minor amounts

### 💬 Support & Communication
- 🤖 AI chatbot for instant support and FAQs
- 📲 Multi-channel support (Email, SMS, WhatsApp)
- 🗂️ Automated ticket routing and agent assignment
- ⭐ Customer feedback and rating system
- 🧑‍💼 Live chat with agents

### 🔐 Security & Compliance
- 🔒 Two-factor authentication (2FA)
- 🧬 Biometric authentication
- 🧑‍⚖️ Enhanced role management (Agent, Manager, Partner)
- 📜 Audit logs and compliance reporting
- 🛡️ GDPR and data privacy compliance

### 📊 Analytics & Reporting
- 📈 Advanced analytics dashboard
- 🔮 Predictive analytics for risk assessment
- 💼 Financial reports and policy insights
- 🧠 Customer behavior analytics
- 🧾 Automated report generation (PDF, Excel)

### 🔌 Integration
- 💳 Payment gateway integration (Stripe, Razorpay, PayPal)
- 🔗 Third-party API integrations
- 📧 Email service providers (SendGrid, AWS SES)
- 📲 SMS notification services
- ☁️ Cloud storage integration (AWS S3, Google Cloud Storage)---

---

## 🤝 Contributing

We welcome contributions! Please follow these steps:

1. 🍴 Fork the repository
2. 🌿 Create a feature branch (`git checkout -b feature/AmazingFeature`)
3. 💾 Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. 📤 Push to the branch (`git push origin feature/AmazingFeature`)
5. 🔁 Open a Pull Request

---

## ✉️ Contact
For questions or support, please open an issue in this repository.

---
