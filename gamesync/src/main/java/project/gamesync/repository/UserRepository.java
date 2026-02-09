package project.gamesync.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.gamesync.entity.User;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // Mencari user berdasarkan username (untuk login)
    Optional<User> findByUsername(String username);

    // Mengecek apakah username sudah terpakai (untuk register)
    Boolean existsByUsername(String username);

    // Mengecek apakah email sudah terpakai
    Boolean existsByEmail(String email);
}