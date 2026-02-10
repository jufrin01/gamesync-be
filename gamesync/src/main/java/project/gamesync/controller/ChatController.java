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
import project.gamesync.dto.response.ChatMessageResponse;
import project.gamesync.service.ChatService;

import java.util.List;

@Controller
@RequestMapping("/api/chat")
public class ChatController {

    @Autowired
    private ChatService chatService;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    // --- 1. REST API: AMBIL RIWAYAT PESAN (Updated) ---
    @GetMapping("/history")
    @ResponseBody
    public ResponseEntity<List<ChatMessageResponse>> getChatHistory(@RequestParam(required = false) Long guildId) {
        if (guildId != null) {
            return ResponseEntity.ok(chatService.getMessagesByGuild(guildId));
        }
        return ResponseEntity.ok(chatService.getAllMessages());
    }

    // --- 2. REST API: KIRIM PESAN (Updated) ---
    @PostMapping("/send")
    @ResponseBody
    public ResponseEntity<?> sendMessageRest(@RequestBody ChatMessage chatMessage) {
        // Simpan ke database (Service sudah mengembalikan DTO lengkap)
        ChatMessageResponse savedMessage = chatService.saveMessage(chatMessage);

        // Broadcast ke semua user via WebSocket (/topic/public)
        // Frontend yang subscribe ke socket ini akan langsung dapat data lengkap (nama, avatar)
        messagingTemplate.convertAndSend("/topic/public", savedMessage);

        return ResponseEntity.ok(savedMessage);
    }

    // --- 3. WEBSOCKET: HANDLERS (Updated) ---

    // Broadcast pesan yang dikirim langsung lewat socket
    @MessageMapping("/chat.sendMessage")
    @SendTo("/topic/public")
    public ChatMessageResponse sendMessage(@Payload ChatMessage chatMessage) {
        // Service sekarang mengembalikan ChatMessageResponse
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