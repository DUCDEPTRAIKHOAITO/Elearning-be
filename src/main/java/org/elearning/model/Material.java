package org.elearning.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "material")
@Data
public class Material {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "lesson_id")
    private Lesson lesson;

    private String referenceLink;
    @Column(length = 200)
    private String title;

    @Column(length = 500)
    private String url;

    private Instant uploadAt;
}
