package com.example.attendance.controller;

import com.example.attendance.dto.*;
import com.example.attendance.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.time.Year;
import java.util.*;

@RestController @RequestMapping("/api/reports") @RequiredArgsConstructor
public class ReportController {
    private final ReportService reports;

    @GetMapping("/monthly")
    public List<ReportRow> monthly(@RequestParam int year,@RequestParam int month,
                                   @RequestParam(required=false) String studentId,
                                   @RequestParam(required=false) String department) {
        return reports.monthly(year,month,studentId,department);
    }

    @GetMapping("/yearly")
    public List<ReportRow> yearly(@RequestParam int year,
                                  @RequestParam(required=false) String studentId,
                                  @RequestParam(required=false) String department) {
        return reports.yearly(year, studentId, department);
    }

    @GetMapping("/summary") public SummaryResponse summary(){ return reports.summary(); }
}
