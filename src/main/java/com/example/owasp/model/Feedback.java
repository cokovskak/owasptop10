package com.example.owasp.model;


import jakarta.persistence.Entity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "feedback")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Feedback {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // The user who submitted the feedback
    @Column(nullable = false)
    private String username;

    // The actual feedback content
    @Column(nullable = false, length = 2000)
    private String message;

    // Timestamp for when the feedback was submitted
    @Column(nullable = true)
    private LocalDateTime submittedAt;

}
