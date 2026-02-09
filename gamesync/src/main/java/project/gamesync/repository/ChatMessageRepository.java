package project.gamesync.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.gamesync.entity.ChatMessage;
import java.util.List;

@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {

    // 1. Ambil Chat Global (Tavern) -> guildId IS NULL
    // Diurutkan dari yang terlama ke terbaru
    List<ChatMessage> findByGuildIdIsNullOrderByCreatedAtAsc();

    // 2. Ambil Chat Khusus Guild -> guildId = ?
    List<ChatMessage> findByGuildIdOrderByCreatedAtAsc(Long guildId);
}