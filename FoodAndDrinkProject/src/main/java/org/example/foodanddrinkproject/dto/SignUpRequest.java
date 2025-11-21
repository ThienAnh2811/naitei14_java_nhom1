package org.example.foodanddrinkproject.dto;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class SignUpRequest {
    @NotBlank
    @Size(min = 4, max = 40)
    private String fullName;


    @NotBlank
    @Email
    private String email;


    @NotBlank
    @Size(min = 6, max = 20)
    private String password;
}
