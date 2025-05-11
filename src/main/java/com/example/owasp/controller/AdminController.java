package com.example.owasp.controller;

import com.example.owasp.model.User;
import com.example.owasp.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final UserService userService;
    private final ErrorAttributes errorAttributes;

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findByUsername(auth.getName()).orElse(null);

        model.addAttribute("user", user);
        return "admin/dashboard";
    }

    @GetMapping("/users")
    public String listUsers(Model model) {
        List<User> users = userService.findAllUsers();
        model.addAttribute("users", users);
        return "admin/users";
    }

    @GetMapping("/users/add")
    public String addUserForm(Model model) {
        model.addAttribute("user", new User());
        model.addAttribute("roles", Arrays.asList(User.Role.values()));
        return "admin/add-user";
    }

    @PostMapping("/users/add")
    public String addUser(User user, RedirectAttributes redirectAttributes) {
        if (userService.usernameExists(user.getUsername())) {
            redirectAttributes.addFlashAttribute("error", "Username already exists");
            return "redirect:/admin/users/add";
        }

        if (userService.emailExists(user.getEmail())) {
            redirectAttributes.addFlashAttribute("error", "Email already exists");
            return "redirect:/admin/users/add";
        }

        userService.createUser(user);
        redirectAttributes.addFlashAttribute("success", "User added successfully");
        return "redirect:/admin/users";
    }

    @GetMapping("/users/edit/{id}")
    public String editUserForm(@PathVariable Long id, Model model) {
        User user = userService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid user ID: " + id));

        model.addAttribute("user", user);
        model.addAttribute("roles", Arrays.asList(User.Role.values()));
        return "admin/edit-user";
    }

    @PostMapping("/users/edit/{id}")
    public String updateUser(@PathVariable Long id, User user, RedirectAttributes redirectAttributes) {
        userService.updateUser(user);
        redirectAttributes.addFlashAttribute("success", "User updated successfully");
        return "redirect:/admin/users";
    }

    @GetMapping("/users/delete/{id}")
    public String deleteUser(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        userService.deleteUser(id);
        redirectAttributes.addFlashAttribute("success", "User deleted successfully");
        return "redirect:/admin/users";
    }




}