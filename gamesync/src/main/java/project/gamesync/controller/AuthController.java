package project.gamesync.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project.gamesync.dto.request.LoginRequest;
import project.gamesync.dto.request.RegisterRequest;
import project.gamesync.dto.response.JwtResponse;
import project.gamesync.dto.response.MessageResponse;
import project.gamesync.service.AuthService;


@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    AuthService authService;

    // Endpoint: POST /api/auth/signin
    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        JwtResponse jwtResponse = authService.authenticateUser(loginRequest);
        return ResponseEntity.ok(jwtResponse);
    }

    // Endpoint: POST /api/auth/signup
    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody RegisterRequest signUpRequest) {
        try {
            authService.registerUser(signUpRequest);
            return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
        } catch (Exception e) {
            // Mengambil pesan error dari service (misal: "Error: Username is already taken!")
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }
}