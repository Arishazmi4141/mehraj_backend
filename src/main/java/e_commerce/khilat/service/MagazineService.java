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

import e_commerce.khilat.dtomodels.MagazineRequest;
import e_commerce.khilat.entity.Journal;
import e_commerce.khilat.entity.Magazine;
import e_commerce.khilat.repository.JournalRepo;
import e_commerce.khilat.repository.MagazineRepo;
import e_commerce.khilat.util.DateUtil;

@Service
public class MagazineService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MagazineService.class);

    @Autowired
    private MagazineRepo magazineRepo;

    @Autowired
    private JournalRepo journalRepo;

    @Autowired
    private CloudflareR2Service cloudflareR2Service;

    @Transactional
    @CacheEvict(value = { "journals", "magazines" }, allEntries = true)
    public Magazine createMagazineWithPdf(MagazineRequest request, MultipartFile pdf, boolean archiveJournals) {

        if (request.getTitle() == null || request.getTitle().isBlank()) {
            throw new IllegalArgumentException("Title is required");
        }
        if (pdf == null || pdf.isEmpty()) {
            throw new IllegalArgumentException("PDF file is required");
        }

        String pdfUrl;
        try {
            pdfUrl = cloudflareR2Service.uploadRawFile(pdf, "magazines");
        } catch (Exception e) {
            throw new RuntimeException("Magazine PDF upload failed", e);
        }

        Magazine magazine = new Magazine();
        magazine.setTitle(request.getTitle());
        magazine.setYear(request.getYear());
        magazine.setPdfUrl(pdfUrl);
        magazine.setCreatedAt(LocalDateTime.now());
        magazine.setDtOfOps(DateUtil.dateConverterToLong(magazine.getCreatedAt()));

        Magazine saved = magazineRepo.save(magazine);

        // 🔥 Year complete -> saare journals ko soft delete kar do
        if (archiveJournals) {
            List<Journal> allJournals = journalRepo.findAll();
            for (Journal journal : allJournals) {
                journal.setDeleted(true);
            }
            journalRepo.saveAll(allJournals);
            LOGGER.debug("Archived {} journals after magazine {} creation", allJournals.size(), saved.getId());
        }

        return saved;
    }

    @CacheEvict(value = "magazines", allEntries = true)
    @Transactional
    public void deleteMagazine(Long magazineId) {
        Magazine magazine = magazineRepo.findById(magazineId)
                .orElseThrow(() -> new RuntimeException("Magazine not found"));
        magazine.setDeleted(true);
        magazineRepo.save(magazine);
    }

    @Cacheable(value = "magazines", key = "T(String).valueOf(#pageable.pageNumber) + '-' + T(String).valueOf(#pageable.pageSize)")
    public Page<Magazine> getAllMagazines(Pageable pageable) {
        return magazineRepo.findAllOrderByYearDesc(pageable);
    }
}