package org.example.foodanddrinkproject.service;

import org.example.foodanddrinkproject.dto.CreateSuggestionRequest;
import org.example.foodanddrinkproject.dto.ProductSuggestionDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProductSuggestionService {
    void createSuggestion(Long userId, CreateSuggestionRequest request, String imageUrl);
    Page<ProductSuggestionDto> getAllSuggestions(Pageable pageable);
    void deleteSuggestion(Long id);
}