package project.gamesync.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import java.time.LocalDateTime;

@Entity
@Table(name = "game_events")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GameEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Relasi ke Guild (Manual ID)
    @Column(name = "guild_id", nullable = false)
    private Long guildId;

    @Column(nullable = false, length = 100)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    // Tipe Event (misal: "RAID", "TOURNAMENT", "MEETING")
    @Column(name = "event_type", length = 50)
    private String eventType;

    // Waktu Mulai Event
    @Column(name = "start_time", nullable = false)
    private LocalDateTime startTime;

    // ID User pembuat event (Manual ID)
    @Column(name = "created_by", nullable = false)
    private Long createdBy;

    // Waktu pembuatan data
    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
}