package com.example.owasp.controller;

import com.example.owasp.model.Feedback;
import com.example.owasp.model.User;
import com.example.owasp.service.FeedbackService;
import com.example.owasp.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/feedback")
public class FeedbackController {
    private static final Logger log = LogManager.getLogger(FeedbackController.class);
    private final FeedbackService feedbackService;
    private final UserService userService;


    @GetMapping("/showfeedback")
    public String showFeedbackForm(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findByUsername(auth.getName()).orElse(null);

        model.addAttribute("user", user);
        return "student/feedback-form";
    }

    @PostMapping("/submitFeedback")
    public String submitFeedback(Authentication authentication,
                                 @RequestParam String message,
                                 Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        User user = userService.findByUsername(auth.getName()).orElse(null);

        if (user == null) {
            return "redirect:/login";
        }
        // Intentionally vulnerable logging: logs raw comments
        log.info("Feedback received from user '{}': {}", user.getUsername(), message);

        Feedback feedback = Feedback.builder()
                .username(user.getUsername())
                .message(message)
                .submittedAt(LocalDateTime.now())
                .build();

        try {
            feedbackService.saveFeedbackVulnerable(feedback);
            model.addAttribute("success", "Thank you for your feedback!");
        } catch (Exception e) {
            // Show the error to demonstrate SQL injection impact
            model.addAttribute("error", "Database error: " + e.getMessage());
            log.error("SQL Error: ", e);
        }
        model.addAttribute("user", user);
        return "student/feedback-form";
    }
    // NEW: Vulnerable search endpoint that shows SQL injection results
    @GetMapping("/search")
    public String showSearchForm(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findByUsername(auth.getName()).orElse(null);
        model.addAttribute("user", user);
        return "student/feedback-search";
    }

    @PostMapping("/search")
    public String searchFeedback(@RequestParam String searchTerm, Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findByUsername(auth.getName()).orElse(null);

        if (user == null) {
            return "redirect:/login";
        }

        try {
            // This is where SQL injection will be most visible
            List<Feedback> results = feedbackService.searchFeedbackVulnerable(searchTerm,user.getUsername());
            model.addAttribute("results", results);
            model.addAttribute("searchTerm", searchTerm);
            model.addAttribute("bravo", "Search completed successfully!");
        } catch (Exception e) {
            model.addAttribute("error", "Search error: " + e.getMessage());
            model.addAttribute("searchTerm", searchTerm);
            log.error("Search SQL Error: ", e);
        }

        model.addAttribute("user", user);
        return "student/feedback-search";
    }


}
