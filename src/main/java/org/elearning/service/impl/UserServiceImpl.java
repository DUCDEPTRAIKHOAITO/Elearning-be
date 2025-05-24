package org.elearning.service.impl;

import lombok.RequiredArgsConstructor;
import org.elearning.dto.elearning.UserDTO;
import org.elearning.enums.RoleInfo;
import org.elearning.enums.UserStatus;
import org.elearning.model.User;
import org.elearning.model.Role;
import org.elearning.respository.UserRepository;
import org.elearning.respository.RoleRepository;
import org.elearning.service.UserService;
import org.elearning.util.EmailSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {


    private final UserRepository userRepository;


    private final RoleRepository roleRepository;

    @Override
    // Lấy tất cả người dùng
    public List<UserDTO> getAllUsers() {
        List<User> users = userRepository.findAll();
        return users.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    // Lấy người dùng theo ID
    public UserDTO getUserById(UUID id) {
        Optional<User> user = userRepository.findById(UUID.fromString(id.toString()));  // Make sure you're passing UUID here
        return user.map(this::convertToDTO).orElse(null);
    }

    @Override
//     Tạo mới người dùng
    public UserDTO createUser(UserDTO userDTO) {
        Role role = roleRepository.findByName(RoleInfo.User.name()).orElse(null);  // Look up Role by UUID

        String password = generatePassword();

        User user = new User();
        user.setName(userDTO.getName());
        user.setEmail(userDTO.getEmail());
        user.setPassword(password);
        user.setStatus(UserStatus.ACTIVE);  // Set status from UserDTO
        user.setRole(role);  // Set role
        user = userRepository.save(user);

        if (user != null) {
            EmailSender.sendPasswordEmail(userDTO.getEmail(), userDTO.getName(), password);
        }

        return convertToDTO(user);
    }


    @Override
    // Cập nhật người dùng
    public UserDTO updateUser(UserDTO userDTO) {
        Optional<User> existingUser = userRepository.findById(UUID.fromString(userDTO.getId()));
        if (existingUser.isPresent()) {
            User user = existingUser.get();
            user.setName(userDTO.getName());
            user.setEmail(userDTO.getEmail());
            user.setStatus(UserStatus.valueOf(userDTO.getStatus()));  // Cập nhật status từ DTO
            user.setRole(roleRepository.findById(UUID.fromString(userDTO.getRoleId())).orElse(null));  // Cập nhật role
//            if (userDTO.getRoleId() != null) {
//                UUID roleId = UUID.fromString(userDTO.getRoleId());
//                Role role = roleRepository.findById(String.valueOf(roleId))
//                        .orElseThrow(() -> new IllegalArgumentException("Role không tồn tại: " + roleId));
//                user.setRole(role);
//            }

            user = userRepository.save(user);
            return convertToDTO(user);
        }
        return null;
    }

    @Override
    // Xóa người dùng
    public void deleteUser(UUID id) {
        userRepository.deleteById(id);
    }

    // Chuyển đổi User thành UserDTO
    private UserDTO convertToDTO(User user) {
        UserDTO dto = new UserDTO();
        dto.setId(String.valueOf(user.getId()));
        dto.setName(user.getName());
        dto.setEmail(user.getEmail());
        dto.setStatus(String.valueOf(user.getStatus()));  // Truyền status dưới dạng UserStatus
        dto.setRoleId(user.getRole() != null ? String.valueOf(user.getRole().getId()) : null);  // Chuyển role thành ID nếu cần
        return dto;
    }

    private String generatePassword() {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*";
        Random random = new Random();
        StringBuilder password = new StringBuilder();

        for (int i = 0; i < 12; i++) {
            password.append(chars.charAt(random.nextInt(chars.length())));
        }

        return password.toString();
    }
}
