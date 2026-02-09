package project.gamesync.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.gamesync.entity.Guild;
import java.util.Optional;

@Repository
public interface GuildRepository extends JpaRepository<Guild, Long> {

    // Mencari Guild berdasarkan nama (unik)
    Optional<Guild> findByName(String name);

    // Mengecek ketersediaan nama guild
    boolean existsByName(String name);
}