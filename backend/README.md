# Appointment Booking System

A Spring Boot based appointment booking platform that allows customers to register, authenticate, book appointments, and manage appointments with branch consultants.

---

# Technology Stack

## Backend

- Java 21
- Spring Boot
- Spring Security (JWT)
- Spring Data JPA
- PostgreSQL
- Flyway
- MapStruct
- Lombok

## Infrastructure

- Docker
- Docker Compose

## Documentation

- OpenAPI / Swagger

---

# Prerequisites

Install the following:

- Java 21
- Docker Desktop
- Git

Verify installation:

```bash
java --version
```

```bash
docker --version
```

```bash
docker compose version
```

---

# Clone Repository

```bash
git clone <repository-url>
```

```bash
cd appointment-booking
```

---

# Environment Configuration

Create a file named:

```text
.env
```

Example:

```env
POSTGRES_DB=appointment_booking

POSTGRES_USER=db_username

POSTGRES_PASSWORD=db_password

JWT_SECRET_KEY=<BASE64_SECRET>
```

Generate a JWT secret:

```bash
openssl rand -base64 64
```

Example:

```env
JWT_SECRET_KEY=fvYKFIBPSvG6wYr...
```

---

# Docker Configuration

The application consists of:

```text
appointment-postgres
appointment-booking-api
```

Example API port mapping:

```yaml
ports:
  - "8088:8080"
```

This means:

```text
Host Port      8088
Container Port 8080
```

---

# Starting the Application

Build and start all services:

```bash
docker compose up --build
```

Start in detached mode:

```bash
docker compose up -d --build
```

---

# Verify Running Containers

```bash
docker ps
```

Expected:

```text
appointment-postgres
appointment-booking-api
```

---

# View Application Logs

```bash
docker compose logs -f
```

or

```bash
docker compose logs -f appointment-booking-api
```

---

# API Documentation

Swagger UI:

```text
http://localhost:8088/swagger-ui/index.html
```

OpenAPI Specification:

```text
http://localhost:8088/v3/api-docs
```

---

# API Base URL

```text
http://localhost:8088/api/v1
```

All endpoints below are relative to this URL.

---

# Authentication Workflow

```text
Register
    ↓
Login
    ↓
Receive JWT
    ↓
Call secured endpoints
```

---

# Customer Journey Walk Through

The following section demonstrates a complete booking journey.

---

# Step 1: Register Customer

Endpoint:

```http
POST /api/v1/auth/register
```

Request:

```json
{
  "firstName": "John",
  "lastName": "Doe",
  "email": "john.doe@mail.com",
  "phoneNumber": "+27821234567",
  "password": "@Password2026"
}
```

cURL:

```bash
curl --location 'http://localhost:808/api/v1/auth/register' \
--header 'Content-Type: application/json' \
--data '{
    "firstName":"John",
    "lastName":"Doe",
    "email":"john.doe@mail.com",
    "phoneNumber":"+27821234567",
    "password":"@Password2026"
}'
```

Expected Response:

```http
201 Created
```

---

# Step 2: Login

Endpoint:

```http
POST /api/v1/auth/login
```

Request:

```json
{
  "email": "john.doe@mail.com",
  "password": "@Password2026"
}
```

cURL:

```bash
curl --location 'http://localhost:8081/api/v1/auth/login' \
--header 'Content-Type: application/json' \
--data '{
    "email":"john.doe@mail.com",
    "password":"@Password2026"
}'
```

Response:

```json
{
  "accessToken": "eyJhbGc...",
  "tokenType": "Bearer",
  "expiresIn": 3600
}
```

Copy:

```text
accessToken
```

All secured requests require:

```http
Authorization: Bearer <access-token>
```

---

# Step 3: Get Current User

Endpoint:

```http
GET /api/v1/users/current
```

Example:

```bash
curl --location 'http://localhost:8081/api/v1/users/current' \
--header 'Authorization: Bearer <token>'
```

Response:

```json
{
  "id": "f3c74...",
  "firstName": "John",
  "lastName": "Doe",
  "email": "john.doe@mail.com",
  "role": "ROLE_CUSTOMER"
}
```

---

# Step 4: Retrieve Branches

Endpoint:

```http
GET /api/v1/branches
```

Example:

```bash
curl --location 'http://localhost:8088/api/v1/branches' \
--header 'Authorization: Bearer <token>'
```

Response:

```json
[
  {
    "id": "branch-id",
    "name": "Sandton",
    "branchCode": "BR001"
  }
]
```

Copy the:

```text
branch id
```

---

# Step 5: Retrieve Services

Endpoint:

```http
GET /api/v1/services
```

Example:

```bash
curl --location 'http://localhost:8088/api/v1/services' \
--header 'Authorization: Bearer <token>'
```

Response:

```json
[
  {
    "id": "service-id",
    "name": "Home Loan Consultation"
  }
]
```

Copy the:

```text
service id
```

---

# Step 6: Retrieve Available Slots

Endpoint:

```http
GET /api/v1/appointments/available-slots
```

Example:

```bash
curl --location \
'http://localhost:8088/api/v1/appointments/available-slots?branchId=<branch-id>&appointmentDate=2026-12-01' \
--header 'Authorization: Bearer <token>'
```

Response:

```json
[
  {
    "startTime": "09:00",
    "endTime": "09:30"
  },
  {
    "startTime": "09:30",
    "endTime": "10:00"
  }
]
```

Copy one of the returned:

```text
startTime
```

values.

---

# Step 7: Book Appointment

Endpoint:

```http
POST /api/v1/appointments
```

Request:

```json
{
  "branchId": "branch-id",
  "serviceId": "service-id",
  "appointmentDate": "2026-12-01",
  "startTime": "09:00"
}
```

Example:

```bash
curl --location 'http://localhost:8088/api/v1/appointments' \
--header 'Authorization: Bearer <token>' \
--header 'Content-Type: application/json' \
--data '{
    "branchId":"branch-id",
    "serviceId":"service-id",
    "appointmentDate":"2026-12-01",
    "startTime":"09:00"
}'
```

Expected:

```http
201 Created
```

Response:

```json
{
  "id": "appointment-id",
  "appointmentNumber": "APT000001",
  "status": "BOOKED",
  "branchName": "Sandton",
  "serviceName": "Home Loan Consultation",
  "consultantName": "Consultant One"
}
```

Copy:

```text
appointment-id
```

---

# Step 8: View My Appointments

Endpoint:

```http
GET /api/v1/appointments
```

Example:

```bash
curl --location 'http://localhost:8088/api/v1/appointments' \
--header 'Authorization: Bearer <token>'
```

---

# Step 9: Cancel Appointment

Endpoint:

```http
PUT /api/v1/appointments/{appointmentId}/cancel
```

Example:

```bash
curl --location --request PUT \
'http://localhost:8088/api/v1/appointments/<appointment-id>/cancel' \
--header 'Authorization: Bearer <token>'
```

Expected Response:

```json
{
  "id": "appointment-id",
  "status": "CANCELLED"
}
```

---

# Database Access

Connect using:

| Property | Value |
|----------|--------|
| Host | localhost |
| Port | 5432 |
| Database | appointment_booking |
| Username | admin |
| Password | value from .env |

Use one of:

- DBeaver
- pgAdmin
- IntelliJ Database Tools

Useful queries:

```sql
SELECT * FROM users;
```

```sql
SELECT * FROM appointments;
```

```sql
SELECT * FROM branches;
```

```sql
SELECT * FROM service_offerings;
```

```sql
SELECT * FROM flyway_schema_history;
```

---

# Flyway

Verify migrations:

```sql
SELECT *
FROM flyway_schema_history
ORDER BY installed_rank;
```

---

# Running Tests

Run unit tests:

```bash
./mvnw test
```

Run all tests:

```bash
./mvnw verify
```

---

# Application Roles

Supported roles:

```text
ROLE_ADMIN
ROLE_CUSTOMER
ROLE_CONSULTANT
```

---

# Security Features

Implemented:

- JWT Authentication
- Role Based Access Control
- Account Lockout
- Login Rate Limiting
- Appointment Ownership Validation
- Consultant Allocation
- Consultant Double-Booking Prevention
- Customer Double-Booking Prevention
- Appointment State Transition Validation

---

# Troubleshooting

## Port Already In Use

Error:

```text
Only one usage of each socket address is normally permitted
```

Change:

```yaml
ports:
  - "8088:8080"
```

to another host port.

Example:

```yaml
ports:
  - "8082:8080"
```

---

## JWT Secret Missing

Error:

```text
JWT_SECRET_KEY is not configured
```

Verify:

```env
JWT_SECRET_KEY=<base64-secret>
```

exists in `.env`.

---

## PostgreSQL Health Check Failure

Use:

```yaml
healthcheck:
  test:
    [
      "CMD-SHELL",
      "pg_isready -U ${POSTGRES_USER} -d ${POSTGRES_DB}"
    ]
```

---

# Stopping the Application

```bash
docker compose down
```

Remove database volume:

```bash
docker compose down -v
```

This removes all database data and starts again from a clean database on the next startup.