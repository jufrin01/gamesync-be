package project.gamesync.dto.response; // Sesuaikan package

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ChatMessageResponse {
    private Long id;
    private Long senderId;
    private Long guildId;
    private String content;
    private String messageType;
    private Boolean isCommand;
    private LocalDateTime createdAt;

    // --- TAMBAHAN WAJIB AGAR FRONTEND BISA BACA NAMA ---
    private String senderName;
    private String senderRole;
    private String avatarUrl;
}