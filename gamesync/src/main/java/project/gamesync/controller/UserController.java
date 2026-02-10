package project.gamesync.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import project.gamesync.dto.request.UpdateProfileRequest;
import project.gamesync.entity.User;

import project.gamesync.security.services.UserDetailsImpl;
import project.gamesync.service.UserService;
import project.gamesync.security.jwt.JwtUtils; // [1] WAJIB IMPORT INI

import java.util.HashMap;
import java.util.Map;

//@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    // [2] Inject JwtUtils untuk bikin token baru
    @Autowired
    private JwtUtils jwtUtils;

    // --- 1. GET CURRENT PROFILE ---
    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser() {
        try {
            UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            User user = userService.getUserById(userDetails.getId());
            return ResponseEntity.ok(user);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error fetching profile: " + e.getMessage());
        }
    }

    // --- 2. UPDATE PROFILE (DENGAN TOKEN BARU) ---
    @PutMapping("/{id}")
    public ResponseEntity<?> updateUser(@PathVariable Long id,
                                        @RequestBody UpdateProfileRequest request) {
        try {
            // Ambil ID User yang sedang login
            UserDetailsImpl currentUser = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            Long requesterId = currentUser.getId();

            // 1. Update Data di Database via Service
            User updatedUser = userService.updateUserProfile(id, requesterId, request);

            // 2. GENERATE TOKEN BARU (PENTING!)
            // Token dibuat berdasarkan Username baru
            String newToken = jwtUtils.generateTokenFromUsername(updatedUser.getUsername());

            // 3. Buat Response Custom (User Data + Token)
            // Kita pakai Map saja biar tidak perlu bikin Class DTO baru
            Map<String, Object> response = new HashMap<>();
            response.put("id", updatedUser.getId());
            response.put("username", updatedUser.getUsername());
            response.put("email", updatedUser.getEmail());
            response.put("role", updatedUser.getRole());
            response.put("level", updatedUser.getLevel());
            response.put("avatarUrl", updatedUser.getAvatarUrl());
            response.put("bio", updatedUser.getBio());

            // Masukkan Token Baru ke response
            response.put("token", newToken); // <--- INI KUNCINYA

            return ResponseEntity.ok(response);

        } catch (RuntimeException e) {
            return ResponseEntity.status(403).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("An error occurred while updating profile: " + e.getMessage());
        }
    }

    @GetMapping("/{id}/stats")
    public ResponseEntity<?> getUserStats(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getUserStats(id));
    }
}