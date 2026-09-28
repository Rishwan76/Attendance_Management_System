package com.example.attendance.controller;

import com.example.attendance.dto.AttendanceResponse;
import com.example.attendance.repository.StudentRepository;
import com.example.attendance.service.AttendanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.List;

@RestController @RequestMapping("/api/me/attendance") @RequiredArgsConstructor
public class MyAttendanceController {
    private final StudentRepository students;
    private final AttendanceService attendance;

    @GetMapping
    public List<AttendanceResponse> mine(Authentication auth,
                                         @RequestParam(required=false) LocalDate from,
                                         @RequestParam(required=false) LocalDate to) {
        Long id = students.findByUserUsername(auth.getName()).orElseThrow().getId();
        return attendance.student(id,from,to);
    }
}
