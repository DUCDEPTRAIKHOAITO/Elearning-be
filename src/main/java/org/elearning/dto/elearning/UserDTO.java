package org.elearning.dto.elearning;

import lombok.Data;

import java.util.UUID;

@Data
public class UserDTO {
    private String id;
    private String name;
    private String email;
    private String password;
    private String status;
    private String roleId;  // Liên kết với Role
    //test
}