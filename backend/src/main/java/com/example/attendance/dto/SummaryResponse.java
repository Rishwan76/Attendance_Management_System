package com.example.attendance.dto;
public record SummaryResponse(long totalStudents, long presentToday, long absentToday, double todayPercentage) {}
