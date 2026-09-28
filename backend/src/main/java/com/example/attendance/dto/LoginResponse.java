package com.example.attendance.dto;
public record LoginResponse(String token, String username, String role, Long userId, Long studentDbId) {}
