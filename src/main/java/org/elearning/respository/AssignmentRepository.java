package org.elearning.respository;

import org.elearning.model.Assignment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface AssignmentRepository extends JpaRepository<Assignment, UUID> {
    // Tìm Assignment theo Course
    List<Assignment> findByCourseId(UUID courseId);

    // Tìm Assignment theo tên
    List<Assignment> findByName(String name);
}