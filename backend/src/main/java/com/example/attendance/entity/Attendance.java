package com.example.attendance.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name="attendance",
       uniqueConstraints=@UniqueConstraint(name="uk_student_date",
                                           columnNames={"student_id","attendance_date"}))
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Attendance {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch=FetchType.LAZY, optional=false)
    @JoinColumn(name="student_id", nullable=false)
    private Student student;

    @Column(name="attendance_date", nullable=false)
    private LocalDate attendanceDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable=false, length=10)
    private AttendanceStatus status;

    @ManyToOne(fetch=FetchType.LAZY, optional=false)
    @JoinColumn(name="marked_by", nullable=false)
    private User markedBy;

    @Column(name="created_at", nullable=false)
    private LocalDateTime createdAt;

    @Column(name="updated_at", nullable=false)
    private LocalDateTime updatedAt;

    @PrePersist
    void prePersist() {
        createdAt = LocalDateTime.now();
        updatedAt = createdAt;
    }

    @PreUpdate
    void preUpdate() { updatedAt = LocalDateTime.now(); }
}
