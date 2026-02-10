package project.gamesync.dto.response;

import lombok.Builder;
import lombok.Data;
import java.util.List;
import project.gamesync.entity.MatchHistory;

@Data
@Builder
public class ChronicleStatsResponse {
    private Long totalMatches;
    private Double winRate;
    private String mostPlayedGame;
    private Double averageKDA;
    private List<MatchHistory> recentMatches;
}