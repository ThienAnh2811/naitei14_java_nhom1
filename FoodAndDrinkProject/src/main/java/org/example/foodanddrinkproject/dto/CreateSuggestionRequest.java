package org.example.foodanddrinkproject.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class CreateSuggestionRequest {
    
    @NotBlank(message = "Product name is required")
    @Size(max = 255, message = "Name must not exceed 255 characters")
    private String suggestedName;
    
    @Size(max = 2000, message = "Description must not exceed 2000 characters")
    private String description;
    
    private Integer categoryId;
    
    // For file upload
    private MultipartFile imageFile;
}