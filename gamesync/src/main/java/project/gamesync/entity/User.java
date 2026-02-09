package project.gamesync.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
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

    @Column(nullable = false, unique = true, length = 50)
    private String username;

    @Column(nullable = false, unique = true, length = 100)
    private String email;

    // Mapping ke kolom 'password_hash' di database
    @Column(name = "password_hash", nullable = false)
    private String password;

    // Role menggunakan Enum (USER, ADMIN, GUILD_LEADER)
    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    @Builder.Default
    private Role role = Role.USER;

    // --- PROFILE RPG ---

    @Column(name = "avatar_url", columnDefinition = "TEXT")
    private String avatarUrl;

    @Builder.Default
    private Integer level = 1;

    // Status Online/Offline
    @Column(length = 20)
    @Builder.Default
    private String status = "OFFLINE";

    // --- RELASI GUILD (MANUAL) ---
    // Kita simpan ID Guild-nya saja (tanpa @ManyToOne berat)
    // Pastikan di database kamu sudah menambahkan kolom 'guild_id'
    @Column(name = "guild_id")
    private Long guildId;

    // --- TIMESTAMPS ---

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    // Constructor Khusus untuk Register (AuthService)
    public User(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.role = Role.USER;
        this.level = 1;
        this.status = "ONLINE";
    }
}