package com.example.owasp.config;


import com.example.owasp.model.Grade;
import com.example.owasp.model.User;
import com.example.owasp.service.GradeService;
import com.example.owasp.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DatabaseSeeder implements CommandLineRunner {
    private final GradeService gradeService;
    private final UserService userService;

    @Override
    public void run(String... args) {
        // Only seed if the database is empty
        if (userService.findAllUsers().isEmpty()) {
            seedUsers();
        }
    }

    private void seedUsers() {
        // Create admin user
        User admin = User.builder()
                .username("admin")
                .password("admin123")
                .fullName("System Administrator")
                .email("admin@example.com")
                .role(User.Role.ROLE_ADMIN)
                .active(true)
                .build();
        userService.createUser(admin);

        // Create teacher users
        User teacher1 = User.builder()
                .username("teacher1")
                .password("teacher123")
                .fullName("John Smith")
                .email("john.smith@example.com")
                .role(User.Role.ROLE_TEACHER)
                .active(true)
                .build();
        userService.createUser(teacher1);

        User teacher2 = User.builder()
                .username("teacher2")
                .password("teacher123")
                .fullName("Emily Johnson")
                .email("emily.johnson@example.com")
                .role(User.Role.ROLE_TEACHER)
                .active(true)
                .build();
        userService.createUser(teacher2);

        // Create student users
        User student1 = User.builder()
                .username("student1")
                .password("student123")
                .fullName("Alex Brown")
                .email("alex.brown@example.com")
                .role(User.Role.ROLE_STUDENT)
                .active(true)
                .build();
        userService.createUser(student1);
        System.out.println("Created student1 with ID: " + student1.getId());

        User student2 = User.builder()
                .username("student2")
                .password("student123")
                .fullName("Maria Garcia")
                .email("maria.garcia@example.com")
                .role(User.Role.ROLE_STUDENT)
                .active(true)
                .build();
        userService.createUser(student2);
        System.out.println("Created student2 with ID: " + student2.getId());

        User student3 = User.builder()
                .username("student3")
                .password("student123")
                .fullName("James Wilson")
                .email("james.wilson@example.com")
                .role(User.Role.ROLE_STUDENT)
                .active(true)
                .build();
        userService.createUser(student3);
        System.out.println("Created student3 with ID: " + student3.getId());

        Grade mathGrade = Grade.builder()
                .courseName("Mathematics")
                .gradeValue("A")
                .student(student1) // this is a User object created earlier
                .build();
        gradeService.saveGrade(mathGrade);

        Grade science = Grade.builder()
                .courseName("Science")
                .gradeValue("B")
                .student(student2) // this is a User object created earlier
                .build();
        gradeService.saveGrade(science);
    }
}