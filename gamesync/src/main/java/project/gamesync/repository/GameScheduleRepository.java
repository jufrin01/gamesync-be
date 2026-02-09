package project.gamesync.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.gamesync.entity.GameSchedule;
import java.util.List;

@Repository
public interface GameScheduleRepository extends JpaRepository<GameSchedule, Long> {

    // Ambil semua jadwal yang statusnya BUKAN "COMPLETED" (alias masih aktif)
    List<GameSchedule> findByStatusNot(String status);

    // Atau ambil berdasarkan Game tertentu
    List<GameSchedule> findByGameName(String gameName);
}