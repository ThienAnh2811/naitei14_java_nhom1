package org.example.foodanddrinkproject.dto;

import lombok.Data;
import java.util.Set;

@Data
public class AdminUpdateUserRequest {
    private String fullName;
    private String phoneNumber;
    private boolean isEnabled;
    private Set<String> roles; // Passing role names as strings for simplicity in form
}
