package project.gamesync.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.gamesync.entity.Quest;

import java.util.List;

@Repository
public interface QuestRepository extends JpaRepository<Quest, Long> {
    // Spring Data JPA akan otomatis menerjemahkan nama method ini menjadi Query SQL:
    // "SELECT * FROM quests ORDER BY date DESC"
    List<Quest> findAllByOrderByDateDesc();
}