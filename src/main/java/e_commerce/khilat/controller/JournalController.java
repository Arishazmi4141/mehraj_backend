package e_commerce.khilat.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import e_commerce.khilat.entity.Journal;
import e_commerce.khilat.service.JournalService;

@RestController
@RequestMapping("/api/journals")
@CrossOrigin
public class JournalController {

    @Autowired
    private JournalService journalService;

    // List view -> journal page pe cards dikhane ke liye
    @GetMapping
    public ResponseEntity<Page<Journal>> getAllJournals(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "12") int size) {

        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(journalService.getAllJournals(pageable));
    }

    // Single journal open karke padhne ke liye
    @GetMapping("/{id}")
    public ResponseEntity<Journal> getJournalById(@PathVariable Long id) {
        return ResponseEntity.ok(journalService.getJournalById(id));
    }
}