package org.elearning.respository;

import org.elearning.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface RoleRepository extends JpaRepository<Role, UUID> {
    // Tìm Role theo tên
    Optional<Role> findByName(String name);
}
