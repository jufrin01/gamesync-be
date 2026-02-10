package project.gamesync.dto.request;

import lombok.Data;

@Data
public class MatchRequest {
    private String gameName;
    private String characterUsed;
    private String result; // "VICTORY" or "DEFEAT"
    private Integer kills;
    private Integer deaths;
    private Integer assists;
    private Integer durationMinutes;
}