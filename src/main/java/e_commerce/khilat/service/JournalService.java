package e_commerce.khilat.service;

import java.time.LocalDateTime;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import e_commerce.khilat.dtomodels.JournalRequest;
import e_commerce.khilat.entity.Journal;
import e_commerce.khilat.entity.JournalImage;
import e_commerce.khilat.repository.JournalRepo;
import e_commerce.khilat.util.DateUtil;

@Service
public class JournalService {

    private static final Logger LOGGER = LoggerFactory.getLogger(JournalService.class);

    @Autowired
    private JournalRepo journalRepo;

    @Autowired
    private JournalImageService journalImageService;

    @CacheEvict(value = "journals", allEntries = true)
    public Journal createJournal(JournalRequest request) {

        if (request.getTitle() == null || request.getTitle().isBlank()) {
            throw new IllegalArgumentException("Title is required");
        }

        Journal journal = new Journal();
        journal.setTitle(request.getTitle());
        journal.setContent(request.getContent());
        journal.setCreatedAt(LocalDateTime.now());
        journal.setDtOfOps(DateUtil.dateConverterToLong(journal.getCreatedAt()));

        return journalRepo.save(journal);
    }

    @Transactional
    @CacheEvict(value = "journals", allEntries = true)
    public Journal createJournalWithImages(JournalRequest request, List<MultipartFile> images) {

        Journal savedJournal = createJournal(request);

        if (images != null && !images.isEmpty()) {
            LOGGER.debug("Uploading {} images for journal {}", images.size(), savedJournal.getId());
            for (MultipartFile file : images) {
                JournalImage savedImage = journalImageService.uploadJournalImage(savedJournal.getId(), file);
                savedJournal.getJournalImages().add(savedImage);
            }
        }

        return savedJournal;
    }

    @Transactional
    @CacheEvict(value = "journals", allEntries = true)
    public Journal updateJournalWithImages(Long journalId, JournalRequest request, List<MultipartFile> images,
            List<Long> deleteImageIds) {

        Journal journal = journalRepo.findById(journalId)
                .orElseThrow(() -> new RuntimeException("Journal not found"));

        journal.setTitle(request.getTitle());
        journal.setContent(request.getContent());

        if (deleteImageIds != null && !deleteImageIds.isEmpty()) {
            for (Long imageId : deleteImageIds) {
                journalImageService.deleteJournalImage(imageId);
            }
        }

        if (images != null && !images.isEmpty()) {
            for (MultipartFile file : images) {
                JournalImage image = journalImageService.uploadJournalImage(journal.getId(), file);
                journal.getJournalImages().add(image);
            }
        }

        return journalRepo.save(journal);
    }

    @CacheEvict(value = "journals", allEntries = true)
    @Transactional
    public void deleteJournal(Long journalId) {
        Journal journal = journalRepo.findById(journalId)
                .orElseThrow(() -> new RuntimeException("Journal not found"));
        journal.setDeleted(true);
        journalRepo.save(journal);
    }

    @Transactional(readOnly = true)
    public Journal getJournalById(Long id) {
        return journalRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Journal not found with id: " + id));
    }

    @Cacheable(value = "journals", key = "T(String).valueOf(#pageable.pageNumber) + '-' + T(String).valueOf(#pageable.pageSize)")
    public Page<Journal> getAllJournals(Pageable pageable) {
        return journalRepo.findAllOrderByCreatedAtDesc(pageable);
    }
}