package project.gamesync.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import project.gamesync.entity.MatchHistory;
import project.gamesync.dto.request.MatchRequest;
import project.gamesync.dto.response.ChronicleStatsResponse;
import project.gamesync.repository.MatchHistoryRepository;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ChronicleService {

    @Autowired
    private MatchHistoryRepository matchRepository;

    // --- 1. GET USER ANALYTICS ---
    public ChronicleStatsResponse getUserAnalytics(Long userId) {
        List<MatchHistory> history = matchRepository.findByUserIdOrderByMatchDateDesc(userId);

        if (history.isEmpty()) {
            return ChronicleStatsResponse.builder()
                    .totalMatches(0L)
                    .winRate(0.0)
                    .averageKDA(0.0)
                    .mostPlayedGame("N/A")
                    .recentMatches(List.of())
                    .build();
        }

        // Hitung Statistik
        long totalMatches = history.size();
        long wins = history.stream().filter(m -> "VICTORY".equalsIgnoreCase(m.getResult())).count();
        double winRate = (double) wins / totalMatches * 100;

        // Hitung Avg KDA: (K + A) / D (jika D=0 dianggap 1 agar tidak error)
        double totalKDA = history.stream()
                .mapToDouble(m -> (double) (m.getKills() + m.getAssists()) / (m.getDeaths() == 0 ? 1 : m.getDeaths()))
                .average().orElse(0.0);

        // Cari Game Paling Sering Dimainkan
        String mostPlayed = history.stream()
                .collect(Collectors.groupingBy(MatchHistory::getGameName, Collectors.counting()))
                .entrySet().stream().max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey).orElse("N/A");

        return ChronicleStatsResponse.builder()
                .totalMatches(totalMatches)
                .winRate(Math.round(winRate * 10.0) / 10.0) // Round 1 desimal
                .averageKDA(Math.round(totalKDA * 100.0) / 100.0) // Round 2 desimal
                .mostPlayedGame(mostPlayed)
                .recentMatches(history.stream().limit(10).collect(Collectors.toList())) // Ambil 10 terakhir
                .build();
    }

    // --- 2. ADD MATCH RECORD ---
    public MatchHistory addMatchRecord(Long userId, MatchRequest request) {
        MatchHistory match = MatchHistory.builder()
                .userId(userId)
                .gameName(request.getGameName())
                .characterUsed(request.getCharacterUsed())
                .result(request.getResult().toUpperCase())
                .kills(request.getKills())
                .deaths(request.getDeaths())
                .assists(request.getAssists())
                .durationMinutes(request.getDurationMinutes())
                .build();

        return matchRepository.save(match);
    }
}