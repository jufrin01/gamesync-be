package project.gamesync.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // --- CREDENTIALS ---
    @Column(nullable = false, unique = true, length = 50)
    private String username;

    @Column(nullable = false, unique = true, length = 100)
    private String email;

    @Column(name = "password_hash", nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    @Builder.Default
    private Role role = Role.USER;

    // --- RPG PROFILE ---
    @Column(name = "avatar_url", columnDefinition = "TEXT")
    private String avatarUrl;

    @Column(columnDefinition = "TEXT")
    private String bio; // Legendary Feats

    @Column(name = "game_identifier")
    private String gameId; // Misal: Steam ID / Riot ID

    // --- STATS & PROGRESSION ---
    @Builder.Default
    private Integer level = 1;

    @Column(name = "current_xp")
    @Builder.Default
    private Long currentXp = 0L; // Untuk progress bar XP (misal: 150/1000)

    // --- SYSTEM CONFIG ---
    @Column(length = 20)
    @Builder.Default
    private String status = "OFFLINE"; // ONLINE, OFFLINE, IN_GAME

    @Column(name = "notifications_enabled")
    @Builder.Default
    private Boolean isNotificationsEnabled = true;

    // --- GUILD RELATIONS ---
    @Column(name = "guild_id")
    private Long guildId;

    // --- TIMESTAMPS ---
    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "last_login")
    private LocalDateTime lastLogin;

    // Constructor custom untuk Register
    public User(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.role = Role.USER;
        this.level = 1;
        this.currentXp = 0L;
        this.isNotificationsEnabled = true;
        this.status = "ONLINE";
    }
}