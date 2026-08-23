package e_commerce.khilat.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import e_commerce.khilat.entity.Journal;
import e_commerce.khilat.entity.JournalImage;
import e_commerce.khilat.repository.JournalImageRepo;
import e_commerce.khilat.repository.JournalRepo;

@Service
public class JournalImageService {

    private static final Logger LOGGER = LoggerFactory.getLogger(JournalImageService.class);

    @Autowired
    private JournalImageRepo journalImageRepo;

    @Autowired
    private JournalRepo journalRepo;

    @Autowired
    private CloudflareR2Service cloudflareR2Service;

    public List<JournalImage> getImagesByJournal(Long journalId) {
        return journalImageRepo.findByJournal_Id(journalId);
    }

    public JournalImage uploadJournalImage(Long journalId, MultipartFile file) {

        if (file == null || file.isEmpty()) {
            throw new RuntimeException("Image is required");
        }

        try {
            Journal journal = journalRepo.findById(journalId)
                    .orElseThrow(() -> new RuntimeException("Journal not found"));

            String imageUrl = cloudflareR2Service.uploadImage(file, "journals");

            JournalImage image = new JournalImage();
            image.setJournal(journal);
            image.setImageUrl(imageUrl);

            return journalImageRepo.save(image);

        } catch (Exception e) {
            throw new RuntimeException("Journal image upload failed", e);
        }
    }

    public void deleteJournalImage(Long imageId) {
        JournalImage image = journalImageRepo.findById(imageId)
                .orElseThrow(() -> new RuntimeException("Journal image not found"));
        journalImageRepo.delete(image);
    }
}