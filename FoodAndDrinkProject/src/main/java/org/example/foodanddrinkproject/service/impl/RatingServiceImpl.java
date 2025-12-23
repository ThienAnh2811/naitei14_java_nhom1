package org.example.foodanddrinkproject.service.impl;

import org.example.foodanddrinkproject.dto.CreateRatingRequest;
import org.example.foodanddrinkproject.dto.ProductRatingSummaryDto;
import org.example.foodanddrinkproject.dto.RatingDto;
import org.example.foodanddrinkproject.entity.Product;
import org.example.foodanddrinkproject.entity.Rating;
import org.example.foodanddrinkproject.entity.User;
import org.example.foodanddrinkproject.repository.ProductRepository;
import org.example.foodanddrinkproject.repository.RatingRepository;
import org.example.foodanddrinkproject.repository.UserRepository;
import org.example.foodanddrinkproject.service.RatingService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RatingServiceImpl implements RatingService {

    private final RatingRepository ratingRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    public RatingServiceImpl(RatingRepository ratingRepository,
                             ProductRepository productRepository,
                             UserRepository userRepository) {
        this.ratingRepository = ratingRepository;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
    }

    @Override
    public Page<Rating> getAllRatings(Pageable pageable) {
        return ratingRepository.findAll(pageable);
    }

    @Override
    public Page<Rating> getAllRatingsFiltered(Long productId, Pageable pageable) {
        if (productId != null) {
            return ratingRepository.findByProductId(productId, pageable);
        }
        return ratingRepository.findAll(pageable);
    }

    @Override
    public void deleteRating(Long id) {
        if (ratingRepository.existsById(id)) {
            ratingRepository.deleteById(id);
        }
    }

    @Override
    public Page<RatingDto> getRatingsByProductId(Long productId, Pageable pageable) {
        return ratingRepository.findByProductId(productId, pageable)
                .map(this::toRatingDto);
    }

    @Override
    public ProductRatingSummaryDto getProductRatingSummary(Long productId, Long userId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        
        Page<Rating> allRatings = ratingRepository.findByProductId(productId, Pageable.unpaged());
        long totalReviews = allRatings.getTotalElements();
        
        // Get user's own rating if userId is provided
        RatingDto userRating = null;
        if (userId != null) {
            userRating = ratingRepository.findByUserIdAndProductId(userId, productId)
                    .map(this::toRatingDto)
                    .orElse(null);
        }
        
        return ProductRatingSummaryDto.builder()
                .productId(productId)
                .averageRating(product.getAvgRating())
                .totalReviews(totalReviews)
                .userRating(userRating)
                .build();
    }

    @Override
    @Transactional
    public RatingDto createRating(Long userId, Long productId, CreateRatingRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        
        // Check if user already rated this product - update if exists
        Rating rating = ratingRepository.findByUserIdAndProductId(userId, productId)
                .orElse(new Rating());
        
        rating.setUser(user);
        rating.setProduct(product);
        rating.setRatingValue(request.getRatingValue());
        rating.setComment(request.getComment());
        
        Rating savedRating = ratingRepository.save(rating);
        
        // Update product average rating
        updateProductAverageRating(product);
        
        return toRatingDto(savedRating);
    }
    
    private void updateProductAverageRating(Product product) {
        Page<Rating> ratings = ratingRepository.findByProductId(product.getId(), Pageable.unpaged());
        double avgRating = ratings.getContent().stream()
                .mapToInt(Rating::getRatingValue)
                .average()
                .orElse(0.0);
        product.setAvgRating(avgRating);
        productRepository.save(product);
    }
    
    private RatingDto toRatingDto(Rating rating) {
        return RatingDto.builder()
                .id(rating.getId())
                .ratingValue(rating.getRatingValue())
                .comment(rating.getComment())
                .createdAt(rating.getCreatedAt())
                .userId(rating.getUser().getId())
                .userFullName(rating.getUser().getFullName())
                .productId(rating.getProduct().getId())
                .build();
    }
}
