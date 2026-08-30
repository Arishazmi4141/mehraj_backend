package e_commerce.khilat.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import e_commerce.khilat.entity.ConsultationRequest;

@Repository
public interface ConsultationRepo extends JpaRepository<ConsultationRequest, Long> {

    @Query("""
        SELECT c FROM ConsultationRequest c
        WHERE (:name IS NULL OR LOWER(c.name) LIKE LOWER(CONCAT('%', CAST(:name AS string), '%')))
          AND (:fromDate IS NULL OR c.createdAt >= :fromDate)
          AND (:toDate IS NULL OR c.createdAt <= :toDate)
        ORDER BY c.createdAt DESC
    """)
    Page<ConsultationRequest> searchConsultations(
            @Param("name") String name,
            @Param("fromDate") LocalDateTime fromDate,
            @Param("toDate") LocalDateTime toDate,
            Pageable pageable
    );
}