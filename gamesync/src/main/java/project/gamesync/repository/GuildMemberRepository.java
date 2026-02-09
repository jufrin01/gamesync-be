package project.gamesync.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.gamesync.entity.GuildMember;

import java.util.List;
import java.util.Optional;

@Repository
public interface GuildMemberRepository extends JpaRepository<GuildMember, Long> {

    // Ambil semua member dalam 1 Guild
    List<GuildMember> findByGuildId(Long guildId);

    // Cek apakah User X sudah join Guild (User cuma boleh 1 Guild)
    Optional<GuildMember> findByUserId(Long userId);

    // Cek status member spesifik di guild tertentu
    Optional<GuildMember> findByUserIdAndGuildId(Long userId, Long guildId);

    // Hapus member (Kick / Leave)
    void deleteByUserIdAndGuildId(Long userId, Long guildId);
}