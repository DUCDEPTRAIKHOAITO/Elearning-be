package org.elearning.service.impl;

import lombok.RequiredArgsConstructor;
import org.elearning.dto.elearning.EnrollmentDTO;
import org.elearning.enums.EnrollmentStatus;
import org.elearning.model.Enrollment;
import org.elearning.respository.EnrollmentRepository;
import org.elearning.respository.CourseRepository;
import org.elearning.respository.LearnerRepository;
import org.elearning.service.EnrollmentService;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EnrollmentServiceImpl implements EnrollmentService {

    private final EnrollmentRepository enrollmentRepository;
    private final CourseRepository courseRepository;
    private final LearnerRepository learnerRepository;

    // Convert Enrollment entity → DTO
    private EnrollmentDTO convertToDTO(Enrollment enrollment) {
        EnrollmentDTO dto = new EnrollmentDTO();
        dto.setId(enrollment.getId().toString());
        if (enrollment.getLearner() != null) {
            dto.setLearnerId(enrollment.getLearner().getId().toString());
            dto.setLearnerName(enrollment.getLearner().getUser().getName());
            // assuming Learner has getUser().getUsername()
        }
        if (enrollment.getCourse() != null) {
            dto.setCourseId(enrollment.getCourse().getId().toString());
        }
        if (enrollment.getEnrollmentDate() != null) {
            dto.setEnrollmentDate(enrollment.getEnrollmentDate().toString());
        }
        if (enrollment.getStatus() != null) {
            dto.setEnrollmentStatus(enrollment.getStatus().name());
        }
        return dto;
    }

    @Override
    public List<EnrollmentDTO> getAllEnrollments() {
        return enrollmentRepository.findAll()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public EnrollmentDTO getEnrollmentById(UUID id) {
        return enrollmentRepository.findById(id)
                .map(this::convertToDTO)
                .orElse(null);
    }

    @Override
    public EnrollmentDTO createEnrollment(EnrollmentDTO dto) {
        Enrollment enrollment = new Enrollment();

        // Map relations
        UUID learnerId = UUID.fromString(dto.getLearnerId());
        learnerRepository.findById(learnerId).ifPresent(enrollment::setLearner);

        UUID courseId = UUID.fromString(dto.getCourseId());
        courseRepository.findById(courseId).ifPresent(enrollment::setCourse);

        // Map date
        if (dto.getEnrollmentDate() != null) {
            enrollment.setEnrollmentDate(Instant.parse(dto.getEnrollmentDate()));
        } else {
            enrollment.setEnrollmentDate(Instant.now());
        }

        // Set status
        if (dto.getEnrollmentStatus() != null) {
            enrollment.setStatus(EnrollmentStatus.valueOf(dto.getEnrollmentStatus()));
        } else {
            enrollment.setStatus(EnrollmentStatus.PENDING);
        }

        enrollment = enrollmentRepository.save(enrollment);
        return convertToDTO(enrollment);
    }

    @Override
    public EnrollmentDTO updateEnrollment(UUID id, EnrollmentDTO dto) {
        Optional<Enrollment> existing = enrollmentRepository.findById(id);
        if (existing.isPresent()) {
            Enrollment enrollment = existing.get();

            // Update relations
            UUID learnerId = UUID.fromString(dto.getLearnerId());
            learnerRepository.findById(learnerId).ifPresent(enrollment::setLearner);

            UUID courseId = UUID.fromString(dto.getCourseId());
            courseRepository.findById(courseId).ifPresent(enrollment::setCourse);

            // Update date
            if (dto.getEnrollmentDate() != null) {
                enrollment.setEnrollmentDate(Instant.parse(dto.getEnrollmentDate()));
            }

            // Update status
            if (dto.getEnrollmentStatus() != null) {
                enrollment.setStatus(EnrollmentStatus.valueOf(dto.getEnrollmentStatus()));
            }

            enrollment = enrollmentRepository.save(enrollment);
            return convertToDTO(enrollment);
        }
        return null;
    }

    @Override
    public void deleteEnrollment(UUID id) {
        enrollmentRepository.deleteById(id);
    }

    // ✅ Lấy danh sách học viên đã đăng ký theo khóa học
    @Override
    public List<EnrollmentDTO> getEnrollmentsByCourse(UUID courseId) {
        return enrollmentRepository.findByCourseId(courseId)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // ✅ Cập nhật trạng thái đăng ký (admin duyệt hoặc từ chối)
    @Override
    public EnrollmentDTO updateEnrollmentStatus(UUID enrollmentId, EnrollmentStatus status) {
        Enrollment enrollment = enrollmentRepository.findById(enrollmentId)
                .orElseThrow(() -> new RuntimeException("Enrollment not found"));
        enrollment.setStatus(status);
        enrollmentRepository.save(enrollment);
        return convertToDTO(enrollment);
    }
}
//test checkgithub
