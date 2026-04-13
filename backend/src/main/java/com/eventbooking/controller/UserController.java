package com.eventbooking.controller;

import com.eventbooking.model.Admin;
import com.eventbooking.model.User;
import com.eventbooking.repository.AdminRepository;
import com.eventbooking.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class UserController {

    private final AdminRepository adminRepository;
    private final UserRepository userRepository;

    public UserController(AdminRepository adminRepository, UserRepository userRepository) {
        this.adminRepository = adminRepository;
        this.userRepository = userRepository;
    }

    // ===== POST /api/login =====
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> body) {
        String email = body.get("email");
        String password = body.get("password");
        String role = body.get("role");

        if (email == null || password == null || role == null) {
            return ResponseEntity.badRequest().body(Map.of("error", "Email, password, and role are required"));
        }

        if ("admin".equals(role)) {
            // Check ADMIN table
            Optional<Admin> adminOpt = adminRepository.findByEmailAndPassword(email, password);
            if (adminOpt.isEmpty()) {
                return ResponseEntity.status(401).body(Map.of("error", "Invalid admin credentials"));
            }

            Admin admin = adminOpt.get();
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Login successful");
            response.put("userId", admin.getId());
            response.put("name", admin.getName());
            response.put("email", admin.getEmail());
            response.put("role", "admin");
            return ResponseEntity.ok(response);

        } else {
            // Check USERS table — auto-register if not found
            Optional<User> userOpt = userRepository.findByEmailAndPassword(email, password);

            if (userOpt.isPresent()) {
                User user = userOpt.get();
                Map<String, Object> response = new HashMap<>();
                response.put("message", "Login successful");
                response.put("userId", user.getId());
                response.put("name", user.getName());
                response.put("email", user.getEmail());
                response.put("role", "user");
                return ResponseEntity.ok(response);
            }

            // Check if email exists but password is wrong
            Optional<User> existingEmail = userRepository.findByEmail(email);
            if (existingEmail.isPresent()) {
                return ResponseEntity.status(401).body(Map.of("error", "Incorrect password"));
            }

            // Auto-register new user
            String name = email.split("@")[0];
            User newUser = new User(name, email, password);
            User savedUser = userRepository.save(newUser);

            Map<String, Object> response = new HashMap<>();
            response.put("message", "Account created and logged in");
            response.put("userId", savedUser.getId());
            response.put("name", savedUser.getName());
            response.put("email", savedUser.getEmail());
            response.put("role", "user");
            return ResponseEntity.ok(response);
        }
    }
}
