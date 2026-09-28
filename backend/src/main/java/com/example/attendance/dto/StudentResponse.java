package com.example.attendance.dto;
import java.time.LocalDateTime;
public record StudentResponse(Long id, String studentId, String name, String email, String phone,
                              String department, String course, Integer year, Boolean status,
                              LocalDateTime createdAt, LocalDateTime updatedAt) {}
