package e_commerce.khilat.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import e_commerce.khilat.entity.Magazine;

@Repository
public interface MagazineRepo extends JpaRepository<Magazine, Long> {

    @Query("SELECT m FROM Magazine m ORDER BY m.year DESC")
    Page<Magazine> findAllOrderByYearDesc(Pageable pageable);
}