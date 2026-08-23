package e_commerce.khilat.admin;

import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;

import e_commerce.khilat.dtomodels.JournalRequest;
import e_commerce.khilat.entity.Journal;
import e_commerce.khilat.service.JournalService;

@RestController
@RequestMapping("/api/admin")
@CrossOrigin
public class AdminJournalController {

    private static final Logger LOGGER = LoggerFactory.getLogger(AdminJournalController.class);

    @Autowired
    private JournalService journalService;

    @PostMapping(value = "/addjournal", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Journal> createJournalWithImages(
            @RequestPart("journal") String journalJson,
            @RequestPart(value = "images", required = false) List<MultipartFile> images) throws Exception {

        LOGGER.debug("process started of saving journal with image");

        ObjectMapper mapper = new ObjectMapper();
        JournalRequest request = mapper.readValue(journalJson, JournalRequest.class);

        return ResponseEntity.ok(journalService.createJournalWithImages(request, images));
    }

    @PostMapping(value = "/updatejournal/{journalId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Journal> updateJournalWithImages(
            @PathVariable Long journalId,
            @RequestPart("journal") String journalJson,
            @RequestPart(value = "images", required = false) List<MultipartFile> images,
            @RequestPart(value = "deleteImageIds", required = false) String deleteImageIds) throws Exception {

        ObjectMapper mapper = new ObjectMapper();
        JournalRequest request = mapper.readValue(journalJson, JournalRequest.class);

        List<Long> deleteIds = null;
        if (deleteImageIds != null && !deleteImageIds.isEmpty()) {
            deleteIds = Arrays.stream(deleteImageIds.split(",")).map(String::trim).map(Long::parseLong).toList();
        }

        Journal updated = journalService.updateJournalWithImages(journalId, request, images, deleteIds);
        return ResponseEntity.ok(updated);
    }

    @CacheEvict(value = "journals", allEntries = true)
    @DeleteMapping("/deletejournal/{journalId}")
    public ResponseEntity<String> deleteJournal(@PathVariable Long journalId) {
        journalService.deleteJournal(journalId);
        return ResponseEntity.ok("Journal deleted successfully");
    }

    @GetMapping("/getalljournals")
    public ResponseEntity<Page<Journal>> getAllJournals(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(journalService.getAllJournals(pageable));
    }
}