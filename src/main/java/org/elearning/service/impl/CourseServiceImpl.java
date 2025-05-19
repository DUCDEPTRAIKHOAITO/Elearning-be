package org.elearning.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.elearning.dto.elearning.CourseDTO;
import org.elearning.enums.CourseStatus;
import org.elearning.model.Course;
import org.elearning.respository.CourseRepository;
import org.elearning.respository.CategoryRepository;
import org.elearning.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CourseServiceImpl implements CourseService {


    private final CourseRepository courseRepository;


    private final CategoryRepository categoryRepository;
    @Override
    // Get all courses
    public List<CourseDTO> getAllCourses() {
        List<Course> courses = courseRepository.findAll();
        return courses.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    // Get course by ID
    public CourseDTO getCourseById(UUID id) {
        Optional<Course> course = courseRepository.findById(id);
        return course.map(this::convertToDTO).orElse(null);
    }

    @Override
    // Create new course
    public CourseDTO createCourse(CourseDTO courseDTO) {
        Course course = new Course();
        course.setName(courseDTO.getName());
        course.setDescription(courseDTO.getDescription());
        course.setCategory(categoryRepository.findById(UUID.fromString(courseDTO.getCategoryId())).orElse(null));
        course.setImageUrl(courseDTO.getImageUrl());
        // Mapping trạng thái từ DTO xuống Entity
        if (courseDTO.getCourseStatus() != null) {
            course.setStatus(CourseStatus.valueOf(courseDTO.getCourseStatus()));
        } else {
            // hoặc gán mặc định
            course.setStatus(CourseStatus.ACTIVE);
        }

        course = courseRepository.save(course);
        return convertToDTO(course);
    }

    @Override
    @Transactional
    public CourseDTO updateImage(UUID id, String imageUrl) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Course not found: " + id));
        course.setImageUrl(imageUrl);
        Course saved = courseRepository.save(course);

        // nếu bạn có method convertToDTO, nhớ set thêm field imageUrl vào DTO
        return convertToDTO(saved);
    }

    public CourseDTO updateCourse(UUID id, CourseDTO courseDTO) {
        Optional<Course> existingCourse = courseRepository.findById(id);
        if (existingCourse.isPresent()) {
            Course course = existingCourse.get();
            course.setName(courseDTO.getName());
            course.setImageUrl(courseDTO.getImageUrl());
            course.setDescription(courseDTO.getDescription());
            course.setCategory(
                    categoryRepository.findById(UUID.fromString(courseDTO.getCategoryId()))
                            .orElse(null)
            );
            // Mapping status
            if (courseDTO.getCourseStatus() != null) {
                course.setStatus(CourseStatus.valueOf(courseDTO.getCourseStatus()));
            }
            course = courseRepository.save(course);
            return convertToDTO(course);
        }
        return null;
    }
    @Override
    @Transactional
    public List<CourseDTO> searchCourses(String keyword) {
        return courseRepository
                .findByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase(keyword, keyword)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    // Delete course
    public void deleteCourse(UUID id) {
        courseRepository.deleteById(id);
    }

    // Convert Course to CourseDTO
    private CourseDTO convertToDTO(Course course) {
        CourseDTO dto = new CourseDTO();
        dto.setId(course.getId().toString());
        dto.setName(course.getName());
        dto.setDescription(course.getDescription());
        dto.setCategoryId(course.getCategory().getId().toString());
        dto.setImageUrl(course.getImageUrl());
        dto.setCourseStatus(course.getStatus() != null
                ? course.getStatus().name()
                : null);
        return dto;
    }
}
