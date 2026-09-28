package com.example.attendance.dto;
import jakarta.validation.constraints.*;
public record StudentRequest(
    @NotBlank String studentId,
    @NotBlank String name,
    @Email @NotBlank String email,
    String phone,
    String department,
    String course,
    @Min(1) @Max(8) Integer year,
    Boolean status,
    String password
) {}
