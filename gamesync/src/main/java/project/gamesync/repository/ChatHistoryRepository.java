package project.gamesync.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.gamesync.entity.ChatHistory;
import java.util.List;

@Repository
public interface ChatHistoryRepository extends JpaRepository<ChatHistory, Long> {

    // Ambil history berdasarkan userId (String/SessionID)
    List<ChatHistory> findByUserIdOrderByCreatedAtAsc(String userId);
}