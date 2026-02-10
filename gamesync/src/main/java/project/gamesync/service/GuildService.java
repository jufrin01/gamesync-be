package project.gamesync.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.gamesync.dto.Notification;
import project.gamesync.dto.request.GuildRequest;
import project.gamesync.entity.Guild;
import project.gamesync.entity.GuildApplication;
import project.gamesync.entity.Role;
import project.gamesync.entity.User;
import project.gamesync.repository.GuildApplicationRepository;
import project.gamesync.repository.GuildRepository;
import project.gamesync.repository.UserRepository;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class GuildService {

    @Autowired
    private GuildRepository guildRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    private GuildApplicationRepository applicationRepository;

    // --- 1. CREATE GUILD ---
    @Transactional
    public Guild createGuild(Long userId, GuildRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // --- LOGIKA LEVEL ---
        // Jika ADMIN, kita anggap levelnya sudah memenuhi syarat (Bypass Check)
        // Jika USER biasa, wajib Level 15
        if (user.getRole() != Role.ADMIN && user.getLevel() < 15) {
            throw new RuntimeException("Level too low! You need Level 15 to establish a guild.");
        }

        // VALIDASI: User belum punya guild
        if (user.getGuildId() != null) {
            throw new RuntimeException("You are already in a guild! Leave it first.");
        }

        // VALIDASI: Nama Guild Unik
        if (guildRepository.existsByName(request.getName())) {
            throw new RuntimeException("Guild name '" + request.getName() + "' is already taken!");
        }

        // Buat Guild Baru
        Guild guild = new Guild();
        guild.setName(request.getName());
        guild.setDescription(request.getDescription());
        guild.setGameName(request.getGameName());
        guild.setIsPrivate(request.getIsPrivate() != null ? request.getIsPrivate() : false);
        guild.setLeaderId(userId);
        guild.setMaxMembers(20);

        // Simpan Guild
        guild = guildRepository.save(guild);

        // Update User
        user.setGuildId(guild.getId());

        // Jika User biasa, naik pangkat jadi GUILD_LEADER
        // Jika Admin, JANGAN ubah role (biarkan tetap ADMIN karena itu role tertinggi)
        if (user.getRole() != Role.ADMIN) {
            user.setRole(Role.GUILD_LEADER);
        }

        userRepository.save(user);

        return guild;
    }

    // --- 2. RECRUIT MEMBER (ADD) ---
    @Transactional
    public void addMember(Long leaderId, Long targetUserId) {
        User leader = userRepository.findById(leaderId).orElseThrow();

        // Cek Guild Leader
        if (leader.getGuildId() == null) throw new RuntimeException("You represent no guild.");

        Guild guild = guildRepository.findById(leader.getGuildId())
                .orElseThrow(() -> new RuntimeException("Guild not found"));

        // Validasi Authority
        if (!guild.getLeaderId().equals(leaderId)) {
            throw new RuntimeException("Only the Guild Leader can recruit members!");
        }

        // Validasi Kapasitas
        long currentMemberCount = userRepository.countByGuildId(guild.getId());
        if (currentMemberCount >= guild.getMaxMembers()) {
            throw new RuntimeException("Guild is full! Max capacity is " + guild.getMaxMembers());
        }

        // Validasi Target User
        User targetUser = userRepository.findById(targetUserId)
                .orElseThrow(() -> new RuntimeException("Adventurer not found with ID: " + targetUserId));

        if (targetUser.getGuildId() != null) {
            throw new RuntimeException("This adventurer is already sworn to another guild.");
        }

        // Masukkan ke Guild
        targetUser.setGuildId(guild.getId());
        userRepository.save(targetUser);

        // Kirim Notifikasi ke Target
        sendNotification("Guild Recruitment",
                "You have been recruited to " + guild.getName() + " by Leader " + leader.getUsername(),
                "GUILD"
        );
    }

    // --- 3. KICK MEMBER ---
    @Transactional
    public void kickMember(Long leaderId, Long targetUserId) {
        User leader = userRepository.findById(leaderId).orElseThrow();
        User targetUser = userRepository.findById(targetUserId)
                .orElseThrow(() -> new RuntimeException("Member not found"));

        if (!leader.getGuildId().equals(targetUser.getGuildId())) {
            throw new RuntimeException("Target is not in your guild!");
        }

        if (leaderId.equals(targetUserId)) {
            throw new RuntimeException("You cannot kick yourself! Disband the guild instead.");
        }

        // Hapus dari Guild
        targetUser.setGuildId(null);
        userRepository.save(targetUser);

        // Kirim Notifikasi
        sendNotification("Guild Exile",
                "You have been removed from the guild by Leader " + leader.getUsername(),
                "SYSTEM"
        );
    }

    public List<Guild> getAllGuilds() {
        return guildRepository.findAll();
    }

    @Transactional
    public void applyToGuild(Long userId, Long guildId) {
        User user = userRepository.findById(userId).orElseThrow();

        // Validasi
        if (user.getGuildId() != null) throw new RuntimeException("You are already in a guild!");
        if (applicationRepository.existsByUserIdAndStatus(userId, GuildApplication.RequestStatus.PENDING)) {
            throw new RuntimeException("You already have a pending application!");
        }

        GuildApplication app = new GuildApplication();
        app.setUserId(userId);
        app.setGuildId(guildId);
        applicationRepository.save(app);

        // Notifikasi ke Leader (Opsional, query leaderId dari guildId)
        Guild guild = guildRepository.findById(guildId).orElseThrow();
        User leader = userRepository.findById(guild.getLeaderId()).orElseThrow();
        sendNotification(leader.getUsername(), user.getUsername() + " applied to your guild!", "SYSTEM");
    }

    // --- FITUR BARU: GET APPLICANTS (Leader View) ---
    public List<Map<String, Object>> getPendingApplications(Long leaderId) {
        User leader = userRepository.findById(leaderId).orElseThrow();
        if (leader.getGuildId() == null) throw new RuntimeException("No guild found.");

        // Validasi Leader
        Guild guild = guildRepository.findById(leader.getGuildId()).orElseThrow();
        if (!guild.getLeaderId().equals(leaderId)) throw new RuntimeException("Unauthorized.");

        List<GuildApplication> apps = applicationRepository.findByGuildIdAndStatus(guild.getId(), GuildApplication.RequestStatus.PENDING);

        // Convert ke format enak dibaca (User Info)
        return apps.stream().map(app -> {
            User applicant = userRepository.findById(app.getUserId()).orElse(null);
            Map<String, Object> map = new HashMap<>();
            map.put("applicationId", app.getId());
            map.put("userId", applicant.getId());
            map.put("username", applicant.getUsername());
            map.put("level", applicant.getLevel());
            map.put("avatarUrl", applicant.getAvatarUrl());
            return map;
        }).collect(Collectors.toList());
    }

    // --- FITUR BARU: APPROVE/REJECT ---
    @Transactional
    public void handleApplication(Long leaderId, Long applicationId, boolean isApproved) {
        GuildApplication app = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new RuntimeException("Application not found"));

        // Validasi Leader (keamanan)
        User leader = userRepository.findById(leaderId).orElseThrow();
        if (!leader.getGuildId().equals(app.getGuildId())) throw new RuntimeException("Unauthorized.");
        Guild guild = guildRepository.findById(leader.getGuildId()).orElseThrow();
        if (!guild.getLeaderId().equals(leaderId)) throw new RuntimeException("Only leader can approve.");

        User applicant = userRepository.findById(app.getUserId()).orElseThrow();

        if (isApproved) {
            // Cek Kapasitas
            long count = userRepository.countByGuildId(guild.getId());
            if (count >= guild.getMaxMembers()) throw new RuntimeException("Guild is full!");

            // Masukkan User
            applicant.setGuildId(guild.getId());
            userRepository.save(applicant);

            app.setStatus(GuildApplication.RequestStatus.APPROVED);
            sendNotification(applicant.getUsername(), "Welcome! Your application to " + guild.getName() + " was accepted.", "GUILD");
        } else {
            app.setStatus(GuildApplication.RequestStatus.REJECTED);
            sendNotification(applicant.getUsername(), "Your application to " + guild.getName() + " was declined.", "SYSTEM");
        }

        applicationRepository.save(app);
    }


    // --- HELPER ---
    public Guild getGuildById(Long id) {
        return guildRepository.findById(id).orElse(null);
    }

    public List<User> getGuildMembers(Long guildId) {
        return userRepository.findAllByGuildId(guildId);
    }

    private void sendNotification(String title, String message, String type) {
        String timeNow = LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm"));
        Notification notif = new Notification(title, message, type, timeNow);
        messagingTemplate.convertAndSend("/topic/notifications", notif);
    }
}