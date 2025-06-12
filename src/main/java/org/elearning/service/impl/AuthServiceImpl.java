package org.elearning.service.impl;

import lombok.RequiredArgsConstructor;
import org.elearning.dto.elearning.auth.AuthResponseDTO;
import org.elearning.dto.elearning.auth.LoginRequest;
import org.elearning.dto.elearning.auth.RegisterRequest;
import org.elearning.model.Role;
import org.elearning.model.User;
import org.elearning.respository.RoleRepository;
import org.elearning.respository.UserRepository;
import org.elearning.security.JwtUtil;
import org.elearning.service.AuthService;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final JwtUtil jwtUtil;

    // Đăng ký người dùng và tự động gán vai trò Learner
    @Override
    public void register(RegisterRequest request) {
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new RuntimeException("Email đã tồn tại");
        }

        // Tạo người dùng mới
        User user = new User();
        user.setId(UUID.randomUUID());
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(request.getPassword());

        // Nếu không có vai trò được chỉ định, tự động gán vai trò Learner
        if (request.getRoleId() != null) {
            Optional<Role> role = roleRepository.findById(UUID.fromString(request.getRoleId()));
            user.setRole(role.orElse(null));
        } else {
            // Tự động gán vai trò Learner nếu không có vai trò được chỉ định
            Optional<Role> learnerRole = roleRepository.findByName("Learner");
            user.setRole(learnerRole.orElseThrow(() -> new RuntimeException("Không tìm thấy vai trò Learner")));
        }

        // Lưu người dùng vào cơ sở dữ liệu
        userRepository.save(user);
    }

    // Đăng nhập và kiểm tra vai trò của người dùng
    @Override
    public AuthResponseDTO login(LoginRequest request) {
        Optional<User> userOpt = userRepository.findByEmail(request.getEmail());

        if (userOpt.isPresent() && userOpt.get().getPassword().equals(request.getPassword())) {
            User user = userOpt.get();

            // Nếu người dùng không có vai trò hoặc vai trò không phải Learner, gán lại vai trò Learner
            if (user.getRole() == null || !user.getRole().getName().equals("Learner")) {
                Optional<Role> learnerRole = roleRepository.findByName("Learner");
                user.setRole(learnerRole.orElseThrow(() -> new RuntimeException("Không tìm thấy vai trò Learner")));
                userRepository.save(user);  // Cập nhật lại vai trò của người dùng trong cơ sở dữ liệu
            }

            // Tạo token JWT
            String token = jwtUtil.generateToken(user.getEmail());

            // Tạo đối tượng AuthResponseDTO để trả về
            AuthResponseDTO authResponseDTO = new AuthResponseDTO();
            authResponseDTO.setEmail(user.getEmail());
            authResponseDTO.setToken(token);
            authResponseDTO.setUsername(user.getName());
            authResponseDTO.setRole(user.getRole() != null ? user.getRole().getName() : "Learner");

            return authResponseDTO;
        } else {
            throw new RuntimeException("Wrong email or password");
        }
    }
}
