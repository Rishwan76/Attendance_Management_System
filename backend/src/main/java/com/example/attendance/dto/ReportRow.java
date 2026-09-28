package com.example.attendance.dto;
public record ReportRow(String studentId, String studentName, String department,
                        long workingDays, long present, long absent, double percentage) {}
