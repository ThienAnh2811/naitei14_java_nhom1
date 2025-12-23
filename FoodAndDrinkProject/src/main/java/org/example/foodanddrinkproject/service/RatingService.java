package org.example.foodanddrinkproject.service;

import org.example.foodanddrinkproject.dto.CreateRatingRequest;
import org.example.foodanddrinkproject.dto.ProductRatingSummaryDto;
import org.example.foodanddrinkproject.dto.RatingDto;
import org.example.foodanddrinkproject.entity.Rating;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface RatingService {
    Page<Rating> getAllRatings(Pageable pageable);
    
    // Admin: get all ratings with optional product filter
    Page<Rating> getAllRatingsFiltered(Long productId, Pageable pageable);
    
    void deleteRating(Long id);
    
    // Admin: get all ratings for a product
    Page<RatingDto> getRatingsByProductId(Long productId, Pageable pageable);
    
    // User: get aggregate rating summary + own rating
    ProductRatingSummaryDto getProductRatingSummary(Long productId, Long userId);
    
    RatingDto createRating(Long userId, Long productId, CreateRatingRequest request);
}
