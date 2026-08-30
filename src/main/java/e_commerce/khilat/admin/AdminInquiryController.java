package e_commerce.khilat.admin;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import e_commerce.khilat.entity.ConsultationRequest;
import e_commerce.khilat.entity.ContactUsRequest;
import e_commerce.khilat.service.ConsultationService;
import e_commerce.khilat.service.ContactUsService;

@RestController
@RequestMapping("/api/admin")
@CrossOrigin
public class AdminInquiryController {

    @Autowired
    private ConsultationService consultationService;

    @Autowired
    private ContactUsService contactUsService;

    // ---------- Consultation ----------

    @GetMapping("/consultations")
    public ResponseEntity<Page<ConsultationRequest>> getConsultations(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fromDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime toDate,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);
        Page<ConsultationRequest> result = consultationService.searchConsultations(name, fromDate, toDate, pageable);
        return ResponseEntity.ok(result);
    }

    @DeleteMapping("/consultations/{id}")
    public ResponseEntity<String> deleteConsultation(@PathVariable Long id) {
        consultationService.deleteConsultation(id);
        return ResponseEntity.ok("Consultation request deleted successfully with id: " + id);
    }

    // ---------- Contact Us ----------

    @GetMapping("/contactus")
    public ResponseEntity<Page<ContactUsRequest>> getContactUsRequests(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fromDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime toDate,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);
        Page<ContactUsRequest> result = contactUsService.searchContactUs(name, fromDate, toDate, pageable);
        return ResponseEntity.ok(result);
    }

    @DeleteMapping("/contactus/{id}")
    public ResponseEntity<String> deleteContactUs(@PathVariable Long id) {
        contactUsService.deleteContactUs(id);
        return ResponseEntity.ok("Contact us request deleted successfully with id: " + id);
    }
}