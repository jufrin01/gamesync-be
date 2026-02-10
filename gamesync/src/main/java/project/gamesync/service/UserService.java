package project.gamesync.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.gamesync.dto.request.UpdateProfileRequest;
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

    // --- HELPER: Mengambil User atau Error ---
    public User getUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Error: User not found with ID: " + userId));
    }

    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    // --- FITUR 1: UPDATE PROFILE (Settings Page) ---
    @Transactional
    public User updateUserProfile(Long targetUserId, Long requesterId, UpdateProfileRequest request) {
        // 1. Validasi Keamanan: Pastikan yang edit adalah pemilik akun
        if (!targetUserId.equals(requesterId)) {
            throw new RuntimeException("Unauthorized: You can only update your own profile!");
        }

        // 2. Ambil User
        User user = getUserById(targetUserId);

        // 3. Update Field (Hanya jika data dikirim)
        if (request.getUsername() != null && !request.getUsername().isEmpty()) {
            user.setUsername(request.getUsername());
        }

        if (request.getBio() != null) {
            user.setBio(request.getBio());
        }

        if (request.getAvatarUrl() != null) {
            user.setAvatarUrl(request.getAvatarUrl());
        }

        // 4. Simpan ke Database
        return userRepository.save(user);
    }

    // --- FITUR 2: GAMIFICATION (XP & LEVEL) ---
    public UserStats getUserStats(Long userId) {
        // Cari stats user, jika belum ada buatkan baru (Lazy Init)
        return userStatsRepository.findById(userId)
                .orElseGet(() -> {
                    UserStats newStats = new UserStats();
                    newStats.setUserId(userId);
                    newStats.setTotalXp(0L);
                    return userStatsRepository.save(newStats);
                });
    }

    @Transactional
    public void addXp(Long userId, Long xpAmount) {
        UserStats stats = getUserStats(userId);

        // 1. Tambah XP
        long currentXp = stats.getTotalXp() != null ? stats.getTotalXp() : 0L;
        long newXp = currentXp + xpAmount;
        stats.setTotalXp(newXp);
        userStatsRepository.save(stats);

        // 2. Hitung Level Baru
        // Rumus: Level 1 (Base) + (TotalXP / 1000)
        // Contoh: 0 XP = Lv 1, 1050 XP = Lv 2, 5000 XP = Lv 6
        int newLevel = 1 + (int) (newXp / 1000);

        // 3. Update Level User jika naik
        User user = getUserById(userId);
        if (newLevel > user.getLevel()) {
            user.setLevel(newLevel);
            userRepository.save(user);

            // TODO: Nantinya bisa kirim notifikasi WebSocket "LEVEL UP!" di sini
            System.out.println("User " + user.getUsername() + " leveled up to " + newLevel + "!");
        }
    }
}