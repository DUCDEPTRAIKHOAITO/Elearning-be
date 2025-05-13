package org.elearning.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.UUID;

@Entity
@Table(name = "admin")
@Data
@PrimaryKeyJoinColumn(name = "id")
public class Admin extends User {
    @Column(nullable = false, length = 100)
    private String departmentName;

}