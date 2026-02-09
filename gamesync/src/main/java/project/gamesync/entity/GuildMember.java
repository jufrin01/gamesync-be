package project.gamesync.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import java.time.LocalDateTime;

@Entity
@Table(name = "guild_members")
@Data
@NoArgsConstructor
public class GuildMember {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Mapping kolom 'user_id'
    // Simpan ID User saja (Relasi Manual)
    @Column(name = "user_id", nullable = false)
    private Long userId;

    // Mapping kolom 'guild_id'
    // Simpan ID Guild saja (Relasi Manual)
    @Column(name = "guild_id", nullable = false)
    private Long guildId;

    // Mapping kolom 'role' (MEMBER, OFFICER, VETERAN, dll)
    // Default valuenya 'MEMBER'
    @Column(length = 20)
    private String role = "MEMBER";

    // Mapping kolom 'joined_at'
    // Otomatis terisi saat member baru ditambahkan
    @CreationTimestamp
    @Column(name = "joined_at", updatable = false)
    private LocalDateTime joinedAt;
}