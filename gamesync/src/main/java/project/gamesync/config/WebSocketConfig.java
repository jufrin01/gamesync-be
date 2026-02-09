package project.gamesync.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker // Mengaktifkan fitur Message Broker untuk WebSocket
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    /**
     * Konfigurasi Endpoint: Pintu masuk koneksi dari Frontend
     */
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // 1. Mendaftarkan endpoint "/ws"
        // Frontend akan connect ke sini: "http://localhost:8080/ws"
        registry.addEndpoint("/ws")

                // 2. Mengizinkan CORS untuk WebSocket
                // Penting agar React (port 5173) bisa connect ke Spring (port 8080)
                .setAllowedOriginPatterns("*")

                // 3. Mengaktifkan SockJS sebagai fallback
                // Jika browser user jadul tidak support WebSocket murni, dia akan otomatis
                // downgrade ke HTTP Long Polling agar chat tetap jalan.
                .withSockJS();
    }

    /**
     * Konfigurasi Broker: Mengatur lalu lintas pesan (Routing)
     */
    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        // 1. Prefix untuk pesan yang dikirim DARI Client (React) KE Server (Spring)
        // Contoh: Jika React kirim ke "/app/chat.sendMessage", maka akan
        // ditangkap oleh @MessageMapping("/chat.sendMessage") di Controller.
        registry.setApplicationDestinationPrefixes("/app");

        // 2. Prefix untuk pesan yang dikirim DARI Server (Spring) KE Client (React)
        // Ini adalah jalur "Subscribe".
        // - /topic: Biasanya untuk pesan publik (Chat Room, Pengumuman)
        // - /queue: Biasanya untuk pesan privat (DM, Notifikasi Personal)
        registry.enableSimpleBroker("/topic", "/queue");

        // (Opsional) Prefix untuk user specific message (Private Chat)
        registry.setUserDestinationPrefix("/user");
    }
}
