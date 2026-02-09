package project.gamesync.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.gamesync.entity.UserStats;

@Repository
public interface UserStatsRepository extends JpaRepository<UserStats, Long> {
    // Karena Primary Key-nya adalah user_id, kita cukup pakai method bawaan:
    // findById(Long userId) untuk mengambil stats.
}