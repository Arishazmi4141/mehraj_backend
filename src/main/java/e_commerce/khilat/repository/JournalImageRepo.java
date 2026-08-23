package e_commerce.khilat.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import e_commerce.khilat.entity.JournalImage;

@Repository
public interface JournalImageRepo extends JpaRepository<JournalImage, Long> {
    List<JournalImage> findByJournal_Id(Long journalId);
}