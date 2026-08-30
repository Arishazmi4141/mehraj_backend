package e_commerce.khilat.service;

import java.time.LocalDateTime;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import e_commerce.khilat.dtomodels.ConsultationRequestDto;
import e_commerce.khilat.entity.ConsultationRequest;
import e_commerce.khilat.repository.ConsultationRepo;

@Service
public class ConsultationService {

    private final ConsultationRepo consultationRepo;

    public ConsultationService(ConsultationRepo consultationRepo) {
        this.consultationRepo = consultationRepo;
    }

    public ConsultationRequest submitConsultation(ConsultationRequestDto dto) {

        // Kam se kam ek preferred slot mandatory hai
        if (dto.getPreferredSlot1() == null
                && dto.getPreferredSlot2() == null
                && dto.getPreferredSlot3() == null) {
            throw new IllegalArgumentException("At least one preferred date & time slot is required");
        }

        ConsultationRequest consultation = new ConsultationRequest();
        consultation.setName(dto.getName());
        consultation.setEmail(dto.getEmail());
        consultation.setPhoneNumber(dto.getPhoneNumber());
        consultation.setMessage(dto.getMessage());
        consultation.setPreferredSlot1(dto.getPreferredSlot1());
        consultation.setPreferredSlot2(dto.getPreferredSlot2());
        consultation.setPreferredSlot3(dto.getPreferredSlot3());
        consultation.setCreatedAt(LocalDateTime.now());

        return consultationRepo.save(consultation);
    }

    public Page<ConsultationRequest> searchConsultations(String name, LocalDateTime fromDate, LocalDateTime toDate, Pageable pageable) {
        return consultationRepo.searchConsultations(name, fromDate, toDate, pageable);
    }

    public void deleteConsultation(Long id) {
        if (!consultationRepo.existsById(id)) {
            throw new RuntimeException("Consultation request not found with id: " + id);
        }
        consultationRepo.deleteById(id);
    }
}