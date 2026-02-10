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
@Table(name = "guilds")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Guild {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // --- IDENTITY ---
    @Column(nullable = false, unique = true, length = 100)
    private String name;

    @Column(name = "game_name", length = 100)
    private String gameName; // Fokus game guild ini (misal: MMORPG, FPS)

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "leader_id", nullable = false)
    private Long leaderId;

    // --- VISUALS ---
    @Column(name = "emblem_url", columnDefinition = "TEXT")
    private String emblemUrl; // Logo Kecil

    @Column(name = "banner_url", columnDefinition = "TEXT")
    private String bannerUrl; // Background Besar

    // --- SETTINGS & LIMITS ---
    @Builder.Default
    @Column(name = "max_members")
    private Integer maxMembers = 20; // Default limit 20 member

    @Builder.Default
    @Column(name = "is_private")
    private Boolean isPrivate = false; // False = Public, True = Invite Only

    @Builder.Default
    @Column(name = "min_level_req")
    private Integer minLevelToJoin = 1; // Syarat level join

    // --- PROGRESSION ---
    @Builder.Default
    private Integer level = 1;

    @Builder.Default
    @Column(name = "total_xp")
    private Long totalXp = 0L; // Akumulasi XP Guild

    // --- TIMESTAMPS ---
    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}