package project.gamesync.dto.request;

import lombok.Data;

@Data
public class GuildRequest {
    // Untuk Create Guild
    private String name;
    private String description;
    private String gameName; // Fokus game (misal: "MMORPG")
    private Boolean isPrivate; // True = Invite Only

    // Untuk Add/Kick Member
    private Long targetUserId;
}
