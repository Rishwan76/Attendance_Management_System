package com.example.attendance.controller;

import com.example.attendance.dto.*;
import com.example.attendance.service.StudentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController @RequestMapping("/api/students") @RequiredArgsConstructor
public class StudentController {
    private final StudentService service;

    @GetMapping
    public List<StudentResponse> all(@RequestParam(required=false) String q){ return service.all(q); }

    @GetMapping("/{id}") public StudentResponse get(@PathVariable Long id){ return service.get(id); }

    @PostMapping public StudentResponse create(@Valid @RequestBody StudentRequest r){ return service.create(r); }
    @PutMapping("/{id}") public StudentResponse update(@PathVariable Long id,@Valid @RequestBody StudentRequest r){ return service.update(id,r); }
    @DeleteMapping("/{id}") public void deactivate(@PathVariable Long id){ service.deactivate(id); }
}
