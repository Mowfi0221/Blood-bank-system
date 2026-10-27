# Blood Bank Management System (BBMS)

> **Web Technology and Application Development — Academic Project**  
> *Theme: Modern Healthcare Red & Black | Full-Stack: Spring Boot + React + MongoDB*

---

## 📌 Project Overview

The **Blood Bank Management System** is a full-stack, browser-based web application engineered to bridge the critical gap between voluntary blood donors and hospitals, healthcare centers, and emergency seekers.

### Key Highlights:
- **Zero Swing / Pure Web Application**: Responsive modern React.js frontend connected to a RESTful Spring Boot backend.
- **Design System**: Professional healthcare aesthetic featuring a dark hero/navigation palette, bold crimson accents (`#E63946`), light-gray content workspaces, rounded modern cards, responsive data tables, and dynamic statistics charts.
- **Security**: BCrypt password hashing, stateless JWT authentication, role-based authorization (`ROLE_DONOR`, `ROLE_ADMIN`), and attempt-limited, 5-minute expiring OTP password recovery.
- **Database**: MongoDB database (`blood_db`) with indexed collections for donors, admins, and OTP verification records.

---

## 🏗️ System Architecture

```
┌─────────────────────────────────────────────────────────┐
│                    REACT FRONTEND (Vite)                │
│    Landing Page | Public Search | Donor Dashboard       │
│     Admin Portal | Registration | Password Recovery     │
└───────────────────────────┬─────────────────────────────┘
                            │ HTTP REST / JSON (Axios)
                            │ Bearer JWT Authentication
┌───────────────────────────▼─────────────────────────────┐
│                 JAVA SPRING BOOT BACKEND                │
│   Controllers | Services | Repositories | Spring Sec    │
│    JWT Auth Filter | BCrypt Encoder | JavaMailSender    │
└───────────────────────────┬─────────────────────────────┘
                            │ Spring Data MongoDB
┌───────────────────────────▼─────────────────────────────┐
│                   MONGODB DATABASE (blood_db)           │
│   Collections: donors | admin | password_reset_otps     │
└─────────────────────────────────────────────────────────┘
```

---

## 💻 Technology Stack

| Component | Technology | Version | Description |
| :--- | :--- | :--- | :--- |
| **Frontend** | React.js (Vite) | React 18+ | Fast Single Page Application (SPA) |
| **Routing** | React Router | 7.x | Declarative client-side routing & route guards |
| **Icons** | Lucide React | Latest | Modern medical and interface icons |
| **HTTP Client** | Axios | 1.x | REST API calls with JWT request/response interceptors |
| **Backend** | Java Spring Boot | 3.3.4 | RESTful service architecture |
| **Language** | Java JDK | 21 LTS | Modern LTS Java runtime |
| **Database** | MongoDB Community | 8.x / 7.x | NoSQL document database (`blood_db`) |
| **ORM/ODM** | Spring Data MongoDB | 4.3.x | Reactive repository interfaces and queries |
| **Security** | Spring Security 6 | BCrypt + JWT | Stateless Bearer token security & role guards |
| **Build Tool** | Apache Maven | 3.9+ / mvnw | Backend dependency management & packaging |
| **API Testing** | Postman | Latest | Complete collection JSON provided |

---

## 🗄️ MongoDB Database Specifications

**Database Name**: `blood_db`  
**Connection URI**: `mongodb://localhost:27017/blood_db`

### Collections:

1. **`donors`**:
   - `_id`: Unique ObjectId string
   - `name`: Donor full name
   - `age`: Donor age (18 - 100)
   - `blood_group`: Blood group (`A+`, `A-`, `B+`, `B-`, `O+`, `O-`, `AB+`, `AB-`)
   - `city`: Donor location
   - `phone`: Unique 10-digit mobile number (Used as login username)
   - `email`: Unique email address (Used for OTP verification)
   - `password_hash`: BCrypt hashed password (Never exposed in responses)
   - `availability`: "YES" or "NO" (Defaults to "YES")
   - `created_at`: Registration timestamp
   - `updated_at`: Last update timestamp

2. **`admin`**:
   - `_id`: Unique ObjectId string
   - `username`: Administrator username (Unique, default seeded: `admin`)
   - `password_hash`: BCrypt hashed password (Default seeded: `admin123`)
   - `created_at`: Account creation timestamp

3. **`password_reset_otps`**:
   - `_id`: Unique ObjectId string
   - `email`: Target donor email
   - `phone`: Target donor phone
   - `otp`: 6-digit random verification code
   - `expires_at`: Expiration timestamp (strictly 5 minutes from creation)
   - `attempts`: Failed attempt counter (max 3 allowed)
   - `verified`: Boolean status flag
   - `created_at`: Generation timestamp

---

## 🚀 Beginner-Friendly Setup Instructions for Windows

### Prerequisites
Make sure you have the following installed on your Windows machine:
1. **Java JDK 21**: Verify with `java -version`
2. **Node.js (v18+) & npm**: Verify with `node -v` and `npm -v`
3. **MongoDB Community Server**: Running on `localhost:27017`

---

### Step 1: Verify / Start MongoDB Community Server

MongoDB Community Server runs automatically as a Windows service.

1. Open PowerShell as Administrator and run:
   ```powershell
   Get-Service -Name "*mongo*"
   ```
2. If it is stopped, start the service:
   ```powershell
   Start-Service MongoDB
   ```
3. Test connectivity to port 27017:
   ```powershell
   Test-NetConnection -ComputerName localhost -Port 27017
   ```
   *Expected output: `TcpTestSucceeded : True`*

---

### Step 2: Configure & Run the Spring Boot Backend

The backend directory contains the complete Spring Boot project and Maven wrapper.

1. Open **Terminal 1 (Backend)**:
   ```powershell
   cd "c:\Users\Monika\Downloads\blood-bank-management-system\backend"
   ```

2. Compile the backend:
   ```powershell
   .\mvnw.cmd compile
   ```

3. Start the Spring Boot Application:
   ```powershell
   .\mvnw.cmd spring-boot:run
   ```

4. You will see the startup banner:
   ```text
   Tomcat started on port 8080 (http) with context path '/'
   Started BloodBankApplication in 5.52 seconds
   >>> Initialized default Administrator account: [username: admin]
   >>> Successfully seeded 8 initial donor records covering all blood groups!
   ```

---

### Step 3: Configure & Run the React Frontend

1. Open **Terminal 2 (Frontend)**:
   ```powershell
   cd "c:\Users\Monika\Downloads\blood-bank-management-system\frontend"
   ```

2. Dependencies are already installed. If needed on a fresh machine, install dependencies:
   ```powershell
   npm install
   ```

3. Launch the Vite development server:
   ```powershell
   npm run dev
   ```

4. Open your browser and navigate to:
   ```
   http://localhost:5173
   ```

---

## 🔑 Default Credentials for Evaluation

| Role | Username / Identifier | Password | Access Route |
| :--- | :--- | :--- | :--- |
| **Administrator** | `admin` | `admin123` | `http://localhost:5173/admin/login` |
| **Seeded Donor 1** | `9876543210` (Rahul Sharma, O+) | `password123` | `http://localhost:5173/login` |
| **Seeded Donor 2** | `9876543211` (Priya V, A+) | `password123` | `http://localhost:5173/login` |
| **Seeded Donor 3** | `9876543212` (Ananya R, B+) | `password123` | `http://localhost:5173/login` |
| **Seeded Donor 4** | `9876543213` (Karthik S, AB+) | `password123` | `http://localhost:5173/login` |

---

## 📧 Email SMTP Configuration for OTP Delivery

The system features real SMTP email delivery with an intelligent development simulation fallback:

1. **Development / Presentation Mode (Default)**:
   - When no external SMTP credentials are provided, OTPs are safely generated, validated for 5 minutes with attempt limits, and printed to the server terminal log (`[DEV MODE EMAIL SIMULATION] OTP for donor@example.com is: XXXXXX`).
   - This ensures examiners can test the entire forgot-password flow without internet/2FA hurdles!

2. **Real Gmail / Mailtrap SMTP Mode**:
   - In `backend/src/main/resources/application.properties`, configure your credentials:
     ```properties
     spring.mail.host=smtp.gmail.com
     spring.mail.port=587
     spring.mail.username=your-email@gmail.com
     spring.mail.password=your-16-digit-app-password
     ```
   - Alternatively, pass environment variables without changing source code:
     ```powershell
     $env:SPRING_MAIL_USERNAME="your-email@gmail.com"
     $env:SPRING_MAIL_PASSWORD="your-app-password"
     ```

---

## 🧪 Postman API Testing Guide

A complete, pre-configured collection is included:  
`Blood_Bank_Management_System.postman_collection.json`

### How to Import & Run in Postman:
1. Open Postman.
2. Click **Import** (top left).
3. Select `Blood_Bank_Management_System.postman_collection.json` from the project root.
4. The collection contains 4 folders:
   - **Public APIs**:
     - `GET /api/donors/search` (View all available donors)
     - `GET /api/donors/search?bloodGroup=O%2B&city=Chennai` (Filtered search)
     - `POST /api/donors/register` (Register a new donor)
   - **Authentication APIs**:
     - `POST /api/auth/donor/login` (Automatically extracts `donorToken`)
     - `POST /api/auth/admin/login` (Automatically extracts `adminToken`)
     - `POST /api/auth/forgot-password` (Generate OTP)
     - `POST /api/auth/verify-otp` (Verify 6-digit OTP)
     - `POST /api/auth/reset-password` (Reset password with OTP)
   - **Donor Profile APIs (Protected)**:
     - `GET /api/donors/me` (Profile view with Bearer JWT)
     - `PUT /api/donors/me` (Update name, age, city, email)
     - `PATCH /api/donors/me/availability` (Toggle YES / NO)
   - **Admin APIs (Protected)**:
     - `GET /api/admin/donors` (List all registered donors)
     - `GET /api/admin/donors/count` (Get total count)
     - `GET /api/admin/donors/statistics` (Blood group inventory and metrics)
     - `DELETE /api/admin/donors/:id` (Delete donor from MongoDB)

---

## ⚡ Automated Test Suite Execution

You can run our automated test suite anytime from the terminal:
```powershell
node test_system_apis.js
```
*Current Result: 37 / 37 Tests Passing (100% Success).*

---

## 🔍 Verifying Donor Records in MongoDB Compass

1. Open **MongoDB Compass**.
2. Click **Connect** with default connection string: `mongodb://localhost:27017`.
3. In the left database list, click `blood_db`.
4. Inspect the 3 collections:
   - `donors`: View registered donor documents, age, blood group, availability, and BCrypt encrypted `password_hash`.
   - `admin`: View the administrator record with hashed password.
   - `password_reset_otps`: View generated OTPs with expiration timestamps and attempt counters.

---

## 🛠️ Troubleshooting Guide

1. **Port 8080 already in use**:
   ```powershell
   Get-Process -Name "java" | Stop-Process -Force
   ```
2. **MongoDB Connection Refused**:
   - Ensure the MongoDB service is running: `Start-Service MongoDB`.
3. **Frontend CORS issue**:
   - Spring Boot has CORS enabled for all origins (`*`) and methods (`GET, POST, PUT, PATCH, DELETE, OPTIONS`).
   - Vite proxy is also configured in `vite.config.js`.

---

## 📜 Project Deliverables Summary

- **Frontend**: Responsive React 18 SPA (`/frontend`)
- **Backend**: Java Spring Boot 3.3.4 RESTful API (`/backend`)
- **MongoDB**: `blood_db` with unique indexes on phone/email
- **Security**: BCrypt encryption + JWT authorization + role filtering
- **Testing**: 37 Automated tests + Postman Collection
- **Motto**: *Every Drop Counts. Every Donor Matters.*
