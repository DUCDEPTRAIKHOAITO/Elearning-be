package org.elearning.dto.elearning;

import lombok.Data;
import org.elearning.enums.UserStatus;

import java.util.UUID;

@Data
public class AdminDTO {
    private String id;
    private String departmentName;
    private String userName;

    private String name;
    private String email;
    private String password;
    private UserStatus status;
    private String roleId;
//    private UUID userId;
}