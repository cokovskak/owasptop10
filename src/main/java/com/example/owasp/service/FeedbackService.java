package com.example.owasp.service;

import com.example.owasp.model.Feedback;
import com.example.owasp.repository.FeedbackRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
@RequiredArgsConstructor
public class FeedbackService {

    private final FeedbackRepository feedbackRepository;
    private final JdbcTemplate jdbcTemplate;  // add this

    public  Feedback saveFeedback(Feedback feedback) {
        // Safe default saving:
        return feedbackRepository.save(feedback);
    }

    // Vulnerable method - don't use in production!
    public void saveFeedbackVulnerable(Feedback feedback) {
        String sql = "INSERT INTO feedback (username, message, submitted_at) VALUES ('"
                + feedback.getUsername() + "', '"
                + feedback.getMessage() + "', '"
                + feedback.getSubmittedAt() + "')";
        jdbcTemplate.execute(sql);
    }

    public List<Feedback> getAllFeedback() {
        return feedbackRepository.findAll();
    }
}
