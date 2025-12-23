package org.example.foodanddrinkproject.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductRatingSummaryDto {
    private Long productId;
    private double averageRating;
    private long totalReviews;
    private RatingDto userRating; // Current user's rating, null if not rated
}
