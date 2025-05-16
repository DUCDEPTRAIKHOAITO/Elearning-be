package org.elearning.controller;

import lombok.RequiredArgsConstructor;
import org.elearning.dto.elearning.auth.LoginRequest;
import org.elearning.dto.elearning.auth.RegisterRequest;
import org.elearning.model.User;
import org.elearning.respository.UserRepository;
import org.elearning.security.JwtUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class LoginController {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            return ResponseEntity.badRequest().body("Email đã tồn tại");
        }

        User user = new User();
        user.setId(UUID.randomUUID());
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(request.getPassword()); // ⚠️ Chưa mã hóa (chỉ dùng test)

        userRepository.save(user);
        return ResponseEntity.ok("Đăng ký thành công");
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        Optional<User> userOpt = userRepository.findByEmail(request.getEmail());

        if (userOpt.isPresent() && userOpt.get().getPassword().equals(request.getPassword())) {
            String token = jwtUtil.generateToken(userOpt.get().getEmail());
            return ResponseEntity.ok(token);
        }

        return ResponseEntity.status(401).body("Sai email hoặc mật khẩu");
    }
}