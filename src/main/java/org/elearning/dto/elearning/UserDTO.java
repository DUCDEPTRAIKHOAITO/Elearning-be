package org.elearning.dto.elearning;

import lombok.Data;

import java.util.UUID;

@Data
public class UserDTO {
    private String id;
    private String name;
    private String email;
    private String status;
    private String roleId;
}