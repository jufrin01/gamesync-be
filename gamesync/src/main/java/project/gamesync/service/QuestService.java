package project.gamesync.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import project.gamesync.entity.Quest;
import project.gamesync.repository.QuestRepository;

import java.util.List;

@Service
public class QuestService {

    @Autowired
    private QuestRepository questRepository;

    /**
     * Mengambil semua quest yang tersedia.
     * Menggunakan method custom di Repository agar urut dari tanggal terbaru.
     */
    public List<Quest> getAllQuests() {
        return questRepository.findAllByOrderByDateDesc();
    }

    /**
     * Menyimpan quest baru ke database.
     * Controller sudah mengisi userId sebelum memanggil method ini.
     */
    public Quest createQuest(Quest quest) {
        // Logika Bisnis: Jika status tidak diisi, default ke "Open"
        if (quest.getStatus() == null || quest.getStatus().isEmpty()) {
            quest.setStatus("Open");
        }

        // Simpan ke database
        return questRepository.save(quest);
    }

    /**
     * Menghapus quest berdasarkan ID.
     */
    public void deleteQuest(Long id) {
        // Cek dulu apakah ID ada (opsional, untuk mencegah error silent)
        if (questRepository.existsById(id)) {
            questRepository.deleteById(id);
        } else {
            throw new RuntimeException("Quest not found with id: " + id);
        }
    }

    /**
     * (Opsional) Mengambil detail satu quest berdasarkan ID.
     */
    public Quest getQuestById(Long id) {
        return questRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Quest not found with id: " + id));
    }
}