package project.gamesync.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import project.gamesync.entity.ChatMessage;
import project.gamesync.repository.ChatMessageRepository;

import java.util.List;

@Service
public class ChatService {

    @Autowired
    ChatMessageRepository chatMessageRepository;

    /**
     * Menyimpan pesan baru ke database PostgreSQL.
     * Digunakan oleh REST API (/api/chat/send) dan WebSocket.
     */
    public ChatMessage saveMessage(ChatMessage message) {
        return chatMessageRepository.save(message);
    }

    /**
     * Mengambil semua pesan global (tanpa guildId).
     * Nama method disesuaikan agar sinkron dengan ChatController.getChatHistory().
     */
    public List<ChatMessage> getAllMessages() {
        // Mengambil pesan yang guildId-nya null dan diurutkan berdasarkan waktu pembuatan
        return chatMessageRepository.findByGuildIdIsNullOrderByCreatedAtAsc();
    }

    /**
     * Mengambil riwayat pesan berdasarkan ID Guild/Squad tertentu.
     * Digunakan untuk fitur chat khusus anggota guild.
     */
    public List<ChatMessage> getMessagesByGuild(Long guildId) {
        return chatMessageRepository.findByGuildIdOrderByCreatedAtAsc(guildId);
    }

    /**
     * Alias untuk getGlobalChatHistory jika masih dibutuhkan oleh controller lama.
     */
    public List<ChatMessage> getGlobalChatHistory() {
        return getAllMessages();
    }

    /**
     * Alias untuk getGuildChatHistory jika masih dibutuhkan oleh controller lama.
     */
    public List<ChatMessage> getGuildChatHistory(Long guildId) {
        return getMessagesByGuild(guildId);
    }
}