package org.elearning.respository;

import org.elearning.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
    // Tìm User theo Email
    Optional<User> findByEmail(String email);

    // Tìm User theo Role
    List<User> findByRole_Name(String roleName);
}