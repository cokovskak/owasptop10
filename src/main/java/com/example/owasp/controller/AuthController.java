package com.example.owasp.controller;


import com.example.owasp.model.User;
import com.example.owasp.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.security.crypto.password.PasswordEncoder;


import java.security.Principal;

@Controller
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    @GetMapping("/")
    public String homePage() {
        return "index";
    }

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @GetMapping("/register")
    public String registerPage(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }



    @PostMapping("/register")
    public String registerUser(User user, RedirectAttributes redirectAttributes) {
        // Validate username and email uniqueness
        if (userService.usernameExists(user.getUsername())) {
            redirectAttributes.addFlashAttribute("error", "Username already exists");
            return "redirect:/register";
        }

        if (userService.emailExists(user.getEmail())) {
            redirectAttributes.addFlashAttribute("error", "Email already exists");
            return "redirect:/register";
        }

        // Default role for registration is STUDENT
        user.setRole(User.Role.ROLE_STUDENT);

        userService.createUser(user);


        redirectAttributes.addFlashAttribute("success", "Registration successful. Please login.");
        return "redirect:/login";
    }
    @PostMapping("/change-password")
    public String changePassword(@RequestParam String securityAnswer,
                                 @RequestParam String newPassword,
                                 @RequestParam String confirmPassword,
                                 Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findByUsername(auth.getName()).orElse(null);

        if (!user.getSecurityAnswer().equalsIgnoreCase(securityAnswer)) {
            model.addAttribute("error", "Security answer is incorrect.");
        } else if (!newPassword.equals(confirmPassword)) {
            model.addAttribute("error", "Passwords do not match.");
        } else {
            user.setPassword(passwordEncoder.encode(newPassword));
            userService.updateUser(user);
            model.addAttribute("success", "Password changed successfully.");
        }


        model.addAttribute("user", user);
        model.addAttribute("fullname", userService.findById(user.getId()).get().getFullName());
        model.addAttribute("username", userService.findById(user.getId()).get().getUsername());
        model.addAttribute("email", userService.findById(user.getId()).get().getEmail());
        return "/student/profile";
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findByUsername(auth.getName()).orElse(null);

        if (user == null) {
            return "redirect:/login";
        }

        model.addAttribute("user", user);

        switch (user.getRole()) {
            case ROLE_STUDENT:
                return "redirect:/student/dashboard";
            case ROLE_TEACHER:
                return "redirect:/teacher/dashboard";
            case ROLE_ADMIN:
                return "redirect:/admin/dashboard";
            default:
                return "redirect:/login";
        }
    }
}