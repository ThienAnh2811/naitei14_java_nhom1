package org.example.foodanddrinkproject.entity;

import org.example.foodanddrinkproject.enums.SuggestionStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import java.time.LocalDateTime;

@Entity
@Table(name = "suggestions")
@Getter
@Setter
public class Suggestion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Suggested product name
    @Column(name = "suggested_name")
    private String suggestedName;

    // Product description
    @Lob
    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    // Keep old field for backward compatibility, but nullable
    @Lob
    @Column(name = "suggestion_text")
    private String suggestionText;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    @Column(name = "image_url", length = 512)
    private String imageUrl;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SuggestionStatus status = SuggestionStatus.PENDING;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}
