# 💼 Job Advertising System (Job Portal)

A full-stack **Job Advertising System** that connects **Job Seekers** and **Employers**, allowing employers to post jobs and job seekers to browse and apply securely.

This project is built as a **real-world, production-ready application** using **Spring Boot** for the backend and **React + Vite** for the frontend, with **JWT-based authentication** and **role-based access control**.

---

## 🚀 Features

### 🔐 Authentication & Authorization

* User registration and login
* JWT-based authentication
* Role-based authorization

  * `JOB_SEEKER`
  * `EMPLOYER`

### 👨‍💼 Employer Features

* Register and login as Employer
* Create job advertisements
* View jobs posted by the employer
* Update and delete own job posts

### 👩‍💻 Job Seeker Features

* Register and login as Job Seeker
* Browse available job advertisements
* View job details
* Apply for jobs (planned)

### 🛡️ Security

* Spring Security with JWT
* Protected API endpoints
* Secure password hashing (BCrypt)
* Role-restricted routes (backend & frontend)

---

## 🧱 Tech Stack

### Backend

* Java 21
* Spring Boot 4
* Spring Security
* JWT (JSON Web Token)
* Spring Data JPA
* PostgreSQL
* Maven

### Frontend

* React
* Vite
* Tailwind CSS
* Axios
* React Router DOM

---

## 📂 Project Structure

```
job_advertising_system/
│
├── backend/
│   ├── src/main/java/com/jobportal/
│   │   ├── auth/
│   │   ├── config/
│   │   ├── controller/
│   │   ├── entity/
│   │   ├── repository/
│   │   ├── security/
│   │   └── service/
│   └── src/main/resources/
│       └── application.yml
│
├── frontend/
│   ├── src/
│   │   ├── api/
│   │   ├── auth/
│   │   ├── components/
│   │   ├── pages/
│   │   ├── routes/
│   │   └── utils/
│   └── vite.config.js
│
└── README.md
```

---

## ⚙️ Backend Setup

### Prerequisites

* Java 21+
* Maven
* PostgreSQL

### Database Configuration

Create a PostgreSQL database:

```sql
CREATE DATABASE job_portal;
```

Update `application.yml`:

```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/job_portal
    username: postgres
    password: your_password
```

### Run Backend

```bash
cd backend
mvn spring-boot:run
```

Backend runs on:

```
http://localhost:8080
```

---

## 🎨 Frontend Setup

### Prerequisites

* Node.js 18+

### Install & Run

```bash
cd frontend
npm install
npm run dev
```

Frontend runs on:

```
http://localhost:5173
```

---

## 🔑 API Endpoints

### Authentication

| Method | Endpoint             | Description   |
| ------ | -------------------- | ------------- |
| POST   | `/api/auth/register` | Register user |
| POST   | `/api/auth/login`    | Login user    |

### Job Management

| Method | Endpoint         | Role     |
| ------ | ---------------- | -------- |
| POST   | `/api/jobs`      | EMPLOYER |
| GET    | `/api/jobs`      | ALL      |
| GET    | `/api/jobs/{id}` | ALL      |
| PUT    | `/api/jobs/{id}` | EMPLOYER |
| DELETE | `/api/jobs/{id}` | EMPLOYER |

---

## 🧭 Role-Based Routing (Frontend)

* Public routes: Login, Register
* Employer routes: Dashboard, Post Job
* Job Seeker routes: Job List, Job Details
* Protected routes handled via JWT and route guards

---

## 🧪 Testing

* Backend tested via Postman
* JWT validation tested for secured endpoints
* Role access verified for each API

---

## 📌 Planned Features

* Job application system
* Employer view of applicants
* Job seeker profile management
* Pagination & search
* Admin role
* Deployment (Docker + Cloud)

---