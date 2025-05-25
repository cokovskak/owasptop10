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
    private final JdbcTemplate jdbcTemplate;

    public  Feedback saveFeedback(Feedback feedback) {
        // Safe default saving:
        return feedbackRepository.save(feedback);
    }

    // Vulnerable method - don't use in production!
    public void saveFeedbackVulnerable(Feedback feedback) {
        String sql = "INSERT INTO feedback (username, message) VALUES ('"
                + feedback.getUsername() + "', '"
                + feedback.getMessage() + "');";
              //  + feedback.getSubmittedAt() + "')";
        System.out.println("Executing SQL: " + sql);
        jdbcTemplate.execute(sql);
    }
    // Additional vulnerable search method for more SQL injection opportunities
    public List<Feedback> searchFeedbackVulnerable(String searchTerm,String username) {
        //String sql = "SELECT * FROM feedback WHERE message LIKE '%" + searchTerm + "%'";
        String sql = "SELECT id, username, message FROM feedback WHERE username = '" + username + "' AND message LIKE '%" + searchTerm + "%'";

        System.out.println("Executing SQL: " + sql); // For debugging/demonstration

        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            Feedback feedback = new Feedback();
            feedback.setId(rs.getLong("id"));
            feedback.setUsername(rs.getString("username"));
            feedback.setMessage(rs.getString("message"));
            return feedback;
        });
    }


    public List<Feedback> getAllFeedback() {
        return feedbackRepository.findAll();
    }
}
