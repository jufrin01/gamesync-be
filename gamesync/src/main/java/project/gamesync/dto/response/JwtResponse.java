package project.gamesync.dto.response;

import lombok.Data;

@Data
public class JwtResponse {
    private String token;
    private String type = "Bearer";
    private Long id;
    private String username;
    private String email;
    private String role;
    private Long guildId;
    private Integer level; // Field Level

    // --- PERHATIKAN PARAMETER DI DALAM KURUNG ---
    public JwtResponse(String accessToken, Long id, String username, String email, String role, Long guildId, Integer level) {
        this.token = accessToken;
        this.id = id;
        this.username = username;
        this.email = email;
        this.role = role;
        this.guildId = guildId;
        this.level = level; // <--- Mengisi nilai dari parameter
    }
}