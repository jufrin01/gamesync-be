package project.gamesync.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import java.time.LocalDateTime;

@Entity
@Table(name = "chat_messages")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // --- RELASI MANUAL (ID ONLY) ---

    // Mapping kolom 'sender_id' (Wajib ada pengirim)
    // Kita simpan ID User saja agar tidak berat load object User utuh
    @Column(name = "sender_id", nullable = false)
    private Long senderId;

    // Mapping kolom 'guild_id' (Boleh Null)
    // Jika NULL = Chat Global (Tavern)
    // Jika Terisi = Chat Khusus Guild tersebut
    @Column(name = "guild_id")
    private Long guildId;

    // --- KONTEN PESAN ---

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    // Mapping kolom 'message_type' (TEXT, IMAGE, SYSTEM, JOIN, LEAVE)
    // Default valuenya 'TEXT'
    @Column(name = "message_type", length = 20)
    @Builder.Default
    private String messageType = "TEXT";

    // Mapping kolom 'is_command' (True jika chat diawali tanda /)
    // Default false
    @Column(name = "is_command")
    @Builder.Default
    private Boolean isCommand = false;

    // --- TIMESTAMP ---

    // Otomatis diisi waktu saat pesan masuk database
    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
}