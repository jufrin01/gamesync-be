package project.gamesync.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import project.gamesync.entity.User;
import project.gamesync.entity.UserStats;
import project.gamesync.security.services.UserDetailsImpl;
import project.gamesync.service.UserService;

import java.util.Optional;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    UserService userService;

    // Helper ambil ID User Login
    private Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        return userDetails.getId();
    }

    // Endpoint: GET /api/users/me (Profil Saya)
    @GetMapping("/me")
    public ResponseEntity<?> getMyProfile() {
        Long userId = getCurrentUserId();
        Optional<User> user = userService.findById(userId);
        return user.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Endpoint: GET /api/users/me/stats (Stats RPG Saya)
    @GetMapping("/me/stats")
    public ResponseEntity<UserStats> getMyStats() {
        Long userId = getCurrentUserId();
        UserStats stats = userService.getUserStats(userId);
        return ResponseEntity.ok(stats);
    }

    // Endpoint: POST /api/users/me/xp (Nambah XP - Buat test naik level)
    @PostMapping("/me/xp")
    public ResponseEntity<?> addXp(@RequestParam Long amount) {
        Long userId = getCurrentUserId();
        userService.addXp(userId, amount);
        return ResponseEntity.ok("XP Added!");
    }
}