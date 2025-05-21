package org.elearning.controller;

import lombok.RequiredArgsConstructor;
import org.elearning.dto.elearning.CourseDTO;
import org.elearning.service.CourseService;
import org.elearning.service.impl.FileStorageService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;


import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/courses")
@RequiredArgsConstructor
public class CourseController {

    private final CourseService courseService;
    private final FileStorageService storageService;  // File storage

    // Lấy tất cả khóa học
    @GetMapping
    public ResponseEntity<List<CourseDTO>> getAllCourses() {
        return ResponseEntity.ok(courseService.getAllCourses());
    }
    // API cho người dùng - chỉ trả về các khóa học đang mở
    @GetMapping("/public")
    public ResponseEntity<List<CourseDTO>> getActiveCourses() {
        return ResponseEntity.ok(courseService.getActiveCourses());
    }

    // Lấy khóa học theo ID
    @GetMapping("/{id}")
    public ResponseEntity<CourseDTO> getCourseById(@PathVariable UUID id) {
        CourseDTO course = courseService.getCourseById(id);
        return course != null
                ? ResponseEntity.ok(course)
                : ResponseEntity.notFound().build();
    }

    // Tạo khóa học mới
    @PostMapping
    public ResponseEntity<CourseDTO> createCourse(@RequestBody CourseDTO courseDTO) {
        return ResponseEntity.ok(courseService.createCourse(courseDTO));
    }

    // Cập nhật khóa học
    @PutMapping("/{id}")
    public ResponseEntity<CourseDTO> updateCourse(
            @PathVariable UUID id,
            @RequestBody CourseDTO courseDTO
    ) {
        CourseDTO updated = courseService.updateCourse(id, courseDTO);
        return updated != null
                ? ResponseEntity.ok(updated)
                : ResponseEntity.notFound().build();
    }




    // Xóa khóa học
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCourse(@PathVariable UUID id) {
        courseService.deleteCourse(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search")
    public ResponseEntity<List<CourseDTO>> searchCourses(
            @RequestParam(value = "keyword", required = false) String keyword
    ) {
        List<CourseDTO> results;
        if (keyword != null && !keyword.isBlank()) {
            results = courseService.searchCourses(keyword);
        } else {
            results = courseService.getAllCourses();
        }
        return ResponseEntity.ok(results);
    }



    // Upload ảnh cho khóa học
    @PostMapping(
            path = "/{id}/image",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<CourseDTO> uploadImage(
            @PathVariable UUID id,
            @RequestParam("file") MultipartFile file
    ) {
        // 1. Lưu file và lấy filename
        String filename = storageService.store(file);
        // 2. Build URL truy cập file
        String url = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/files/")
                .path(filename)
                .toUriString();
        // 3. Cập nhật URL vào Course
        CourseDTO updated = courseService.updateImage(id, url);
        return ResponseEntity.ok(updated);
    }

}