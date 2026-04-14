# Freelance Pay Gap Detector - Postman API Testing Guide

All endpoints assume your Spring Boot server is running on `http://localhost:8081` (Check application.properties if it's 8080 or 8081).
Make sure to set the Body type in Postman to `raw` and select `JSON`.

---

## 1. User Endpoints

### Register a User
- **Method**: POST
- **URL**: `http://localhost:8081/api/users/register`
- **Body**:
```json
{
    "name": "Jane Doe",
    "email": "jane.doe@example.com",
    "password": "SecurePassword123",
    "gender": "Female"
}
```

### Login a User
- **Method**: POST
- **URL**: `http://localhost:8081/api/users/login`
- **Body**:
```json
{
    "email": "jane.doe@example.com",
    "password": "SecurePassword123"
}
```
*(Save the returned `userId` for the next steps!)*

---

## 2. Project Endpoints

### Create a Project
*(Assuming you manually seeded a Client with ID 1, or just testing limits)*
- **Method**: POST
- **URL**: `http://localhost:8081/api/projects`
- **Body**:
```json
{
    "clientId": 1,
    "userId": 1,
    "title": "E-Commerce Website Redesign",
    "description": "Full UI/UX redesign of the main catalog",
    "projectType": "Software Engineer",
    "agreedAmount": 5000.00,
    "agreedHours": 100.0,
    "deadline": "2024-12-01",
    "status": "IN_PROGRESS"
}
```

### Get Projects by User
- **Method**: GET
- **URL**: `http://localhost:8081/api/projects/user/1`

### Update Project Status
- **Method**: PATCH
- **URL**: `http://localhost:8081/api/projects/1/status`
- **Body**:
```json
{
    "status": "COMPLETED"
}
```

---

## 3. Payment Endpoints

### Log a Payment
- **Method**: POST
- **URL**: `http://localhost:8081/api/payments`
- **Body**:
```json
{
    "projectId": 1,
    "amountReceived": 2500.00,
    "paymentDate": "2024-11-15",
    "dueDate": "2024-11-01",
    "paymentStatus": "PARTIAL"
}
```

### Get Total Received for Project
- **Method**: GET
- **URL**: `http://localhost:8081/api/payments/project/1/total`

---

## 4. Work Log Endpoints

### Record Daily Work & Revisions
- **Method**: POST
- **URL**: `http://localhost:8081/api/worklogs`
- **Body**:
{
    "projectId": 1,
    "workDate": "2024-11-05",
    "hoursWorked": 12.5,
    "revisionsDone": 2,
    "notes": "Client requested massive button color changes."
}

### Get Work Totals for Project
- **Method**: GET
- **URL**: `http://localhost:8081/api/worklogs/project/1/totals`

---

## 5. Core Analysis Endpoints

### Trigger Exploitation Analysis
- **Method**: POST
- **URL**: `http://localhost:8081/api/analyze/1`
- **Response Example**:
```json
{
    "riskId": 1,
    "projectId": 1,
    "delayScore": 4.66,
    "underpaymentScore": 8.5,
    "revisionScore": 2.0,
    "unpaidHoursScore": 0.0,
    "totalRiskScore": 5.09,
    "riskLevel": "HIGH"
}
```

---

## 6. Gender Pay Gap Analytics

### Get Gap Statistics
- **Method**: GET
- **URL**: `http://localhost:8081/api/analytics/gender-gap?projectType=Software%20Engineer`
