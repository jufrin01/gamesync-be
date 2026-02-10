package project.gamesync.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.gamesync.entity.MatchHistory;
import java.util.List;

@Repository
public interface MatchHistoryRepository extends JpaRepository<MatchHistory, Long> {
    // Ambil history user diurutkan dari yang terbaru
    List<MatchHistory> findByUserIdOrderByMatchDateDesc(Long userId);

    // Hitung total match
    long countByUserId(Long userId);

    // Hitung total kemenangan (untuk Win Rate)
    long countByUserIdAndResult(Long userId, String result);
}