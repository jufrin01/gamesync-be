package project.gamesync.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import project.gamesync.entity.User;
import project.gamesync.entity.UserStats;
import project.gamesync.repository.UserRepository;
import project.gamesync.repository.UserStatsRepository;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserStatsRepository userStatsRepository;

    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    // Ambil Statistik User (XP, Level, Quests)
    public UserStats getUserStats(Long userId) {
        return userStatsRepository.findById(userId)
                .orElseGet(() -> {
                    // Fallback jika data stats tidak ditemukan (Harusnya tidak terjadi jika register benar)
                    UserStats newStats = new UserStats();
                    newStats.setUserId(userId);
                    return userStatsRepository.save(newStats);
                });
    }

    // Update Level User (Contoh logika sederhana)
    public void addXp(Long userId, Long xpAmount) {
        UserStats stats = getUserStats(userId);
        stats.setTotalXp(stats.getTotalXp() + xpAmount);

        // Logika naik level (Misal: tiap 1000 XP naik 1 level)
        /* if (stats.getTotalXp() >= 1000) {
            // Update Level di tabel User
            User user = userRepository.findById(userId).orElse(null);
            if (user != null) {
                user.setLevel(user.getLevel() + 1);
                userRepository.save(user);
            }
        }
        */

        userStatsRepository.save(stats);
    }
}