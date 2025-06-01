package org.elearning.service;

import org.elearning.dto.elearning.EnrollmentDTO;
import org.elearning.enums.EnrollmentStatus;

import java.util.List;
import java.util.UUID;

public interface EnrollmentService {
    List<EnrollmentDTO> getAllEnrollments();
    EnrollmentDTO getEnrollmentById(UUID id);
    EnrollmentDTO createEnrollment(EnrollmentDTO enrollmentDTO);
    EnrollmentDTO updateEnrollment(UUID id, EnrollmentDTO enrollmentDTO);
    void deleteEnrollment(UUID id);
    List<EnrollmentDTO> getEnrollmentsByCourse(UUID courseId);

    // ✅ Admin duyệt hoặc từ chối
    EnrollmentDTO updateEnrollmentStatus(UUID enrollmentId, EnrollmentStatus status);
}
