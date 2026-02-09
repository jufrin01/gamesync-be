package project.gamesync.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import project.gamesync.dto.response.MessageResponse;
import project.gamesync.entity.Guild;
import project.gamesync.security.services.UserDetailsImpl;
import project.gamesync.service.GuildService;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/guilds")
public class GuildController {

    @Autowired
    GuildService guildService;

    // Helper untuk mengambil User ID dari Token JWT yang sedang login
    private Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        return userDetails.getId();
    }

    // Endpoint: GET /api/guilds (Lihat semua guild)
    @GetMapping
    public List<Guild> getAllGuilds() {
        return guildService.getAllGuilds();
    }

    // Endpoint: POST /api/guilds (Buat Guild)
    // Body: { "name": "NamaGuild", "description": "Deskripsi" }
    @PostMapping
    public ResponseEntity<?> createGuild(@RequestBody Guild guildRequest) {
        try {
            Long userId = getCurrentUserId();
            Guild newGuild = guildService.createGuild(guildRequest.getName(), guildRequest.getDescription(), userId);
            return ResponseEntity.ok(newGuild);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }

    // Endpoint: POST /api/guilds/{id}/join (Gabung Guild)
    @PostMapping("/{guildId}/join")
    public ResponseEntity<?> joinGuild(@PathVariable Long guildId) {
        try {
            Long userId = getCurrentUserId();
            guildService.joinGuild(guildId, userId);
            return ResponseEntity.ok(new MessageResponse("Successfully joined guild!"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }
}