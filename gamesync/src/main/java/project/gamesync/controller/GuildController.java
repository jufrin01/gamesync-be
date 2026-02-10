package project.gamesync.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import project.gamesync.dto.request.GuildRequest;
import project.gamesync.entity.Guild;
import project.gamesync.entity.User;
import project.gamesync.security.services.UserDetailsImpl;
import project.gamesync.service.GuildService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/guilds")
public class GuildController {

    @Autowired private GuildService guildService;

    // --- CREATE GUILD ---
    @PostMapping("/create")
    public ResponseEntity<?> createGuild(@RequestBody GuildRequest request) {
        UserDetailsImpl user = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        try {
            Guild guild = guildService.createGuild(user.getId(), request);
            return ResponseEntity.ok(guild);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // --- GET MY GUILD (Dashboard) ---
    @GetMapping("/my")
    public ResponseEntity<?> getMyGuild() {
        UserDetailsImpl user = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (user.getGuildId() == null) {
            return ResponseEntity.noContent().build(); // 204 No Content jika belum punya guild
        }

        Guild guild = guildService.getGuildById(user.getGuildId());
        List<User> members = guildService.getGuildMembers(user.getGuildId());

        Map<String, Object> response = new HashMap<>();
        response.put("info", guild);
        response.put("members", members);

        return ResponseEntity.ok(response);
    }

    // --- ADD MEMBER ---
    @PostMapping("/add")
    public ResponseEntity<?> addMember(@RequestBody GuildRequest request) {

        UserDetailsImpl user = (UserDetailsImpl) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();
        try {
            guildService.addMember(user.getId(), request.getTargetUserId());
            return ResponseEntity.ok("Member recruited successfully!");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // --- KICK MEMBER ---
    @PostMapping("/kick")
    public ResponseEntity<?> kickMember(@RequestBody GuildRequest request) {
        UserDetailsImpl user = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        try {
            guildService.kickMember(user.getId(), request.getTargetUserId());
            return ResponseEntity.ok("Member removed successfully!");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAllGuilds() {
        return ResponseEntity.ok(guildService.getAllGuilds());
    }

    // User Apply ke Guild
    @PostMapping("/apply/{guildId}")
    public ResponseEntity<?> applyToGuild(@PathVariable Long guildId) {
        UserDetailsImpl user = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        try {
            guildService.applyToGuild(user.getId(), guildId);
            return ResponseEntity.ok("Application sent to the Guild Leader.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Leader lihat siapa yang apply
    @GetMapping("/applications")
    public ResponseEntity<?> getApplications() {
        UserDetailsImpl user = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        try {
            return ResponseEntity.ok(guildService.getPendingApplications(user.getId()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Leader Approve/Reject
    @PostMapping("/application/{appId}/{action}") // action: approve or reject
    public ResponseEntity<?> handleApplication(@PathVariable Long appId,
                                               @PathVariable String action) {
        UserDetailsImpl user = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        try {
            boolean isApproved = action.equalsIgnoreCase("approve");
            guildService.handleApplication(user.getId(), appId, isApproved);
            return ResponseEntity.ok("Application " + (isApproved ? "Approved" : "Rejected"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}