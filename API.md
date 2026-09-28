# API Documentation

Base URL: `http://localhost:8080/api`

Authenticated endpoints require:

`Authorization: Bearer <JWT>`

## POST /auth/login

Request:
```json
{"username":"admin","password":"Admin@123"}
```

Response:
```json
{
  "token":"<jwt>",
  "username":"admin",
  "role":"ADMIN",
  "userId":1,
  "studentDbId":null
}
```

## POST /students

ADMIN/STAFF only.

```json
{
  "studentId":"ST003",
  "name":"Alice",
  "email":"alice@example.com",
  "phone":"9999999999",
  "department":"Computer Science",
  "course":"B.E. CSE",
  "year":4,
  "status":true,
  "password":"Student@123"
}
```

## POST /attendance

ADMIN/STAFF only.

```json
{
  "studentId":1,
  "attendanceDate":"2026-08-14",
  "status":"PRESENT"
}
```

## PUT /attendance/{id}

ADMIN/STAFF only.

```json
{
  "studentId":1,
  "attendanceDate":"2026-08-14",
  "status":"ABSENT"
}
```

## GET /reports/monthly

Query:
- `year`
- `month`
- optional `studentId`
- optional `department`

Example:

`GET /reports/monthly?year=2026&month=8`

Response:
```json
[
  {
    "studentId":"ST001",
    "studentName":"John Doe",
    "department":"Computer Science",
    "workingDays":25,
    "present":22,
    "absent":3,
    "percentage":88.0
  }
]
```

## Common errors

400 — validation/business rule error

401 — missing, invalid, or expired JWT

403 — authenticated user lacks required role

404 — resource not found

409 — duplicate student ID/email or duplicate attendance

500 — unexpected server error
