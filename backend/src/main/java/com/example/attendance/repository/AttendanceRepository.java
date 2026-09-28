package com.example.attendance.repository;

import com.example.attendance.entity.*;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import java.time.LocalDate;
import java.util.*;

public interface AttendanceRepository extends JpaRepository<Attendance, Long> {
    Optional<Attendance> findByStudentIdAndAttendanceDate(Long studentId, LocalDate date);
    List<Attendance> findByAttendanceDateOrderByStudent_Name(LocalDate date);
    List<Attendance> findByStudentIdOrderByAttendanceDateDesc(Long studentId);
    List<Attendance> findByStudentIdAndAttendanceDateBetweenOrderByAttendanceDateDesc(Long studentId, LocalDate from, LocalDate to);
    List<Attendance> findByAttendanceDateBetween(LocalDate from, LocalDate to);

    @Query("select a from Attendance a join fetch a.student where a.attendanceDate between :from and :to order by a.attendanceDate desc")
    List<Attendance> findReport(@Param("from") LocalDate from, @Param("to") LocalDate to);
}
