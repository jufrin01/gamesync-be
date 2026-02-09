package project.gamesync.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import java.time.LocalDateTime;

@Entity
@Table(name = "game_schedules")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GameSchedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Nama Game (misal: "Valorant", "Mobile Legends")
    @Column(name = "game_name", nullable = false, length = 100)
    private String gameName;

    // Nama Squad / Tim yang akan main (Opsional)
    @Column(name = "squad_name", length = 100)
    private String squadName;

    // Waktu Main (Jadwal)
    @Column(name = "play_time", nullable = false)
    private LocalDateTime playTime;

    // Status: "PLANNED", "ONGOING", "COMPLETED", "CANCELLED"
    // Default: "PLANNED"
    @Column(length = 20)
    @Builder.Default
    private String status = "PLANNED";

    // Username pembuat jadwal (Creator)
    @Column(name = "created_by", length = 50)
    private String createdBy;

    // Waktu pembuatan jadwal
    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
}