package project.gamesync.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import project.gamesync.entity.ChatMessage;
import project.gamesync.service.ChatService;

import java.util.List;

@Controller // Pakai @Controller karena ada WebSocket
public class ChatController {

    @Autowired
    ChatService chatService;

    // --- WEBSOCKET HANDLERS ---

    // 1. Kirim Pesan: /app/chat.sendMessage -> Disebar ke /topic/public
    @MessageMapping("/chat.sendMessage")
    @SendTo("/topic/public")
    public ChatMessage sendMessage(@Payload ChatMessage chatMessage) {
        // Simpan ke database sebelum disebar ke user lain
        return chatService.saveMessage(chatMessage);
    }

    // 2. User Join: /app/chat.addUser
    @MessageMapping("/chat.addUser")
    @SendTo("/topic/public")
    public ChatMessage addUser(@Payload ChatMessage chatMessage,
                               SimpMessageHeaderAccessor headerAccessor) {
        // Simpan username di session WebSocket agar bisa dipakai saat disconnect
        headerAccessor.getSessionAttributes().put("username", chatMessage.getSenderId());
        return chatMessage;
    }

    // --- REST API HANDLERS (Untuk History) ---

    // Endpoint: GET /api/chat/history
    @GetMapping("/api/chat/history")
    @ResponseBody // Wajib pakai ini karena class-nya @Controller
    public List<ChatMessage> getChatHistory(@RequestParam(required = false) Long guildId) {
        if (guildId != null) {
            return chatService.getGuildChatHistory(guildId);
        }
        return chatService.getGlobalChatHistory();
    }
}