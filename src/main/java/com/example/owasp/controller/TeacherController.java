package com.example.owasp.controller;


import com.example.owasp.model.User;
import com.example.owasp.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

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
}