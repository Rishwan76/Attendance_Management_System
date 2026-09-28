package com.example.attendance.service;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.example.attendance.dto.AttendanceRequest;
import com.example.attendance.dto.AttendanceResponse;
import com.example.attendance.entity.Attendance;
import com.example.attendance.entity.Student;
import com.example.attendance.entity.User;
import com.example.attendance.exception.ApiException;
import com.example.attendance.repository.AttendanceRepository;
import com.example.attendance.repository.StudentRepository;
import com.example.attendance.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AttendanceService {

    private final AttendanceRepository attendance;
    private final StudentRepository students;
    private final UserRepository users;

    private AttendanceResponse out(Attendance a) {
        return new AttendanceResponse(
                a.getId(),
                a.getStudent().getId(),
                a.getStudent().getStudentId(),
                a.getStudent().getName(),
                a.getAttendanceDate(),
                a.getStatus(),
                a.getMarkedBy().getUsername(),
                a.getCreatedAt(),
                a.getUpdatedAt()
        );
    }

    public List<AttendanceResponse> date(LocalDate date) {

        return attendance
                .findByAttendanceDateOrderByStudent_Name(date)
                .stream()
                .map(this::out)
                .toList();
    }

    public List<AttendanceResponse> student(
            Long studentId,
            LocalDate from,
            LocalDate to
    ) {

        students.findById(studentId)
                .orElseThrow(() ->
                        new ApiException(
                                "Student not found",
                                HttpStatus.NOT_FOUND
                        )
                );

        List<Attendance> list =
                (from != null && to != null)
                        ? attendance
                            .findByStudentIdAndAttendanceDateBetweenOrderByAttendanceDateDesc(
                                    studentId,
                                    from,
                                    to
                            )
                        : attendance
                            .findByStudentIdOrderByAttendanceDateDesc(studentId);

        return list.stream()
                .map(this::out)
                .toList();
    }

    public AttendanceResponse create(
            AttendanceRequest r,
            String username
    ) {

        Student s = students.findById(r.studentId())
                .orElseThrow(() ->
                        new ApiException(
                                "Student not found",
                                HttpStatus.NOT_FOUND
                        )
                );

        if (!s.getStatus()) {
            throw new ApiException(
                    "Student is inactive",
                    HttpStatus.BAD_REQUEST
            );
        }

        if (r.attendanceDate().isAfter(LocalDate.now())) {
            throw new ApiException(
                    "Attendance date cannot be in the future",
                    HttpStatus.BAD_REQUEST
            );
        }

        if (attendance
                .findByStudentIdAndAttendanceDate(
                        s.getId(),
                        r.attendanceDate()
                )
                .isPresent()) {

            throw new ApiException(
                    "Attendance already exists for this student and date",
                    HttpStatus.CONFLICT
            );
        }

        User marker = users.findByUsername(username)
                .orElseThrow();

        Attendance a = Attendance.builder()
                .student(s)
                .attendanceDate(r.attendanceDate())
                .status(r.status())
                .markedBy(marker)
                .build();

        return out(attendance.save(a));
    }

    public AttendanceResponse update(
            Long id,
            AttendanceRequest r,
            String username
    ) {

        Attendance a = attendance.findById(id)
                .orElseThrow(() ->
                        new ApiException(
                                "Attendance record not found",
                                HttpStatus.NOT_FOUND
                        )
                );

        if (r.attendanceDate().isAfter(LocalDate.now())) {
            throw new ApiException(
                    "Attendance date cannot be in the future",
                    HttpStatus.BAD_REQUEST
            );
        }

        var duplicate =
                attendance.findByStudentIdAndAttendanceDate(
                        r.studentId(),
                        r.attendanceDate()
                );

        if (duplicate.isPresent()
                && !duplicate.get().getId().equals(id)) {

            throw new ApiException(
                    "Duplicate attendance for this student and date",
                    HttpStatus.CONFLICT
            );
        }

        Student s = students.findById(r.studentId())
                .orElseThrow(() ->
                        new ApiException(
                                "Student not found",
                                HttpStatus.NOT_FOUND
                        )
                );

        a.setStudent(s);
        a.setAttendanceDate(r.attendanceDate());
        a.setStatus(r.status());

        a.setMarkedBy(
                users.findByUsername(username)
                        .orElseThrow()
        );

        return out(attendance.save(a));
    }

    public List<AttendanceResponse> history() {

        return attendance.findAll()
                .stream()
                .sorted(
                        Comparator.comparing(
                                Attendance::getAttendanceDate
                        ).reversed()
                )
                .map(this::out)
                .toList();
    }

    public List<Attendance> range(
            LocalDate from,
            LocalDate to
    ) {

        return attendance.findByAttendanceDateBetween(
                from,
                to
        );
    }
}