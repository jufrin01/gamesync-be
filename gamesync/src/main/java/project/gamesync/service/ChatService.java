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

    // Simpan Pesan ke Database
    public ChatMessage saveMessage(ChatMessage message) {
        return chatMessageRepository.save(message);
    }

    // Ambil History Chat Global
    public List<ChatMessage> getGlobalChatHistory() {
        return chatMessageRepository.findByGuildIdIsNullOrderByCreatedAtAsc();
    }

    // Ambil History Chat Guild Tertentu
    public List<ChatMessage> getGuildChatHistory(Long guildId) {
        return chatMessageRepository.findByGuildIdOrderByCreatedAtAsc(guildId);
    }
}