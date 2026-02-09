package project.gamesync.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import project.gamesync.entity.ChatMessage;
import project.gamesync.service.ChatService;

import java.util.List;

@Controller
@RequestMapping("/api/chat")
public class ChatController {

    @Autowired
    private ChatService chatService;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    // --- 1. REST API: AMBIL RIWAYAT PESAN ---
    @GetMapping("/history")
    @ResponseBody
    public List<ChatMessage> getChatHistory(@RequestParam(required = false) Long guildId) {
        if (guildId != null) {
            return chatService.getMessagesByGuild(guildId);
        }
        return chatService.getAllMessages(); // Ambil semua chat umum
    }

    // --- 2. REST API: KIRIM PESAN ---
    @PostMapping("/send")
    @ResponseBody
    public ResponseEntity<?> sendMessageRest(@RequestBody ChatMessage chatMessage) {
        // Simpan ke database
        ChatMessage savedMessage = chatService.saveMessage(chatMessage);

        // Broadcast ke semua user via WebSocket (/topic/public)
        messagingTemplate.convertAndSend("/topic/public", savedMessage);

        return ResponseEntity.ok(savedMessage);
    }

    // --- 3. WEBSOCKET: HANDLERS ---

    // Broadcast pesan yang dikirim langsung lewat socket
    @MessageMapping("/chat.sendMessage")
    @SendTo("/topic/public")
    public ChatMessage sendMessage(@Payload ChatMessage chatMessage) {
        return chatService.saveMessage(chatMessage);
    }

    // Notifikasi user bergabung
    @MessageMapping("/chat.addUser")
    @SendTo("/topic/public")
    public ChatMessage addUser(@Payload ChatMessage chatMessage,
                               SimpMessageHeaderAccessor headerAccessor) {
        headerAccessor.getSessionAttributes().put("username", chatMessage.getSenderId());
        return chatMessage;
    }
}