package project.gamesync.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import project.gamesync.dto.Notification;
import project.gamesync.entity.Quest;
import project.gamesync.security.services.UserDetailsImpl;
import project.gamesync.service.QuestService;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@RequestMapping("/api/quests")
public class QuestController {

    @Autowired
    private QuestService questService;

    @Autowired
    private SimpMessagingTemplate messagingTemplate; // Tool untuk kirim WebSocket

    @GetMapping
    public List<Quest> getAllQuests() {
        return questService.getAllQuests();
    }

    @PostMapping
    public ResponseEntity<Quest> createQuest(@RequestBody Quest quest) {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        quest.setUserId(userDetails.getId());

        Quest newQuest = questService.createQuest(quest);

        String timeNow = LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm"));

        Notification notif = new Notification(
                "New Quest Available!",
                "Hero " + userDetails.getUsername() + " is looking for a squad in " + newQuest.getGame(),
                "QUEST",
                timeNow
        );

        messagingTemplate.convertAndSend("/topic/notifications", notif);

        return ResponseEntity.ok(newQuest);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteQuest(@PathVariable Long id) {
        questService.deleteQuest(id);
        return ResponseEntity.ok().build();
    }
}