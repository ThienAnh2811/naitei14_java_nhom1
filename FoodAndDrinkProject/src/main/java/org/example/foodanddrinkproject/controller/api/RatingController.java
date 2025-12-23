package org.example.foodanddrinkproject.controller.api;

import jakarta.validation.Valid;
import org.example.foodanddrinkproject.dto.CreateRatingRequest;
import org.example.foodanddrinkproject.dto.ProductRatingSummaryDto;
import org.example.foodanddrinkproject.dto.RatingDto;
import org.example.foodanddrinkproject.security.CurrentUser;
import org.example.foodanddrinkproject.security.UserPrincipal;
import org.example.foodanddrinkproject.service.RatingService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ratings")
public class RatingController {

    private final RatingService ratingService;

    public RatingController(RatingService ratingService) {
        this.ratingService = ratingService;
    }

    /**
     * Get aggregate rating summary for a product (public endpoint)
     * Shows: average rating, total reviews count, and current user's own rating if authenticated
     */
    @GetMapping("/{productId}")
    public ResponseEntity<ProductRatingSummaryDto> getProductRatingSummary(
            @PathVariable Long productId,
            @CurrentUser UserPrincipal currentUser) {
        Long userId = currentUser != null ? currentUser.getId() : null;
        return ResponseEntity.ok(ratingService.getProductRatingSummary(productId, userId));
    }

    @PostMapping("/{productId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<RatingDto> createRating(
            @CurrentUser UserPrincipal currentUser,
            @PathVariable Long productId,
            @Valid @RequestBody CreateRatingRequest request) {
        RatingDto rating = ratingService.createRating(currentUser.getId(), productId, request);
        return ResponseEntity.ok(rating);
    }
}
