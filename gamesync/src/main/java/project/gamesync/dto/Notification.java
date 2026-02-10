package project.gamesync.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Notification {
    private String title;
    private String message;
    private String type; // Contoh: 'QUEST', 'SYSTEM', 'GUILD'
    private String timestamp;
}