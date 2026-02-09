package project.gamesync.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import java.time.LocalDateTime;

@Entity
@Table(name = "guilds")
@Data
@NoArgsConstructor
public class Guild {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 100)
    private String name;

    // Mapping kolom 'game_name'
    @Column(name = "game_name", length = 100)
    private String gameName;

    // Mapping kolom 'description' (Text)
    @Column(columnDefinition = "TEXT")
    private String description;

    // Mapping kolom 'leader_id'
    // Kita simpan ID Leader saja (Relasi Manual) agar ringan
    @Column(name = "leader_id", nullable = false)
    private Long leaderId;

    // Mapping kolom 'emblem_url'
    @Column(name = "emblem_url", columnDefinition = "TEXT")
    private String emblemUrl;

    // Default Level = 1
    private Integer level = 1;

    // Mapping kolom 'created_at'
    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
}