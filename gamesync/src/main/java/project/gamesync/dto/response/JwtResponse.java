package project.gamesync.dto.response;

import lombok.Data;

@Data
public class JwtResponse {
    private String token;
    private String type = "Bearer";
    private Long id;
    private String username;
    private String email;
    private String role;    // String role tunggal (USER/ADMIN)
    private Long guildId;   // Info guild user (null jika belum join)

    // Constructor lengkap
    public JwtResponse(String accessToken, Long id, String username, String email, String role, Long guildId) {
        this.token = accessToken;
        this.id = id;
        this.username = username;
        this.email = email;
        this.role = role;
        this.guildId = guildId;
    }
}