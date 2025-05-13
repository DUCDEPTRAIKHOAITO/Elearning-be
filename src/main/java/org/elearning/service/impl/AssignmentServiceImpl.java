package org.elearning.service.impl;

import lombok.RequiredArgsConstructor;
import org.elearning.dto.elearning.AssignmentDTO;
import org.elearning.model.Assignment;
import org.elearning.respository.AssignmentRepository;
import org.elearning.respository.CourseRepository;
import org.elearning.service.AssignmentService;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AssignmentServiceImpl implements AssignmentService {

    private final AssignmentRepository assignmentRepository;
    private final CourseRepository courseRepository;

    private AssignmentDTO convertToDTO(Assignment assignment) {
        AssignmentDTO dto = new AssignmentDTO();
        dto.setId(assignment.getId().toString());
        if (assignment.getCourse() != null) {
            dto.setCourseId(assignment.getCourse().getId().toString());
        }
        dto.setName(assignment.getName());
        dto.setDescription(assignment.getDescription());
        if (assignment.getDate() != null) {
            dto.setAssignmentDate(assignment.getDate().toString());
        }
        return dto;
    }

    @Override
    public List<AssignmentDTO> getAllAssignments() {
        return assignmentRepository.findAll()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public AssignmentDTO getAssignmentById(UUID id) {
        return assignmentRepository.findById(id)
                .map(this::convertToDTO)
                .orElse(null);
    }

    @Override
    public AssignmentDTO createAssignment(AssignmentDTO dto) {
        Assignment assignment = new Assignment();
        // map course
        if (dto.getCourseId() != null) {
            UUID courseId = UUID.fromString(dto.getCourseId());
            courseRepository.findById(courseId).ifPresent(assignment::setCourse);
        }
        assignment.setName(dto.getName());
        assignment.setDescription(dto.getDescription());
        // map date
        if (dto.getAssignmentDate() != null) {
            try {
                assignment.setDate(Instant.parse(dto.getAssignmentDate()));
            } catch (Exception e) {
                // ignore or handle invalid format
            }
        }
        Assignment saved = assignmentRepository.save(assignment);
        return convertToDTO(saved);
    }

    @Override
    public AssignmentDTO updateAssignment(UUID id, AssignmentDTO dto) {
        Optional<Assignment> existing = assignmentRepository.findById(id);
        if (existing.isPresent()) {
            Assignment assignment = existing.get();
            // map course
            if (dto.getCourseId() != null) {
                UUID courseId = UUID.fromString(dto.getCourseId());
                courseRepository.findById(courseId).ifPresent(assignment::setCourse);
            }
            assignment.setName(dto.getName());
            assignment.setDescription(dto.getDescription());
            if (dto.getAssignmentDate() != null) {
                try {
                    assignment.setDate(Instant.parse(dto.getAssignmentDate()));
                } catch (Exception ignored) {}
            }
            Assignment saved = assignmentRepository.save(assignment);
            return convertToDTO(saved);
        }
        return null;
    }

    @Override
    public void deleteAssignment(UUID id) {
        assignmentRepository.deleteById(id);
    }
}

