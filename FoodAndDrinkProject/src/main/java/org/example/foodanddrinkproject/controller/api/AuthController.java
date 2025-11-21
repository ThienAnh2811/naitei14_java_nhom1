package org.example.foodanddrinkproject.controller.api;


import org.example.foodanddrinkproject.dto.JwtAuthResponse;
import org.example.foodanddrinkproject.dto.LoginRequest;
import org.example.foodanddrinkproject.dto.SignUpRequest;
import org.example.foodanddrinkproject.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/auth")
public class AuthController {


    private final AuthService authService;


    public AuthController(AuthService authService) {
        this.authService = authService;
    }


    @PostMapping("/login")
    public ResponseEntity<JwtAuthResponse> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        return ResponseEntity.ok(authService.loginUser(loginRequest));
    }


    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignUpRequest signUpRequest) {
        return authService.registerUser(signUpRequest);
    }


    @GetMapping("/success")
    public ResponseEntity<JwtAuthResponse> oauthLoginSuccess(@RequestParam("token") String token) {
        // We just return the token so the user (or frontend) can grab it
        return ResponseEntity.ok(new JwtAuthResponse(token));
    }
}
