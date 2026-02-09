package project.gamesync.dto.request;

import lombok.Data;

@Data
public class ChatRequest {
    // Isi pesan
    private String content;

    // Pengirim (Username)
    private String sender;

    // Tipe pesan: "CHAT", "JOIN", "LEAVE"
    private String type;

    // Jika null = Chat Global (Tavern)
    // Jika ada ID = Chat Guild
    private Long guildId;
}