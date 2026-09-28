package com.example.attendance.config;

import java.time.LocalDate;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.example.attendance.entity.Attendance;
import com.example.attendance.entity.AttendanceStatus;
import com.example.attendance.entity.Role;
import com.example.attendance.entity.Student;
import com.example.attendance.entity.User;
import com.example.attendance.repository.AttendanceRepository;
import com.example.attendance.repository.StudentRepository;
import com.example.attendance.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class DataInitializer {

    private final UserRepository users;
    private final StudentRepository students;
    private final AttendanceRepository attendance;
    private final PasswordEncoder encoder;

    @Bean
    CommandLineRunner seed() {

        return args -> {

            // =========================
            // CREATE ADMIN
            // =========================

            User admin = users.findByUsername("admin")
                    .orElseGet(() ->
                            users.save(
                                    User.builder()
                                            .username("admin")
                                            .password(encoder.encode("Admin@123"))
                                            .role(Role.ADMIN)
                                            .build()
                            )
                    );


            // =========================
            // CREATE STAFF
            // =========================

            User staff = users.findByUsername("staff")
                    .orElseGet(() ->
                            users.save(
                                    User.builder()
                                            .username("staff")
                                            .password(encoder.encode("Staff@123"))
                                            .role(Role.STAFF)
                                            .build()
                            )
                    );


            // =========================
            // CREATE SAMPLE STUDENTS
            // =========================

            if (students.count() == 0) {

                // Student 1 User
                User u1 = users.save(
                        User.builder()
                                .username("student1@example.com")
                                .password(encoder.encode("Student@123"))
                                .role(Role.STUDENT)
                                .build()
                );

                // Student 2 User
                User u2 = users.save(
                        User.builder()
                                .username("student2@example.com")
                                .password(encoder.encode("Student@123"))
                                .role(Role.STUDENT)
                                .build()
                );


                // =========================
                // STUDENT 1
                // =========================

                Student s1 = students.save(
                        Student.builder()
                                .studentId("ST001")
                                .name("John Doe")
                                .email("student1@example.com")
                                .phone("9876543210")
                                .department("Computer Science")
                                .course("B.E. CSE")
                                .year(4)
                                .status(true)
                                .user(u1)
                                .build()
                );


                // =========================
                // STUDENT 2
                // =========================

                Student s2 = students.save(
                        Student.builder()
                                .studentId("ST002")
                                .name("Jane Smith")
                                .email("student2@example.com")
                                .phone("9876543211")
                                .department("Information Technology")
                                .course("B.Tech IT")
                                .year(3)
                                .status(true)
                                .user(u2)
                                .build()
                );


                // =========================
                // SAMPLE ATTENDANCE
                // =========================

                LocalDate today = LocalDate.now();


                // Student 1 - Today - Present
                attendance.save(
                        Attendance.builder()
                                .student(s1)
                                .attendanceDate(today)
                                .status(AttendanceStatus.PRESENT)
                                .markedBy(admin)
                                .build()
                );


                // Student 2 - Today - Absent
                attendance.save(
                        Attendance.builder()
                                .student(s2)
                                .attendanceDate(today)
                                .status(AttendanceStatus.ABSENT)
                                .markedBy(admin)
                                .build()
                );


                // Student 1 - Yesterday - Present
                attendance.save(
                        Attendance.builder()
                                .student(s1)
                                .attendanceDate(today.minusDays(1))
                                .status(AttendanceStatus.PRESENT)
                                .markedBy(staff)
                                .build()
                );


                // Student 2 - Yesterday - Present
                attendance.save(
                        Attendance.builder()
                                .student(s2)
                                .attendanceDate(today.minusDays(1))
                                .status(AttendanceStatus.PRESENT)
                                .markedBy(staff)
                                .build()
                );
            }
        };
    }
}