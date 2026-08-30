package e_commerce.khilat.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import e_commerce.khilat.dtomodels.ConsultationRequestDto;
import e_commerce.khilat.entity.ConsultationRequest;
import e_commerce.khilat.service.ConsultationService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/consultation")
@CrossOrigin
public class ConsultationController {

    @Autowired
    private ConsultationService consultationService;

    @PostMapping("/submit")
    public ResponseEntity<ConsultationRequest> submitConsultation(@Valid @RequestBody ConsultationRequestDto dto) {
        ConsultationRequest saved = consultationService.submitConsultation(dto);
        return ResponseEntity.ok(saved);
    }
}