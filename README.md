# Attendance Management System

Full-stack responsive attendance management application.

## Stack

- Frontend: React 19, Vite, React Router, Axios, Recharts, Lucide React
- Backend: Java 17, Spring Boot 3.5, Spring Web, Spring Data JPA, Spring Security, JWT
- Database: MySQL 8+
- Build: Maven / npm

## Features

### Admin / Staff
- JWT login
- Dashboard statistics and charts
- Student CRUD/deactivation
- Search students
- Daily attendance marking and updating
- Attendance history by date
- Monthly and yearly reports
- Attendance percentage and low-attendance highlighting
- CSV export
- Profile/logout

### Student
- JWT login
- Personal dashboard
- Personal attendance statistics
- Personal attendance history and month filter
- Students cannot access admin APIs or another student's attendance

## Default demo accounts

| Role | Username | Password |
|---|---|---|
| Admin | admin | Admin@123 |
| Staff | staff | Staff@123 |
| Student | student1@example.com | Student@123 |
| Student | student2@example.com | Student@123 |

Change these credentials before production.

## 1. Database

Install MySQL 8+ and create the database:

```sql
CREATE DATABASE attendance_db;
```

Or run `database/schema.sql`.

The application uses `spring.jpa.hibernate.ddl-auto=update`, so JPA creates/updates tables automatically.

## 2. Backend

Set `DB_USERNAME`, `DB_PASSWORD`, and `APP_JWT_SECRET` in your shell or IDE run configuration. `DB_PASSWORD` has no default; do not commit credentials.

Then:

```bash
cd backend
mvn clean spring-boot:run
```

Backend:

`http://localhost:8080`

API base:

`http://localhost:8080/api`

## 3. Frontend

Open another terminal:

```bash
cd frontend
npm install
npm run dev
```

Frontend:

`http://localhost:5173`

Optional environment file:

`frontend/.env`

```env
VITE_API_URL=http://localhost:8080/api
```

## API summary

### Authentication
- `POST /api/auth/login`

### Students — ADMIN/STAFF
- `GET /api/students?q=`
- `GET /api/students/{id}`
- `POST /api/students`
- `PUT /api/students/{id}`
- `DELETE /api/students/{id}`

### Attendance — ADMIN/STAFF
- `GET /api/attendance`
- `GET /api/attendance/date/{yyyy-MM-dd}`
- `GET /api/attendance/student/{studentDbId}`
- `POST /api/attendance`
- `PUT /api/attendance/{id}`

### Reports — ADMIN/STAFF
- `GET /api/reports/monthly?year=2026&month=8`
- `GET /api/reports/yearly?year=2026`
- `GET /api/reports/monthly?year=2026&month=8&studentId=ST001&department=Computer%20Science`
- `GET /api/reports/summary`

### Student-only
- `GET /api/me/student`
- `GET /api/me/attendance`
- `GET /api/me/attendance?from=2026-08-01&to=2026-08-31`

## Attendance rules

- Only ADMIN/STAFF can create/update attendance.
- Future attendance dates are rejected.
- A database unique constraint enforces one attendance record per student/date.
- Inactive students cannot receive new attendance.
- Student endpoints derive the student from the authenticated JWT username, so a student cannot choose another student ID.

## Security notes

For production:
1. Replace `app.jwt.secret`.
2. Use environment variables/secrets for database credentials and JWT secret.
3. Use HTTPS.
4. Configure CORS to the actual frontend domain.
5. Add refresh-token rotation if long-lived sessions are needed.
6. Add audit/event logging for administrative changes.
7. Consider method-level authorization and rate limiting.
8. Disable demo seed credentials.
9. Use `ddl-auto=validate` with controlled database migrations.
10. Add automated tests and CI/CD.

## Important implementation note

The example treats working days as attendance records that exist in the selected period. If your institution defines working days independently of attendance (for example, excluding weekends, holidays, exams, or approved leave), create a calendar/holiday table and calculate working days from that source.
