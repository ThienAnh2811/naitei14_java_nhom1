package org.example.foodanddrinkproject.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RatingDto {
    private Long id;
    private int ratingValue;
    private String comment;
    private LocalDateTime createdAt;
    private Long userId;
    private String userFullName;
    private Long productId;
}
