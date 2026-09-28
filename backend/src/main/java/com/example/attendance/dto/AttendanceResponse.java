package com.example.attendance.dto;
import com.example.attendance.entity.AttendanceStatus;
import java.time.*;
public record AttendanceResponse(Long id, Long studentId, String studentCode, String studentName,
                                 LocalDate attendanceDate, AttendanceStatus status,
                                 String markedBy, LocalDateTime createdAt, LocalDateTime updatedAt) {}
