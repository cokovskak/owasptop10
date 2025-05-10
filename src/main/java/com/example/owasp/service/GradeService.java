package com.example.owasp.service;

import com.example.owasp.model.Grade;
import com.example.owasp.model.User;
import com.example.owasp.repository.GradeRepository;
import com.example.owasp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GradeService {

    private final GradeRepository gradeRepository;
    private final UserRepository userRepository;

    public Grade saveGrade(Grade grade) {
        return gradeRepository.save(grade);
    }

    public List<Grade> getAllGrades() {
        return gradeRepository.findAll();
    }

    public List<Grade> findGradesByStudent(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Student not found"));
        return gradeRepository.findByStudent(user);
    }

}
