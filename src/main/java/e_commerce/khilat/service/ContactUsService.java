package e_commerce.khilat.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.criteria.Predicate;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import e_commerce.khilat.dtomodels.ContactUsRequestDto;
import e_commerce.khilat.entity.ContactUsRequest;
import e_commerce.khilat.repository.ContactUsRepo;

@Service
public class ContactUsService {

    private final ContactUsRepo contactUsRepo;

    public ContactUsService(ContactUsRepo contactUsRepo) {
        this.contactUsRepo = contactUsRepo;
    }

    public ContactUsRequest submitContactUs(ContactUsRequestDto dto) {

        ContactUsRequest contactUs = new ContactUsRequest();
        contactUs.setName(dto.getName());
        contactUs.setEmail(dto.getEmail());
        contactUs.setPhoneNumber(dto.getPhoneNumber());
        contactUs.setMessage(dto.getMessage());
        contactUs.setCreatedAt(LocalDateTime.now());

        return contactUsRepo.save(contactUs);
    }

    public Page<ContactUsRequest> searchContactUs(String name, LocalDateTime fromDate, LocalDateTime toDate, Pageable pageable) {

        Specification<ContactUsRequest> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (name != null && !name.trim().isEmpty()) {
                predicates.add(cb.like(cb.lower(root.get("name")), "%" + name.trim().toLowerCase() + "%"));
            }
            if (fromDate != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("createdAt"), fromDate));
            }
            if (toDate != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("createdAt"), toDate));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };

        return contactUsRepo.findAll(spec, pageable);
    }

    public void deleteContactUs(Long id) {
        if (!contactUsRepo.existsById(id)) {
            throw new RuntimeException("Contact us request not found with id: " + id);
        }
        contactUsRepo.deleteById(id);
    }
}