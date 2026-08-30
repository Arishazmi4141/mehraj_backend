package e_commerce.khilat.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import e_commerce.khilat.dtomodels.ContactUsRequestDto;
import e_commerce.khilat.entity.ContactUsRequest;
import e_commerce.khilat.service.ContactUsService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/contactus")
@CrossOrigin
public class ContactUsController {

    @Autowired
    private ContactUsService contactUsService;

    @PostMapping("/submit")
    public ResponseEntity<ContactUsRequest> submitContactUs(@Valid @RequestBody ContactUsRequestDto dto) {
        ContactUsRequest saved = contactUsService.submitContactUs(dto);
        return ResponseEntity.ok(saved);
    }
}