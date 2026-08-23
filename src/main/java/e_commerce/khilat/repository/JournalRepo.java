package e_commerce.khilat.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import e_commerce.khilat.entity.Journal;

@Repository
public interface JournalRepo extends JpaRepository<Journal, Long> {

    @Query("SELECT j FROM Journal j LEFT JOIN FETCH j.journalImages ORDER BY j.createdAt DESC")
    Page<Journal> findAllOrderByCreatedAtDesc(Pageable pageable);
}