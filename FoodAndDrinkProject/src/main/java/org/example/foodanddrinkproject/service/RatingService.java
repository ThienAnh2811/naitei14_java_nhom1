package org.example.foodanddrinkproject.service;

import org.example.foodanddrinkproject.entity.Rating;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface RatingService {
    Page<Rating> getAllRatings(Pageable pageable);
    void deleteRating(Long id);
}
