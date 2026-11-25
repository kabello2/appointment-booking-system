# Appointment Booking System

A full-stack appointment booking platform that allows customers to register, authenticate, book appointments, manage appointments, receive notifications, and interact with branch consultants.

The system consists of:

- Angular Frontend
- Spring Boot Backend
- PostgreSQL Database
- Docker & Docker Compose

---

# Technology Stack

## Frontend

- Angular 20
- Angular Material
- TypeScript
- Nginx

## Backend

- Java 21
- Spring Boot
- Spring Security
- JWT Authentication
- Spring Data JPA
- Flyway
- PostgreSQL

## Infrastructure

- Docker
- Docker Compose

---

# Prerequisites

The only required software is:

- Docker Desktop

Verify installation:

```bash
docker --version
```

```bash
docker compose version
```

---

# Clone Repository

```bash
git clone https://github.com/kabello2/appointment-booking-system
```

```bash
cd appointment-booking-system
```

---

# Environment Configuration

Copy:

```bash
cp .env.example .env
```

Or manually create:

```text
.env
```

Example:

```env
POSTGRES_DB=appointment_booking

POSTGRES_USER=admin

POSTGRES_PASSWORD=ChangeMe123!

JWT_SECRET_KEY=GENERATE_A_RANDOM_BASE64_SECRET

BOOTSTRAP_ADMIN_EMAIL=admin@appointment.co.za

BOOTSTRAP_ADMIN_PASSWORD=Admin@123!
```

---

# Start The Entire System

Run:

```bash
docker compose up --build
```

This command starts:

```text
PostgreSQL
↓
Spring Boot API
↓
Angular Application
```

No additional setup is required.

---

# Access The Application

## Frontend

```text
http://localhost:4200
```

## Swagger Documentation

```text
http://localhost:8088/swagger-ui/index.html
```

## OpenAPI Specification

```text
http://localhost:8088/v3/api-docs
```

---

# Initial Administrator

If no administrator exists, the backend automatically creates one during startup using the values supplied in:

```env
BOOTSTRAP_ADMIN_EMAIL
BOOTSTRAP_ADMIN_PASSWORD
```

Example:

```text
Email:
admin@appointment.co.za

Password:
Admin@123!
```

---

# Application Walkthrough

## 1. Login As Administrator

Open:

```text
http://localhost:4200
```

Login using:

```text
admin@appointment.co.za
Admin@123!
```

---

## 2. Create Consultant

Navigate:

```text
Administration
→ Users
→ Create Consultant
```

Assign the consultant to a branch.

Create at least one consultant.

---

## 3. Register Customer

Logout.

Navigate:

```text
Register
```

Create a customer account.

Example:

```text
john.doe@mail.com
```

---

## 4. Login As Customer

Navigate:

```text
Login
```

Login using the newly registered credentials.

---

## 5. Book Appointment

Navigate:

```text
Appointments
→ Book Appointment
```

Select:

- Branch
- Service
- Date
- Available Time Slot

Submit the booking.

Expected result:

```text
Appointment Status = BOOKED
```

A consultant is allocated automatically.

---

## 6. View Appointments

Navigate:

```text
My Appointments
```

Verify the appointment appears.

---

## 7. Cancel Appointment

Select an appointment.

Click:

```text
Cancel
```

Expected result:

```text
Appointment Status = CANCELLED
```

---

## 8. View Notifications

Login as administrator.

Navigate:

```text
Notifications
```

Verify notification entries were generated.

Use the:

```text
View
```

action to inspect full notification details.

---

# Database

PostgreSQL runs inside Docker.

The following gets executed automatically automatically:

- Creates the schema using Flyway
- Applies database migrations
- Creates required indexes and constraints
- Creates the bootstrap administrator when needed

---

# Security

Implemented:

- JWT Authentication
- Role-Based Access Control
- Account Lockout Protection
- Login Rate Limiting
- Ownership Enforcement
- Appointment Status Validation
- Consultant Allocation
- PostgreSQL Concurrency Protection

---

# Architecture

```text
┌───────────────────────┐
│ Angular Frontend      │
│ localhost:4200        │
└───────────┬───────────┘
            │
            │ /api/*
            ▼
┌───────────────────────┐
│ Spring Boot API       │        │
└───────────┬───────────┘
            │
            ▼
┌───────────────────────┐
│ PostgreSQL            │
└───────────────────────┘
```

The frontend communicates with the backend through an Nginx reverse proxy and does not expose backend hostnames internally.

---

# Docker Services

View running services:

```bash
docker ps
```

Expected:

```text
appointment-postgres

appointment-booking-api

appointment-booking-ui
```

---

# View Logs

All services:

```bash
docker compose logs -f
```

Backend only:

```bash
docker compose logs -f appointment-booking-api
```

Frontend only:

```bash
docker compose logs -f appointment-booking-ui
```

Database only:

```bash
docker compose logs -f appointment-postgres
```

---

# Stop Application

```bash
docker compose down
```

---

# Full Cleanup

Removes containers and database data:

```bash
docker compose down -v
```

---
