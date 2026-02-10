package project.gamesync.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import project.gamesync.entity.GuildApplication;
import java.util.List;

public interface GuildApplicationRepository extends JpaRepository<GuildApplication, Long> {
    // Cari lamaran pending user (agar tidak spam apply)
    boolean existsByUserIdAndStatus(Long userId, GuildApplication.RequestStatus status);

    // Ambil semua lamaran untuk guild tertentu (untuk dilihat Leader)
    List<GuildApplication> findByGuildIdAndStatus(Long guildId, GuildApplication.RequestStatus status);
}