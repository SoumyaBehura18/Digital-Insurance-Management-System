# InsureCore - Digital Insurance Management System

[![Java](https://img.shields.io/badge/Java-17+-orange.svg)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.x-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![Vue.js](https://img.shields.io/badge/Vue.js-3.x-green.svg)](https://vuejs.org/)
[![PostgreSQL](https://img.shields.io/badge/PostgreSQL-13+-blue.svg)](https://www.postgresql.org/)
[![License](https://img.shields.io/badge/License-MIT-yellow.svg)](LICENSE)

## Overview

InsureCore is a comprehensive digital insurance management system designed to simplify policy management for both customers and insurance providers. The platform provides seamless end-to-end functionality including user registration, policy browsing and purchase, claims management, and support ticket resolution.

## Features

### For Customers
- Secure registration and JWT-based authentication
- Browse insurance products across Life, Health, and Vehicle domains
- Purchase, renew, and cancel policies
- Real-time policy validity tracking
- Submit and monitor insurance claims
- Create and track support tickets

### For Administrators
- Robust dashboards for user and policy management
- Approve or reject insurance claims
- Monitor policy statuses and renewals
- Manage and resolve support tickets
- Oversee user accounts

## Technologies Used

### Frontend
- **Vue.js 3** (Composition API) - Reactive, modular UI components
- **Vuex** - Centralized state management
- **Vue Router** - Navigation and route guards
- **Tailwind CSS** - Modern, utility-first styling
- **Lucide Vue Next** - Lightweight icons
- **Axios** - API communication

### Backend
- **Spring Boot** (Java) - RESTful APIs
- **Spring Security + JWT** - Authentication and authorization
- **Hibernate (JPA)** - ORM & persistence layer

### Database
- **PostgreSQL** - Primary relational database
- **Supabase** - Managed Postgres service

### Testing
- **JUnit** - Backend unit testing
- **Mockito** - Mocking dependencies
- **Vitest + Vue Test Utils** - Frontend testing

### Build Tools
- **Maven** - Backend dependency management
- **npm** - Frontend dependency management

## System Architecture

<img width="1920" height="1610" alt="image" src="https://github.com/user-attachments/assets/eeb75e8d-3154-4dc3-b6cf-9b3d6119463c" />

### Architecture Overview
- **Frontend**: Vue.js 3 SPA with Vuex state management
- **Backend**: Spring Boot REST APIs
- **Database**: PostgreSQL with Hibernate ORM
- **Authentication**: JWT-based with Spring Security
- **Storage**: Supabase for file uploads

## Database Schema


<img width="1881" height="981" alt="image" src="https://github.com/user-attachments/assets/c89b9350-4fd8-4694-a0f3-07275866f5fc" />


### Core Entities
- **User** - Customer, Admin, and Agent accounts
- **Policy** - Master policy definitions (Health, Life, Vehicle)
- **UserPolicy** - Junction table linking users to purchased policies
- **Claim** - Insurance claim requests and approvals
- **SupportTicket** - Customer support and queries
- **Message** - Ticket communication thread

## Getting Started

### Prerequisites
- Java 17+
- Node.js 18+
- PostgreSQL 13+
- Maven 3.8+
- npm

### Backend Setup
```bash
# Navigate to backend directory
cd backend

# Run the application
./mvnw spring-boot:run
```
 Backend runs at http://localhost:9090

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

### Database Setup
```
-- Create database
CREATE DATABASE insurecore;
```

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
spring.app.jwtSecret = akdkkdjdfkejkeieirirueueiekkekrujerkjkdfkdkjdjkdfkjafdjl

# Supabase Configuration
supabase.url=https://exhhnhdfkmwxluwhvyvq.supabase.co
supabase.anon.key=eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6ImV4aGhuaGRma213eGx1d2h2eXZxIiwicm9sZSI6ImFub24iLCJpYXQiOjE3NTkxNDA5NTEsImV4cCI6MjA3NDcxNjk1MX0.Vf4aUCUDj1F-grXYztUrUFn4t-sgAupuFBwRybLDhBw
supabase.bucket.name=insurance-management-system
```

### Environment Variables

## Backend (.env)
```
DB_URL=jdbc:postgresql://localhost:5432/insurecore
DB_USER=your_username
DB_PASS=your_password
JWT_SECRET=your-secret-key
SERVER_PORT=9090
```

## Frontend (.env)
```
VUE_APP_API_BASE_URL=http://localhost:9090
```

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

### Backend Jar File
```
```

### Testing
## Backend Tests
```
cd backend
./mvnw test
```

### Frontend Tests
```
cd frontend
npx vitest
```

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

## Team

| Name | Role | Responsibilities |
|------|------|-----------------|
| **Soumya Behura** | Authentication & Admin Management | User registration/login, JWT authentication, Admin policy management, Role-based access control |
| **Deep Parekh** | Policy Management & Dashboards | Policy catalog, Purchase/renewal/cancellation, User & admin dashboards, Policy lifecycle management |
| **SK Hussain** | Claims Management | Claims submission & tracking, Approval workflow, Admin claims dashboard, Claim validation |
| **Parth Verma** | Support & Ticketing System | Support ticket creation, User query management, Admin ticket dashboard, Issue resolution |

## Future Enhancements

### User Experience
- Mobile app development for Android and iOS
- Multi-language support for wider accessibility
- Progressive Web App (PWA) for offline access
- Dark mode and accessibility improvements

### Policy Management
- AI-driven policy recommendations based on user profile
- Automated policy renewal with payment integration
- Bundled insurance packages (Health + Life + Vehicle)
- Premium calculator with real-time quotes

### Claims Processing
- OCR integration for automatic document reading
- AI-based fraud detection using ML models
- Digital signature support for secure approvals
- Video claim verification
- Instant claim settlement for minor amounts

### Support & Communication
- AI chatbot for instant support and FAQs
- Multi-channel support (Email, SMS, WhatsApp)
- Automated ticket routing and agent assignment
- Customer feedback and rating system
- Live chat with agents

### Security & Compliance
- Two-factor authentication (2FA)
- Biometric authentication
- Enhanced role management (Agent, Manager, Partner)
- Audit logs and compliance reporting
- GDPR and data privacy compliance

### Analytics & Reporting
- Advanced analytics dashboard
- Predictive analytics for risk assessment
- Financial reports and policy insights
- Customer behavior analytics
- Automated report generation (PDF, Excel)

### Integration
- Payment gateway integration (Stripe, Razorpay, PayPal)
- Third-party API integrations
- Email service providers (SendGrid, AWS SES)
- SMS notification services
- Cloud storage integration (AWS S3, Google Cloud Storage)

## License

This project is licensed under the MIT License.

## Contact
For questions or support, please open an issue in this repository.
