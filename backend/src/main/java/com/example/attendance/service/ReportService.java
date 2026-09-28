package com.example.attendance.service;

import com.example.attendance.dto.*;
import com.example.attendance.entity.*;
import com.example.attendance.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.*;
import java.time.temporal.TemporalAdjusters;
import java.util.*;

@Service
@RequiredArgsConstructor
public class ReportService {
    private final StudentRepository students;
    private final AttendanceRepository attendance;

    public List<ReportRow> monthly(int year, int month, String studentId, String department) {
        LocalDate from=LocalDate.of(year,month,1), to=from.with(TemporalAdjusters.lastDayOfMonth());
        Map<Long,List<Attendance>> grouped=new HashMap<>();
        attendance.findByAttendanceDateBetween(from,to).forEach(a -> grouped.computeIfAbsent(a.getStudent().getId(), k->new ArrayList<>()).add(a));
        return students.findAll().stream()
            .filter(s -> studentId==null || studentId.isBlank() || s.getStudentId().equalsIgnoreCase(studentId))
            .filter(s -> department==null || department.isBlank() || department.equalsIgnoreCase(s.getDepartment()))
            .filter(Student::getStatus)
            .map(s -> {
                List<Attendance> rows=grouped.getOrDefault(s.getId(),List.of());
                long present=rows.stream().filter(a->a.getStatus()==AttendanceStatus.PRESENT).count();
                long absent=rows.stream().filter(a->a.getStatus()==AttendanceStatus.ABSENT).count();
                long working=present+absent;
                double pct=working==0?0:Math.round((present*10000.0/working))/100.0;
                return new ReportRow(s.getStudentId(),s.getName(),s.getDepartment(),working,present,absent,pct);
            }).toList();
    }


    public List<ReportRow> yearly(int year, String studentId, String department) {
        LocalDate from=LocalDate.of(year,1,1), to=LocalDate.of(year,12,31);
        Map<Long,List<Attendance>> grouped=new HashMap<>();
        attendance.findByAttendanceDateBetween(from,to).forEach(a -> grouped.computeIfAbsent(a.getStudent().getId(), k->new ArrayList<>()).add(a));
        return students.findAll().stream()
            .filter(s -> studentId==null || studentId.isBlank() || s.getStudentId().equalsIgnoreCase(studentId))
            .filter(s -> department==null || department.isBlank() || department.equalsIgnoreCase(s.getDepartment()))
            .filter(Student::getStatus)
            .map(s -> {
                List<Attendance> rows=grouped.getOrDefault(s.getId(),List.of());
                long present=rows.stream().filter(a->a.getStatus()==AttendanceStatus.PRESENT).count();
                long absent=rows.stream().filter(a->a.getStatus()==AttendanceStatus.ABSENT).count();
                long working=present+absent;
                double pct=working==0?0:Math.round((present*10000.0/working))/100.0;
                return new ReportRow(s.getStudentId(),s.getName(),s.getDepartment(),working,present,absent,pct);
            }).toList();
    }

    public SummaryResponse summary() {
        LocalDate today=LocalDate.now();
        long total=students.findAll().stream().filter(Student::getStatus).count();
        List<Attendance> rows=attendance.findByAttendanceDateBetween(today,today);
        long present=rows.stream().filter(a->a.getStatus()==AttendanceStatus.PRESENT).count();
        long absent=rows.stream().filter(a->a.getStatus()==AttendanceStatus.ABSENT).count();
        double pct=(present+absent)==0?0:Math.round(present*10000.0/(present+absent))/100.0;
        return new SummaryResponse(total,present,absent,pct);
    }
}
