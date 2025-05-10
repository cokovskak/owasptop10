package com.example.owasp.repository;

import com.example.owasp.model.Grade;
import com.example.owasp.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GradeRepository extends JpaRepository<Grade, Long> {
    List<Grade> findByStudent(User student);

}
