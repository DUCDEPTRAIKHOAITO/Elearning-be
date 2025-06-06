package org.elearning.respository;

import org.elearning.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface CategoryRepository extends JpaRepository<Category, UUID> {
    // Custom queries if needed
}
