package org.elearning.dto.elearning;

import lombok.Data;

import java.util.List;
import java.util.Set;

@Data
public class CourseDTO {
    private String id;
    private String name;
    private String categoryId; // Liên kết với Category
    private String description;
    private String courseStatus;
    private String imageUrl;
    private List<LessonDTO> lessons;
}
