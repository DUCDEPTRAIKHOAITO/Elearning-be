package org.elearning.service;

import org.elearning.dto.elearning.CourseDTO;

import java.util.List;
import java.util.UUID;

public interface CourseService {
    List<CourseDTO> getAllCourses();
    CourseDTO getCourseById(UUID id);
    CourseDTO createCourse(CourseDTO courseDTO);
    CourseDTO updateCourse(UUID id, CourseDTO courseDTO);
    void deleteCourse(UUID id);
    CourseDTO updateImage(UUID id, String imageUrl);
    List<CourseDTO> getActiveCourses();
//        * Tìm kiếm khóa học theo tên hoặc mô tả chứa keyword

    List<CourseDTO> searchCourses(String keyword);
}
