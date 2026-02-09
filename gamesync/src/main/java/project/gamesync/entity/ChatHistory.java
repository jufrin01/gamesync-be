package project.gamesync.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import java.time.LocalDateTime;

@Entity
@Table(name = "chat_history")
@Data
@NoArgsConstructor
public class ChatHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Mapping kolom 'user_id' (Varchar 50)
    // Bisa berupa Username atau Session ID
    @Column(name = "user_id", nullable = false, length = 50)
    private String userId;

    // Mapping kolom 'message' (Input User)
    @Column(nullable = false, columnDefinition = "TEXT")
    private String message;

    // Mapping kolom 'reply' (Balasan AI / Bot)
    @Column(nullable = false, columnDefinition = "TEXT")
    private String reply;

    // Mapping kolom 'created_at'
    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
}