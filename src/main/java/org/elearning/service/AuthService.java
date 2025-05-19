package org.elearning.service;

import org.elearning.dto.elearning.auth.AuthResponseDTO;
import org.elearning.dto.elearning.auth.LoginRequest;
import org.elearning.dto.elearning.auth.RegisterRequest;

public interface AuthService {
    void register(RegisterRequest request);
    AuthResponseDTO login(LoginRequest request);
}
