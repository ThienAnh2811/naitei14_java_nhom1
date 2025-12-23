package org.example.foodanddrinkproject.dto;

import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter
@Setter
public class ProductSuggestionDto {
    private Long id;
    private Long userId;
    private String userName;
    private String suggestedName;
    private String description;
    private Integer categoryId;
    private String categoryName;
    private String imageUrl;
    private LocalDateTime createdAt;
}