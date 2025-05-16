package org.elearning.controller;

import lombok.RequiredArgsConstructor;
import org.elearning.security.JwtUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class SecureController {

    private final JwtUtil jwtUtil;

    @GetMapping("/secure-data")
    public ResponseEntity<?> getSecureData(@RequestHeader("Authorization") String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(401).body("Thiếu token xác thực");
        }

        String token = authHeader.substring(7); // Bỏ "Bearer "
        if (jwtUtil.validateToken(token)) {
            String email = jwtUtil.getEmailFromToken(token);
            return ResponseEntity.ok("Chào " + email + "! Bạn đã xác thực thành công.");
        } else {
            return ResponseEntity.status(401).body("Token không hợp lệ hoặc đã hết hạn");
        }
    }
}