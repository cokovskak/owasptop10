package com.example.owasp.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "grades")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Grade {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String courseName;

    @Column(nullable = false)
    private String gradeValue; // e.g., "A", "B+", "75", etc.

    @ManyToOne
    @JoinColumn(name = "student_id", nullable = false)
    private User student;
}
