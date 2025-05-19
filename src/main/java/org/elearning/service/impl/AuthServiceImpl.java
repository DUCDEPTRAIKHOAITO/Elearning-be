package org.elearning.service.impl;

import lombok.RequiredArgsConstructor;
import org.elearning.dto.elearning.auth.AuthResponseDTO;
import org.elearning.dto.elearning.auth.LoginRequest;
import org.elearning.dto.elearning.auth.RegisterRequest;
import org.elearning.enums.RoleInfo;
import org.elearning.model.Role;
import org.elearning.model.User;
import org.elearning.respository.RoleRepository;
import org.elearning.respository.UserRepository;
import org.elearning.security.JwtUtil;
import org.elearning.service.AuthService;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final JwtUtil jwtUtil;

    @Override
    public void register(RegisterRequest request) {
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
           throw new RuntimeException("Email đã tồn tại");
        }

        Optional<Role> role = roleRepository.findByName(RoleInfo.User.name());

        if (role.isEmpty()) {
            throw new RuntimeException("Role not found");
        }

        User user = new User();
        user.setId(UUID.randomUUID());
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(request.getPassword());
        user.setRole(role.get());

       userRepository.save(user);
    }

    @Override
    public AuthResponseDTO login(LoginRequest request) {
        Optional<User> userOpt = userRepository.findByEmail(request.getEmail());

        if (userOpt.isPresent() && userOpt.get().getPassword().equals(request.getPassword())) {
            String token = jwtUtil.generateToken(userOpt.get().getEmail());
            AuthResponseDTO authResponseDTO = new AuthResponseDTO();
            authResponseDTO.setEmail(userOpt.get().getEmail());
            authResponseDTO.setToken(token);
            authResponseDTO.setUsername(userOpt.get().getName());
            authResponseDTO.setRole(userOpt.get().getRole().getName());
            return authResponseDTO;
        }
        else {
            throw new RuntimeException("Wrong email or password");
        }
    }
}
