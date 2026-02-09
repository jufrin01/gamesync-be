package project.gamesync.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.gamesync.entity.GameEvent;
import java.util.List;

@Repository
public interface GameEventRepository extends JpaRepository<GameEvent, Long> {

    // Ambil semua event milik Guild tertentu, urutkan berdasarkan waktu mulai
    List<GameEvent> findByGuildIdOrderByStartTimeAsc(Long guildId);
}