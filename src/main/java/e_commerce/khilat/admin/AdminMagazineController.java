package e_commerce.khilat.admin;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;

import e_commerce.khilat.dtomodels.MagazineRequest;
import e_commerce.khilat.entity.Magazine;
import e_commerce.khilat.service.MagazineService;

@RestController
@RequestMapping("/api/admin")
@CrossOrigin
public class AdminMagazineController {

    private static final Logger LOGGER = LoggerFactory.getLogger(AdminMagazineController.class);

    @Autowired
    private MagazineService magazineService;

    // archiveJournals=true -> is magazine ke banate hi saare purane journals delete ho jayenge
    @PostMapping(value = "/addmagazine", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Magazine> createMagazine(
            @RequestPart("magazine") String magazineJson,
            @RequestPart("pdf") MultipartFile pdf,
            @RequestParam(defaultValue = "true") boolean archiveJournals) throws Exception {

        ObjectMapper mapper = new ObjectMapper();
        MagazineRequest request = mapper.readValue(magazineJson, MagazineRequest.class);

        LOGGER.debug("process started of saving magazine with pdf, archiveJournals={}", archiveJournals);

        return ResponseEntity.ok(magazineService.createMagazineWithPdf(request, pdf, archiveJournals));
    }

    @DeleteMapping("/deletemagazine/{magazineId}")
    public ResponseEntity<String> deleteMagazine(@PathVariable Long magazineId) {
        magazineService.deleteMagazine(magazineId);
        return ResponseEntity.ok("Magazine deleted successfully");
    }

    @GetMapping("/getallmagazines")
    public ResponseEntity<Page<Magazine>> getAllMagazines(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(magazineService.getAllMagazines(pageable));
    }
}