package org.elearning.service.impl;

import lombok.RequiredArgsConstructor;
import org.elearning.dto.elearning.MaterialDTO;
import org.elearning.model.Material;
import org.elearning.respository.MaterialRepository;
import org.elearning.respository.LessonRepository;
import org.elearning.service.MaterialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MaterialServiceImpl implements MaterialService {

    private final MaterialRepository materialRepository;
    private final LessonRepository lessonRepository;

    private MaterialDTO convertToDTO(Material material) {
        MaterialDTO dto = new MaterialDTO();
        dto.setId(material.getId().toString());
        if (material.getLesson() != null) {
            dto.setLessonId(material.getLesson().getId().toString());
        }
        dto.setReferenceLink(material.getReferenceLink());
        dto.setTitle(material.getTitle());
        dto.setUrl(material.getUrl());
        // map uploadAt
        if (material.getUploadAt() != null) {
            dto.setUploadAt(material.getUploadAt().toString());
        }
        return dto;
    }

    @Override
    public List<MaterialDTO> getAllMaterials() {
        return materialRepository.findAll()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public MaterialDTO getMaterialById(UUID id) {
        return materialRepository.findById(id)
                .map(this::convertToDTO)
                .orElse(null);
    }

    @Override
    public MaterialDTO createMaterial(MaterialDTO dto) {
        Material material = new Material();
        // map lesson
        UUID lessonId = UUID.fromString(dto.getLessonId());
        lessonRepository.findById(lessonId).ifPresent(material::setLesson);
        material.setReferenceLink(dto.getReferenceLink());
        material.setTitle(dto.getTitle());
        material.setUrl(dto.getUrl());
        // set uploadAt: use provided or now
        if (dto.getUploadAt() != null) {
            material.setUploadAt(Instant.parse(dto.getUploadAt()));
        } else {
            material.setUploadAt(Instant.now());
        }
        material = materialRepository.save(material);
        return convertToDTO(material);
    }

    @Override
    public MaterialDTO updateMaterial(UUID id, MaterialDTO dto) {
        Optional<Material> existing = materialRepository.findById(id);
        if (existing.isPresent()) {
            Material material = existing.get();
            UUID lessonId = UUID.fromString(dto.getLessonId());
            lessonRepository.findById(lessonId).ifPresent(material::setLesson);
            material.setReferenceLink(dto.getReferenceLink());
            material.setTitle(dto.getTitle());
            material.setUrl(dto.getUrl());
            // update uploadAt if provided
            if (dto.getUploadAt() != null) {
                material.setUploadAt(Instant.parse(dto.getUploadAt()));
            }
            material = materialRepository.save(material);
            return convertToDTO(material);
        }
        return null;
    }

    @Override
    public void deleteMaterial(UUID id) {
        materialRepository.deleteById(id);
    }
}

