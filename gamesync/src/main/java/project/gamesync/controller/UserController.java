package project.gamesync.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import project.gamesync.dto.request.UpdateProfileRequest;
import project.gamesync.entity.User;

import project.gamesync.security.services.UserDetailsImpl;
import project.gamesync.service.UserService;

//@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    // --- 1. GET CURRENT PROFILE ---
    // Endpoint: GET /api/users/me
    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser() {
        try {
            // Ambil ID User dari Token JWT
            UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

            // Panggil Service untuk ambil data lengkap dari DB (termasuk bio, level, dll)
            User user = userService.getUserById(userDetails.getId());

            return ResponseEntity.ok(user);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error fetching profile: " + e.getMessage());
        }
    }

    // --- 2. UPDATE PROFILE ---
    // Endpoint: PUT /api/users/{id}
    @PutMapping("/{id}")
    public ResponseEntity<?> updateUser(@PathVariable Long id,
                                        @RequestBody UpdateProfileRequest request) {
        try {
            // Ambil ID User yang sedang login (Requester)
            UserDetailsImpl currentUser = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            Long requesterId = currentUser.getId();

            // Serahkan semua logika validasi & update ke Service
            User updatedUser = userService.updateUserProfile(id, requesterId, request);

            return ResponseEntity.ok(updatedUser);

        } catch (RuntimeException e) {
            // Tangkap error validasi dari Service (misal: Edit punya orang lain)
            return ResponseEntity.status(403).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("An error occurred while updating profile.");
        }
    }


    @GetMapping("/{id}/stats")
    public ResponseEntity<?> getUserStats(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getUserStats(id));
    }
}