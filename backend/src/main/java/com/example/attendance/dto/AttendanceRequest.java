package com.example.attendance.dto;
import com.example.attendance.entity.AttendanceStatus;
import jakarta.validation.constraints.*;
import java.time.LocalDate;
public record AttendanceRequest(@NotNull Long studentId, @NotNull LocalDate attendanceDate, @NotNull AttendanceStatus status) {}
