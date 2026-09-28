package com.example.attendance.service;

import com.example.attendance.dto.*;
import com.example.attendance.entity.*;
import com.example.attendance.exception.ApiException;
import com.example.attendance.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
@RequiredArgsConstructor
public class StudentService {
    private final StudentRepository students;
    private final UserRepository users;
    private final PasswordEncoder encoder;

    private StudentResponse out(Student s) {
        return new StudentResponse(s.getId(),s.getStudentId(),s.getName(),s.getEmail(),s.getPhone(),
                s.getDepartment(),s.getCourse(),s.getYear(),s.getStatus(),s.getCreatedAt(),s.getUpdatedAt());
    }

    public List<StudentResponse> all(String q) {
        return (q == null || q.isBlank() ? students.findAll() : students.search(q))
                .stream().map(this::out).toList();
    }

    public StudentResponse get(Long id) {
        return out(students.findById(id).orElseThrow(() -> new ApiException("Student not found", HttpStatus.NOT_FOUND)));
    }

    public StudentResponse create(StudentRequest r) {
        if (students.existsByStudentId(r.studentId())) throw new ApiException("Duplicate student ID", HttpStatus.CONFLICT);
        if (students.existsByEmail(r.email())) throw new ApiException("Duplicate student email", HttpStatus.CONFLICT);
        String username = r.email();
        if (users.existsByUsername(username)) throw new ApiException("A user already exists for this email", HttpStatus.CONFLICT);
        if (r.password() == null || r.password().isBlank()) throw new ApiException("Password is required for a new student", HttpStatus.BAD_REQUEST);

        User u = users.save(User.builder().username(username).password(encoder.encode(r.password())).role(Role.STUDENT).build());
        Student s = Student.builder().studentId(r.studentId()).name(r.name()).email(r.email()).phone(r.phone())
                .department(r.department()).course(r.course()).year(r.year()).status(r.status()==null?true:r.status()).user(u).build();
        return out(students.save(s));
    }

    public StudentResponse update(Long id, StudentRequest r) {
        Student s = students.findById(id).orElseThrow(() -> new ApiException("Student not found", HttpStatus.NOT_FOUND));
        if (!s.getStudentId().equals(r.studentId()) && students.existsByStudentId(r.studentId()))
            throw new ApiException("Duplicate student ID", HttpStatus.CONFLICT);
        if (!s.getEmail().equalsIgnoreCase(r.email()) && students.existsByEmail(r.email()))
            throw new ApiException("Duplicate student email", HttpStatus.CONFLICT);
        s.setStudentId(r.studentId()); s.setName(r.name()); s.setEmail(r.email()); s.setPhone(r.phone());
        s.setDepartment(r.department()); s.setCourse(r.course()); s.setYear(r.year());
        if (r.status()!=null) s.setStatus(r.status());
        s.getUser().setUsername(r.email());
        if (r.password()!=null && !r.password().isBlank()) s.getUser().setPassword(encoder.encode(r.password()));
        users.save(s.getUser());
        return out(students.save(s));
    }

    public void deactivate(Long id) {
        Student s = students.findById(id).orElseThrow(() -> new ApiException("Student not found", HttpStatus.NOT_FOUND));
        s.setStatus(false);
        students.save(s);
    }
}
