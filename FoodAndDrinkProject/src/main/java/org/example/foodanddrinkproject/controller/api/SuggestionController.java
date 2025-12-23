package org.example.foodanddrinkproject.controller.api;

import jakarta.validation.Valid;
import org.example.foodanddrinkproject.dto.ApiResponse;
import org.example.foodanddrinkproject.dto.CreateSuggestionRequest;
import org.example.foodanddrinkproject.security.UserPrincipal;
import org.example.foodanddrinkproject.service.FileStorageService;
import org.example.foodanddrinkproject.service.ProductSuggestionService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/suggestions")
public class SuggestionController {

    private final ProductSuggestionService suggestionService;
    private final FileStorageService fileStorageService;

    public SuggestionController(ProductSuggestionService suggestionService,
                                FileStorageService fileStorageService) {
        this.suggestionService = suggestionService;
        this.fileStorageService = fileStorageService;
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<ApiResponse> createSuggestion(
            @AuthenticationPrincipal UserPrincipal currentUser,
            @Valid @ModelAttribute CreateSuggestionRequest request) {

        // Handle image upload if provided
        String imageUrl = null;
        if (request.getImageFile() != null && !request.getImageFile().isEmpty()) {
            imageUrl = fileStorageService.storeFile(request.getImageFile(), "suggestions");
        }

        suggestionService.createSuggestion(currentUser.getId(), request, imageUrl);
        return ResponseEntity.ok(new ApiResponse(true, "Product suggestion submitted successfully!"));
    }
}
