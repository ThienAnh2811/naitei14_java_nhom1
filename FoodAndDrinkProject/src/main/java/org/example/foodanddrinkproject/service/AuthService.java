package org.example.foodanddrinkproject.service;


import org.example.foodanddrinkproject.dto.JwtAuthResponse;
import org.example.foodanddrinkproject.dto.LoginRequest;
import org.example.foodanddrinkproject.dto.SignUpRequest;
import org.springframework.http.ResponseEntity;


public interface AuthService {
    JwtAuthResponse loginUser(LoginRequest loginRequest);
    ResponseEntity<?> registerUser(SignUpRequest signUpRequest);
}
