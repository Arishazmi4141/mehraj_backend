package e_commerce.khilat.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import e_commerce.khilat.entity.Category;

@Repository
public interface CategoryRepo extends JpaRepository<Category, Long> {

    Optional<Category> findByName(String name);
}
