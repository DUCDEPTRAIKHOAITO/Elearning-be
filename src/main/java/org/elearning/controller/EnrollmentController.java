package org.elearning.controller;

import lombok.RequiredArgsConstructor;
import org.elearning.dto.elearning.EnrollmentDTO;
import org.elearning.enums.EnrollmentStatus;
import org.elearning.service.EnrollmentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/enrollments")
@RequiredArgsConstructor
public class EnrollmentController {

    private final EnrollmentService enrollmentService;

    // Lấy tất cả bản ghi ghi danh
    @GetMapping
    public ResponseEntity<List<EnrollmentDTO>> getAllEnrollments() {
        return ResponseEntity.ok(enrollmentService.getAllEnrollments());
    }

    // Lấy ghi danh theo ID
    @GetMapping("/{id}")
    public ResponseEntity<EnrollmentDTO> getEnrollmentById(@PathVariable UUID id) {
        EnrollmentDTO enrollment = enrollmentService.getEnrollmentById(id);
        return enrollment != null ? ResponseEntity.ok(enrollment) : ResponseEntity.notFound().build();
    }

    // Tạo ghi danh mới
    @PostMapping
    public ResponseEntity<EnrollmentDTO> createEnrollment(@RequestBody EnrollmentDTO enrollmentDTO) {
        return ResponseEntity.ok(enrollmentService.createEnrollment(enrollmentDTO));
    }

    // Cập nhật ghi danh
    @PutMapping("/{id}")
    public ResponseEntity<EnrollmentDTO> updateEnrollment(@PathVariable UUID id, @RequestBody EnrollmentDTO enrollmentDTO) {
        EnrollmentDTO updated = enrollmentService.updateEnrollment(id, enrollmentDTO);
        return updated != null ? ResponseEntity.ok(updated) : ResponseEntity.notFound().build();
    }

    // Xóa ghi danh
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEnrollment(@PathVariable UUID id) {
        enrollmentService.deleteEnrollment(id);
        return ResponseEntity.noContent().build();
    }

    // ✅ Lấy danh sách ghi danh theo khóa học (Admin)
    @GetMapping("/course/{courseId}/enrollments")
    public ResponseEntity<List<EnrollmentDTO>> getEnrollmentsByCourse(@PathVariable UUID courseId) {
        return ResponseEntity.ok(enrollmentService.getEnrollmentsByCourse(courseId));
    }

    // ✅ Cập nhật trạng thái ghi danh (duyệt / hủy)
    @PutMapping("/{enrollmentId}/status")
    public ResponseEntity<EnrollmentDTO> updateEnrollmentStatus(
            @PathVariable UUID enrollmentId,
            @RequestParam EnrollmentStatus status) {
        return ResponseEntity.ok(enrollmentService.updateEnrollmentStatus(enrollmentId, status));
    }
}
