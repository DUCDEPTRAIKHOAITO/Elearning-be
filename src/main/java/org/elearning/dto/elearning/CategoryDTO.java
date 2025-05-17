package org.elearning.dto.elearning;

import lombok.Data;
import org.elearning.enums.CategoryStatus;

@Data
public class CategoryDTO {
    private String id;
    private String categoryName;
    private String description;
    private CategoryStatus status;
}