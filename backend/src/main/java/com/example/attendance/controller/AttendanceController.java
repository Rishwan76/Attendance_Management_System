package com.example.attendance.controller;

import com.example.attendance.dto.*;
import com.example.attendance.service.AttendanceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.*;

@RestController @RequestMapping("/api/attendance") @RequiredArgsConstructor
public class AttendanceController {
    private final AttendanceService service;

    @GetMapping public List<AttendanceResponse> all(){ return service.history(); }

    @GetMapping("/date/{date}")
    public List<AttendanceResponse> byDate(@PathVariable LocalDate date){ return service.date(date); }

    @GetMapping("/student/{studentId}")
    public List<AttendanceResponse> byStudent(@PathVariable Long studentId,
        @RequestParam(required=false) LocalDate from, @RequestParam(required=false) LocalDate to) {
        return service.student(studentId,from,to);
    }

    @PostMapping
    public AttendanceResponse create(@Valid @RequestBody AttendanceRequest r, Authentication a){
        return service.create(r,a.getName());
    }

    @PutMapping("/{id}")
    public AttendanceResponse update(@PathVariable Long id,@Valid @RequestBody AttendanceRequest r,Authentication a){
        return service.update(id,r,a.getName());
    }
}
