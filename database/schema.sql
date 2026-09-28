CREATE DATABASE IF NOT EXISTS attendance_db;
USE attendance_db;

-- The Spring Boot application can create/update these tables automatically using JPA.
-- This SQL is provided for reference and for manual database creation.

CREATE TABLE IF NOT EXISTS users (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(20) NOT NULL,
    created_at DATETIME NOT NULL,
    updated_at DATETIME NOT NULL
);

CREATE TABLE IF NOT EXISTS students (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    student_id VARCHAR(30) NOT NULL UNIQUE,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    phone VARCHAR(20),
    department VARCHAR(100),
    course VARCHAR(100),
    study_year INT,
    status BOOLEAN NOT NULL DEFAULT TRUE,
    user_id BIGINT NOT NULL UNIQUE,
    created_at DATETIME NOT NULL,
    updated_at DATETIME NOT NULL,
    CONSTRAINT fk_student_user FOREIGN KEY (user_id) REFERENCES users(id)
);

CREATE TABLE IF NOT EXISTS attendance (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    student_id BIGINT NOT NULL,
    attendance_date DATE NOT NULL,
    status VARCHAR(10) NOT NULL,
    marked_by BIGINT NOT NULL,
    created_at DATETIME NOT NULL,
    updated_at DATETIME NOT NULL,
    CONSTRAINT uk_student_date UNIQUE(student_id, attendance_date),
    CONSTRAINT fk_att_student FOREIGN KEY(student_id) REFERENCES students(id),
    CONSTRAINT fk_att_marker FOREIGN KEY(marked_by) REFERENCES users(id)
);

-- select * from users;
-- select * from students;
-- select * from attendance;
