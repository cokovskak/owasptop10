package com.example.owasp.controller;


import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.example.owasp.model.User;
import com.example.owasp.service.GradeService;
import com.example.owasp.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/student")
@RequiredArgsConstructor
public class StudentController {

    private final UserService userService;
    private final GradeService gradeService;

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findByUsername(auth.getName()).orElse(null);

        model.addAttribute("user", user);
        return "student/dashboard";
    }

    @GetMapping("/{id}/profile")
    public String profile(@PathVariable Long id,Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        User user = userService.findByUsername(auth.getName()).orElse(null);

        model.addAttribute("user", user);
        model.addAttribute("fullname", userService.findById(id).get().getFullName());
        model.addAttribute("username", userService.findById(id).get().getUsername());
        model.addAttribute("email", userService.findById(id).get().getEmail());
        model.addAttribute("role", userService.findById(id).get().getRole());
        return "student/profile";
    }


    @GetMapping("/{id}/grades")
    public String showGrades(@PathVariable Long id, Model model) {
        model.addAttribute("grades", gradeService.findGradesByStudent(id));
        model.addAttribute("fullname", userService.findById(id).get().getFullName());

        return "student/grades";
    }

    @PostMapping("/upload")
    public String uploadHomework(@RequestParam("file") MultipartFile file, RedirectAttributes redirectAttributes) {
        String UPLOAD_DIR = "src/main/resources/uploads/";

        File dir = new File(UPLOAD_DIR);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        try {
            String filename = file.getOriginalFilename();
            Path filePath = Paths.get(UPLOAD_DIR + filename);
            Files.write(filePath, file.getBytes());

            String fileContent = new String(Files.readAllBytes(filePath), "UTF-8");
            System.out.println("Received code submission: " + filename);

            // OWASP A08:2021 - Software and Data Integrity Failures
            if (filename.endsWith(".py")) {
                System.out.println("Executing Python code...");

                Path tempScript = Files.createTempFile("student_submission_", ".py");
                Files.write(tempScript, fileContent.getBytes());

                Process process = Runtime.getRuntime().exec("python " + tempScript.toAbsolutePath().toString());

                BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
                StringBuilder output = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    output.append(line).append("\n");
                }

                BufferedReader errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
                StringBuilder errorOutput = new StringBuilder();
                while ((line = errorReader.readLine()) != null) {
                    errorOutput.append(line).append("\n");
                }

                int exitCode = process.waitFor();
                System.out.println("Python execution completed. Exit code: " + exitCode);

                if (exitCode == 0) {
                    System.out.println("Output: " + output.toString());
                    redirectAttributes.addFlashAttribute("success", "Python code executed successfully!");
                    redirectAttributes.addFlashAttribute("output", output.toString());
                } else {
                    System.out.println("Error: " + errorOutput.toString());
                    redirectAttributes.addFlashAttribute("warning", "Code execution resulted in errors: " + errorOutput.toString());
                }

                Files.deleteIfExists(tempScript);
            } else if (filename.endsWith(".js")) {
                System.out.println("Executing JavaScript code...");

                Path tempScript = Files.createTempFile("student_submission_", ".js");
                Files.write(tempScript, fileContent.getBytes());

                Process process = Runtime.getRuntime().exec("node " + tempScript.toAbsolutePath().toString());
                BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
                StringBuilder output = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    output.append(line).append("\n");
                }

                BufferedReader errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
                StringBuilder errorOutput = new StringBuilder();
                while ((line = errorReader.readLine()) != null) {
                    errorOutput.append(line).append("\n");
                }

                int exitCode = process.waitFor();

                if (exitCode == 0) {
                    redirectAttributes.addFlashAttribute("success", "JavaScript code executed successfully!");
                    redirectAttributes.addFlashAttribute("output", output.toString());
                } else {
                    redirectAttributes.addFlashAttribute("warning", "Code execution resulted in errors: " + errorOutput.toString());
                }

                Files.deleteIfExists(tempScript);
            } else {
                redirectAttributes.addFlashAttribute("info", "File uploaded successfully, but only .py and .js files can be executed.");
            }

            return "redirect:/student/dashboard";

        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("error", "Upload failed: " + e.getMessage());
            return "redirect:/student/dashboard";
        }
    }


}