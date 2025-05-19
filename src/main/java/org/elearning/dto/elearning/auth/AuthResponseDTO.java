package org.elearning.dto.elearning.auth;

import lombok.Data;

@Data
public class AuthResponseDTO {
    private String token;
    private String username;
    private String role;
    private String email;
}
