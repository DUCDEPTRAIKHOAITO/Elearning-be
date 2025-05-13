package org.elearning.service.impl;

import lombok.RequiredArgsConstructor;
import org.elearning.dto.elearning.LearnerDTO;
import org.elearning.model.Learner;
import org.elearning.respository.LearnerRepository;
import org.elearning.respository.UserRepository;
import org.elearning.enums.LearnerStatus;
import org.elearning.service.LearnerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LearnerServiceImpl implements LearnerService {

    private final LearnerRepository learnerRepository;
    private final UserRepository userRepository;

    // Convert Learner entity to LearnerDTO
    private LearnerDTO convertToDTO(Learner learner) {
        LearnerDTO dto = new LearnerDTO();
        dto.setId(learner.getId().toString());  // Convert UUID to String
        // Guard against null user
        if (learner.getUser() != null) {
            dto.setUserId(learner.getUser().getId().toString());
        }
        dto.setLearnerStatus(learner.getStatus().name());  // Convert enum to string
        return dto;
    }

    @Override
    public LearnerDTO createLearner(LearnerDTO learnerDTO) {
        Learner learner = new Learner();
        learner.setStatus(LearnerStatus.valueOf(learnerDTO.getLearnerStatus()));
        // Correctly map userId from DTO
        UUID userId = UUID.fromString(learnerDTO.getUserId());
        userRepository.findById(userId).ifPresent(learner::setUser);
        learner = learnerRepository.save(learner);
        return convertToDTO(learner);
    }

    @Override
    public LearnerDTO updateLearner(UUID id, LearnerDTO learnerDTO) {
        Optional<Learner> existing = learnerRepository.findById(id);
        if (existing.isPresent()) {
            Learner learner = existing.get();
            learner.setStatus(LearnerStatus.valueOf(learnerDTO.getLearnerStatus()));
            // Correctly map userId from DTO instead of DTO id
            UUID userId = UUID.fromString(learnerDTO.getUserId());
            userRepository.findById(userId).ifPresent(learner::setUser);
            learner = learnerRepository.save(learner);
            return convertToDTO(learner);
        }
        return null;
    }

    @Override
    public List<LearnerDTO> getAllLearners() {
        return learnerRepository.findAll()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public LearnerDTO getLearnerById(UUID id) {
        return learnerRepository.findById(id)
                .map(this::convertToDTO)
                .orElse(null);
    }

    @Override
    public void deleteLearner(UUID id) {
        learnerRepository.deleteById(id);
    }
}

