package project.gamesync.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.gamesync.entity.Guild;
import project.gamesync.entity.GuildMember;
import project.gamesync.entity.Role;
import project.gamesync.entity.User;
import project.gamesync.repository.GuildMemberRepository;
import project.gamesync.repository.GuildRepository;
import project.gamesync.repository.UserRepository;

import java.util.List;
import java.util.Optional;

@Service
public class GuildService {

    @Autowired
    GuildRepository guildRepository;

    @Autowired
    GuildMemberRepository guildMemberRepository;

    @Autowired
    UserRepository userRepository;

    // --- CREATE GUILD ---
    @Transactional
    public Guild createGuild(String guildName, String description, Long userId) throws Exception {
        // 1. Cek apakah nama guild sudah ada
        if (guildRepository.existsByName(guildName)) {
            throw new Exception("Guild name already exists!");
        }

        // 2. Cek apakah user sudah punya guild
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new Exception("User not found"));

        if (user.getGuildId() != null) {
            throw new Exception("You are already in a guild!");
        }

        // 3. Buat Guild Baru
        Guild guild = new Guild();
        guild.setName(guildName);
        guild.setDescription(description);
        guild.setLeaderId(userId);
        guild.setLevel(1);

        Guild savedGuild = guildRepository.save(guild);

        // 4. Update User (Set Guild ID & Role jadi GUILD_LEADER)
        user.setGuildId(savedGuild.getId());
        user.setRole(Role.GUILD_LEADER);
        userRepository.save(user);

        // 5. Masukkan User ke tabel GuildMember
        GuildMember member = new GuildMember();
        member.setUserId(userId);
        member.setGuildId(savedGuild.getId());
        member.setRole("LEADER");
        guildMemberRepository.save(member);

        return savedGuild;
    }

    // --- JOIN GUILD ---
    @Transactional
    public void joinGuild(Long guildId, Long userId) throws Exception {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new Exception("User not found"));

        if (user.getGuildId() != null) {
            throw new Exception("User already has a guild!");
        }

        if (!guildRepository.existsById(guildId)) {
            throw new Exception("Guild not found!");
        }

        // 1. Update User
        user.setGuildId(guildId);
        userRepository.save(user);

        // 2. Add to GuildMember
        GuildMember member = new GuildMember();
        member.setUserId(userId);
        member.setGuildId(guildId);
        member.setRole("MEMBER");
        guildMemberRepository.save(member);
    }

    // --- GET ALL GUILDS ---
    public List<Guild> getAllGuilds() {
        return guildRepository.findAll();
    }
}