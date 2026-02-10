package project.gamesync.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import project.gamesync.entity.ChatMessage;
import project.gamesync.entity.User;
import project.gamesync.dto.response.ChatMessageResponse; // Import DTO baru
import project.gamesync.repository.ChatMessageRepository;
import project.gamesync.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ChatService {

    @Autowired
    private ChatMessageRepository chatMessageRepository;

    @Autowired
    private UserRepository userRepository; // Inject User Repo untuk cari nama

    // --- SIMPAN PESAN ---
    public ChatMessageResponse saveMessage(ChatMessage message) {
        ChatMessage saved = chatMessageRepository.save(message);
        return mapToResponse(saved); // Kembalikan DTO lengkap
    }

    // --- AMBIL SEMUA (GLOBAL) ---
    public List<ChatMessageResponse> getAllMessages() {
        List<ChatMessage> msgs = chatMessageRepository.findByGuildIdIsNullOrderByCreatedAtAsc();
        return msgs.stream().map(this::mapToResponse).collect(Collectors.toList());
    }

    // --- AMBIL PER GUILD ---
    public List<ChatMessageResponse> getMessagesByGuild(Long guildId) {
        List<ChatMessage> msgs = chatMessageRepository.findByGuildIdOrderByCreatedAtAsc(guildId);
        return msgs.stream().map(this::mapToResponse).collect(Collectors.toList());
    }

    // --- LOGIKA UTAMA: MAPPING ENTITY KE DTO ---
    private ChatMessageResponse mapToResponse(ChatMessage msg) {
        ChatMessageResponse response = new ChatMessageResponse();

        // 1. Copy data asli dari Entity ChatMessage
        response.setId(msg.getId());
        response.setSenderId(msg.getSenderId());
        response.setGuildId(msg.getGuildId());
        response.setContent(msg.getContent());
        response.setMessageType(msg.getMessageType());
        response.setIsCommand(msg.getIsCommand());
        response.setCreatedAt(msg.getCreatedAt());

        // 2. Cari data User berdasarkan senderId
        User sender = userRepository.findById(msg.getSenderId()).orElse(null);

        if (sender != null) {
            response.setSenderName(sender.getUsername());    // Data Nama
            response.setSenderRole(sender.getRole().name()); // Data Role
            response.setAvatarUrl(sender.getAvatarUrl());    // Data Foto
        } else {
            // Fallback jika user terhapus
            response.setSenderName("Unknown Hero");
            response.setSenderRole("USER");
            response.setAvatarUrl(null);
        }

        return response;
    }
}