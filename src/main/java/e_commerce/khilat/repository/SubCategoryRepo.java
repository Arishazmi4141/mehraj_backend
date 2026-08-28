package e_commerce.khilat.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import e_commerce.khilat.entity.SubCategory;

@Repository
public interface SubCategoryRepo extends JpaRepository<SubCategory, Long> {

    List<SubCategory> findByCategory_Id(Long categoryId);

    Optional<SubCategory> findByNameAndCategory_Id(String name, Long categoryId);
}