package org.elearning.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.elearning.dto.elearning.CategoryDTO;
import org.elearning.enums.CategoryStatus;
import org.elearning.model.Category;
import org.elearning.respository.CategoryRepository;
import org.elearning.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    @Override
    public List<CategoryDTO> getAllCategories() {
        return categoryRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public CategoryDTO getCategoryById(UUID id) {
        return categoryRepository.findById(id)
                .map(this::convertToDTO)
                .orElse(null);
    }

    @Override
    @Transactional
    public CategoryDTO createCategory(CategoryDTO categoryDTO) {
        Category category = new Category();
        category.setCategoryName(categoryDTO.getCategoryName());
        category.setDescription(categoryDTO.getDescription());
        // Gán default status nếu client không truyền
        if (categoryDTO.getStatus() == null) {
            category.setStatus(CategoryStatus.ACTIVE);
        } else {
            category.setStatus(categoryDTO.getStatus());
        }
        Category saved = categoryRepository.save(category);
        return convertToDTO(saved);
    }

    @Override
    @Transactional
    public CategoryDTO updateCategory(UUID id, CategoryDTO categoryDTO) {
        Optional<Category> opt = categoryRepository.findById(id);
        if (!opt.isPresent()) {
            return null;
        }
        Category category = opt.get();
        category.setCategoryName(categoryDTO.getCategoryName());
        category.setDescription(categoryDTO.getDescription());

        // Nếu client có truyền status, kiểm tra transition rồi mới gán
        if (categoryDTO.getStatus() != null
                && !category.getStatus().equals(categoryDTO.getStatus())) {
            CategoryStatus newStatus = categoryDTO.getStatus();
            if (!category.getStatus().canTransitionTo(newStatus)) {
                throw new IllegalStateException(
                        String.format("Cannot transition status from %s to %s",
                                category.getStatus(), newStatus));
            }
            category.setStatus(newStatus);
        }

        Category updated = categoryRepository.save(category);
        return convertToDTO(updated);
    }

    @Override
    @Transactional
    public void deleteCategory(UUID id) {
        categoryRepository.deleteById(id);
    }

    @Override
    @Transactional
    public CategoryDTO updateStatus(UUID id, CategoryStatus newStatus) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Category not found: " + id));

        CategoryStatus current = category.getStatus();
        if (current.equals(newStatus)) {
            return convertToDTO(category);
        }
        if (!current.canTransitionTo(newStatus)) {
            throw new IllegalStateException(
                    String.format("Cannot transition status from %s to %s", current, newStatus));
        }
        category.setStatus(newStatus);
        Category saved = categoryRepository.save(category);
        return convertToDTO(saved);
    }

    // Chuyển entity sang DTO nội tuyến, có thêm field status
    private CategoryDTO convertToDTO(Category category) {
        CategoryDTO dto = new CategoryDTO();
        dto.setId(category.getId().toString());
        dto.setCategoryName(category.getCategoryName());
        dto.setDescription(category.getDescription());
        dto.setStatus(category.getStatus());
        return dto;
    }
}
