package org.elearning.respository;

import org.elearning.enums.CourseStatus;
import org.elearning.model.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface CourseRepository extends JpaRepository<Course, UUID> {
    // Tìm Course theo Category
    List<Course> findByCategory_Id(UUID categoryId);

    // Tìm Course theo tên
    List<Course> findByName(String name);
    // Tìm Course tên chứa keyword (ignore case)
    List<Course> findByNameContainingIgnoreCase(String keyword);
    List<Course> findByStatus(CourseStatus status);

    // Tìm Course tên hoặc mô tả chứa keyword (ignore case)
    List<Course> findByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase(
            String nameKeyword,
            String descriptionKeyword
    );
}
