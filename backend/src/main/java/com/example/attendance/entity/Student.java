package com.example.attendance.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name="students")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Student {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    @Column(name="student_id", nullable=false, unique=true, length=30)
    private String studentId;

    @Column(nullable=false, length=100)
    private String name;

    @Column(nullable=false, unique=true, length=100)
    private String email;

    @Column(length=20)
    private String phone;

    @Column(length=100)
    private String department;

    @Column(length=100)
    private String course;

    @Column(name="study_year")
    private Integer year;

    @Column(nullable=false)
    private Boolean status = true;

    @OneToOne(fetch=FetchType.LAZY, optional=false)
    @JoinColumn(name="user_id", nullable=false, unique=true)
    private User user;

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
