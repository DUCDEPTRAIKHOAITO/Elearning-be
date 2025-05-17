package org.elearning.model;

import jakarta.persistence.*;
import lombok.Data;
import org.elearning.enums.CategoryStatus;

import java.util.UUID;

@Entity
@Table(name = "category")
@Data
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String categoryName;
    private String description;

    @Enumerated(EnumType.STRING)
    private CategoryStatus status = CategoryStatus.ACTIVE;
}

