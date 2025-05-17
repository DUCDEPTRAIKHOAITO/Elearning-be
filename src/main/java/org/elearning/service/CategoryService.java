package org.elearning.service;

import org.elearning.dto.elearning.CategoryDTO;
import org.elearning.enums.CategoryStatus;

import java.util.List;
import java.util.UUID;

public interface CategoryService {
    List<CategoryDTO> getAllCategories();
    CategoryDTO getCategoryById(UUID id);
    CategoryDTO createCategory(CategoryDTO categoryDTO);
    CategoryDTO updateCategory(UUID id, CategoryDTO categoryDTO);
    void deleteCategory(UUID id);
    CategoryDTO updateStatus(UUID id, CategoryStatus newStatus);
}
