package project.gamesync.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import project.gamesync.dto.request.MatchRequest;
import project.gamesync.security.services.UserDetailsImpl;
import project.gamesync.service.ChronicleService;

//@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/chronicles")
public class ChronicleController {

    @Autowired
    private ChronicleService chronicleService;

    // GET Analytics (Untuk Dashboard)
    @GetMapping("/analytics")
    public ResponseEntity<?> getMyAnalytics() {
        UserDetailsImpl user = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return ResponseEntity.ok(chronicleService.getUserAnalytics(user.getId()));
    }

    // POST Add Match (Untuk Input Manual)
    @PostMapping("/record")
    public ResponseEntity<?> recordMatch(@RequestBody MatchRequest request) {
        UserDetailsImpl user = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        try {
            return ResponseEntity.ok(chronicleService.addMatchRecord(user.getId(), request));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Failed to record match: " + e.getMessage());
        }
    }
}