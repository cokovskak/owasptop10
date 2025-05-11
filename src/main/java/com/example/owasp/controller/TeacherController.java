package com.example.owasp.controller;


import com.example.owasp.model.User;
import com.example.owasp.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/teacher")
@RequiredArgsConstructor
public class TeacherController {

    private final UserService userService;

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findByUsername(auth.getName()).orElse(null);

        model.addAttribute("user", user);
        return "teacher/dashboard";
    }

    @GetMapping("/profile")
    public String profile(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findByUsername(auth.getName()).orElse(null);

        model.addAttribute("user", user);
        return "teacher/profile";
    }

    @GetMapping("/students")
    public String viewStudents(Model model) {
        // Get all students
        model.addAttribute("students", userService.findAllUsers().stream()
                .filter(user -> user.getRole() == User.Role.ROLE_STUDENT)
                .toList());
        return "teacher/students";
    }

    /**
     * SSRF Vulnerable Endpoint
     * This endpoint allows teachers to fetch external resources
     * but contains an SSRF vulnerability
     */
    @GetMapping("/resource-fetcher")
    public String resourceFetcherPage(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findByUsername(auth.getName()).orElse(null);

        model.addAttribute("user", user);
        return "teacher/resource-fetcher";
    }


    @PostMapping("/fetch-resource")
    @ResponseBody
    public ResponseEntity<String> fetchResource(@RequestParam String url) {
        try {
            // Vulnerable implementation: No validation on the URL parameter
            URL resourceUrl = new URL(url);
            URLConnection connection = resourceUrl.openConnection();

            try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                String content = reader.lines().collect(Collectors.joining("\n"));
                return ResponseEntity.ok(content);
            }
        } catch (IOException e) {
            return ResponseEntity.badRequest().body("Error fetching resource: " + e.getMessage());
        }
    }

    /**
     * Internal API endpoint that should not be accessible through SSRF
     * Contains sensitive information that would be valuable to an attacker
     */
    @GetMapping("/internal-api/system-info")
    @ResponseBody
    public ResponseEntity<String> getSystemInfo() {
        // This endpoint is meant to be internal and not accessible from outside
        StringBuilder info = new StringBuilder();
        info.append("System Information:\n");
        info.append("===================\n");
        info.append("Database Connection String: jdbc:mysql://localhost:3306/school_db\n");
        info.append("Database Username: school_admin\n");
        info.append("Database Password: super_secret_password123\n");
        info.append("API Key for External Service: 4pI_k3Y_f0R_3xT3rN4l_S3rV1c3\n");
        info.append("Environment: PRODUCTION\n");

        return ResponseEntity.ok(info.toString());
    }
}