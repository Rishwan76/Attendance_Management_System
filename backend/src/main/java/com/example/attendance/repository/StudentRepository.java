package com.example.attendance.repository;
import com.example.attendance.entity.Student;
import org.springframework.data.jpa.repository.*;
import java.util.*;
public interface StudentRepository extends JpaRepository<Student, Long> {
    Optional<Student> findByStudentId(String studentId);
    Optional<Student> findByUserId(Long userId);
    Optional<Student> findByUserUsername(String username);
    boolean existsByStudentId(String studentId);
    boolean existsByEmail(String email);
    @Query("select s from Student s where lower(s.studentId) like lower(concat('%', :q, '%')) or lower(s.name) like lower(concat('%', :q, '%')) or lower(s.email) like lower(concat('%', :q, '%'))")
    List<Student> search(String q);
}
