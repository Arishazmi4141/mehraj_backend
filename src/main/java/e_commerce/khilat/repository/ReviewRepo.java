package e_commerce.khilat.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import e_commerce.khilat.entity.ReviewMessage;

@Repository
public interface ReviewRepo extends JpaRepository<ReviewMessage, Long> {
	
	
	
	List<ReviewMessage> findTop5ByProductIdOrderByIdDesc(Long id);
	
	
	
	

}