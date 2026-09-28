package com.example.attendance.controller;

import com.example.attendance.dto.StudentResponse;
import com.example.attendance.repository.StudentRepository;
import com.example.attendance.service.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController @RequestMapping("/api/me") @RequiredArgsConstructor
public class MeController {
    private final StudentRepository students;
    private final StudentService service;

    @GetMapping("/student")
    public StudentResponse student(Authentication auth) {
        return service.get(students.findByUserUsername(auth.getName()).orElseThrow().getId());
    }
}
