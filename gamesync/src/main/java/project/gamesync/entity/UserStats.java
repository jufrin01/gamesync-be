package project.gamesync.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UpdateTimestamp;
import java.time.LocalDateTime;

@Entity
@Table(name = "user_stats")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserStats {

    // Primary Key adalah user_id (Bukan Auto Increment)
    // ID ini harus di-set manual sama dengan ID User saat pembuatan data
    @Id
    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "total_xp")
    private Long totalXp = 0L; // Default 0

    @Column(name = "quests_completed")
    private Integer questsCompleted = 0; // Default 0

    @Column(name = "raids_attended")
    private Integer raidsAttended = 0; // Default 0

    // @UpdateTimestamp otomatis update waktu setiap kali ada perubahan data (save/update)
    @UpdateTimestamp
    @Column(name = "last_updated")
    private LocalDateTime lastUpdated;
}