package org.example.foodanddrinkproject.service.impl;

import org.example.foodanddrinkproject.dto.CreateSuggestionRequest;
import org.example.foodanddrinkproject.dto.ProductSuggestionDto;
import org.example.foodanddrinkproject.entity.Category;
import org.example.foodanddrinkproject.entity.Suggestion;
import org.example.foodanddrinkproject.entity.User;
import org.example.foodanddrinkproject.exception.ResourceNotFoundException;
import org.example.foodanddrinkproject.repository.CategoryRepository;
import org.example.foodanddrinkproject.repository.SuggestionRepository;
import org.example.foodanddrinkproject.repository.UserRepository;
import org.example.foodanddrinkproject.service.ProductSuggestionService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProductSuggestionServiceImpl implements ProductSuggestionService {

    private final SuggestionRepository suggestionRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;

    public ProductSuggestionServiceImpl(SuggestionRepository suggestionRepository, 
                                        CategoryRepository categoryRepository, 
                                        UserRepository userRepository) {
        this.suggestionRepository = suggestionRepository;
        this.categoryRepository = categoryRepository;
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public void createSuggestion(Long userId, CreateSuggestionRequest request, String imageUrl) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));

        Suggestion suggestion = new Suggestion();
        suggestion.setUser(user);
        suggestion.setSuggestedName(request.getSuggestedName());
        suggestion.setDescription(request.getDescription());
        suggestion.setImageUrl(imageUrl);
        // Set legacy field to satisfy NOT NULL constraint in database
        suggestion.setSuggestionText(request.getSuggestedName() != null ? request.getSuggestedName() : "New product suggestion");
        
        // Set category if provided
        if (request.getCategoryId() != null) {
            Category category = categoryRepository.findById(request.getCategoryId())
                    .orElseThrow(() -> new ResourceNotFoundException("Category", "id", request.getCategoryId()));
            suggestion.setCategory(category);
        }

        suggestionRepository.save(suggestion);
    }

    @Override
    public Page<ProductSuggestionDto> getAllSuggestions(Pageable pageable) {
        return suggestionRepository.findAllByOrderByCreatedAtDesc(pageable)
                .map(this::convertToDto);
    }

    @Override
    public void deleteSuggestion(Long id) {
        if (suggestionRepository.existsById(id)) {
            suggestionRepository.deleteById(id);
        }
    }

    private ProductSuggestionDto convertToDto(Suggestion entity) {
        ProductSuggestionDto dto = new ProductSuggestionDto();
        dto.setId(entity.getId());
        dto.setUserId(entity.getUser().getId());
        dto.setUserName(entity.getUser().getFullName());
        dto.setSuggestedName(entity.getSuggestedName());
        dto.setDescription(entity.getDescription());
        dto.setImageUrl(entity.getImageUrl());
        dto.setCreatedAt(entity.getCreatedAt());
        
        if (entity.getCategory() != null) {
            dto.setCategoryId(entity.getCategory().getId());
            dto.setCategoryName(entity.getCategory().getName());
        }
        
        return dto;
    }
}