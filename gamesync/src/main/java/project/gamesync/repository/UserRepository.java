package project.gamesync.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.gamesync.entity.User;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // --- AUTHENTICATION ---
    Optional<User> findByUsername(String username);
    Boolean existsByUsername(String username);
    Boolean existsByEmail(String email);


    // 1. Menghitung jumlah member dalam satu guild (untuk cek kapasitas max 20)
    long countByGuildId(Long guildId);

    // 2. Mengambil daftar semua member dalam satu guild (untuk ditampilkan di dashboard)
    List<User> findAllByGuildId(Long guildId);
}