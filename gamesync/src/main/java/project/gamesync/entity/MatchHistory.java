package project.gamesync.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import java.time.LocalDateTime;

@Entity
@Table(name = "match_histories")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MatchHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    // Nama Game (Mobile Legends, Valorant, PUBG, dll)
    @Column(name = "game_name", nullable = false)
    private String gameName;

    // Karakter/Hero/Agent yang dipakai
    @Column(name = "character_used")
    private String characterUsed;

    // Hasil: VICTORY, DEFEAT, DRAW
    @Column(nullable = false)
    private String result;

    // Statistik KDA
    private Integer kills = 0;
    private Integer deaths = 0;
    private Integer assists = 0;

    // Durasi match dalam menit
    private Integer durationMinutes;

    // Tanggal Match
    @CreationTimestamp
    private LocalDateTime matchDate;
}