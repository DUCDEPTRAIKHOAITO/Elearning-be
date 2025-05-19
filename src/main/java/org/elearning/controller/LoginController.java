package org.elearning.controller;

import lombok.RequiredArgsConstructor;
import org.elearning.dto.elearning.auth.AuthResponseDTO;
import org.elearning.dto.elearning.auth.LoginRequest;
import org.elearning.dto.elearning.auth.RegisterRequest;
import org.elearning.model.User;
import org.elearning.respository.UserRepository;
import org.elearning.security.JwtUtil;
import org.elearning.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class LoginController {

    private final AuthService authService;


    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        try {
            authService.register(request);
            return ResponseEntity.ok("Register successful");
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        try {
            AuthResponseDTO authResponseDTO = authService.login(request);
            return ResponseEntity.ok(authResponseDTO);
        }
        catch (Exception e) {
            return ResponseEntity.status(401).body(e.getMessage());
        }
    }
}