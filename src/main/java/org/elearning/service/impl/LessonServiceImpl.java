package org.elearning.service.impl;

import lombok.RequiredArgsConstructor;
import org.elearning.dto.elearning.LessonDTO;
import org.elearning.model.Lesson;
import org.elearning.respository.LessonRepository;
import org.elearning.respository.CourseRepository;
import org.elearning.service.LessonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LessonServiceImpl implements LessonService {

    private final LessonRepository lessonRepository;
    private final CourseRepository courseRepository;

    private LessonDTO convertToDTO(Lesson lesson) {
        LessonDTO dto = new LessonDTO();
        dto.setId(lesson.getId().toString());
        dto.setCourseId(lesson.getCourse().getId().toString());
        dto.setName(lesson.getName());
        dto.setDescription(lesson.getDescription());
        dto.setReferenceLink(lesson.getReferenceLink());
        if (lesson.getLessonDate() != null) {
            dto.setLessonDate(lesson.getLessonDate().toString());
        }
        return dto;
    }

    @Override
    public List<LessonDTO> getAllLessons() {
        return lessonRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public LessonDTO getLessonById(UUID id) {
        return lessonRepository.findById(id)
                .map(this::convertToDTO)
                .orElse(null);
    }

    @Override
    public LessonDTO createLesson(LessonDTO lessonDTO) {
        Lesson lesson = new Lesson();
        // Map course relation
        UUID courseId = UUID.fromString(lessonDTO.getCourseId());
        courseRepository.findById(courseId).ifPresent(lesson::setCourse);
        // Map other fields
        lesson.setName(lessonDTO.getName());
        lesson.setDescription(lessonDTO.getDescription());
        lesson.setReferenceLink(lessonDTO.getReferenceLink());
        if (lessonDTO.getLessonDate() != null) {
            lesson.setLessonDate(Instant.parse(lessonDTO.getLessonDate()));
        }
        lesson = lessonRepository.save(lesson);
        return convertToDTO(lesson);
    }

    @Override
    public LessonDTO updateLesson(UUID id, LessonDTO lessonDTO) {
        Optional<Lesson> existing = lessonRepository.findById(id);
        if (existing.isPresent()) {
            Lesson lesson = existing.get();
            // Map course
            UUID courseId = UUID.fromString(lessonDTO.getCourseId());
            courseRepository.findById(courseId).ifPresent(lesson::setCourse);
            // Map updated fields
            lesson.setName(lessonDTO.getName());
            lesson.setDescription(lessonDTO.getDescription());
            lesson.setReferenceLink(lessonDTO.getReferenceLink());
            if (lessonDTO.getLessonDate() != null) {
                lesson.setLessonDate(Instant.parse(lessonDTO.getLessonDate()));
            }
            lesson = lessonRepository.save(lesson);
            return convertToDTO(lesson);
        }
        return null;
    }

    @Override
    public void deleteLesson(UUID id) {
        lessonRepository.deleteById(id);
    }
}

