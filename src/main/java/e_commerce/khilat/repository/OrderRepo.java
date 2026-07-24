package e_commerce.khilat.repository;

import java.util.UUID;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import e_commerce.khilat.entity.Order;

@Repository
public interface OrderRepo extends JpaRepository<Order, Long> {
	
//	<List>Order findByguestId(UUID guestId);
	
	
	Optional<Order> findByPaymentId(Long paymentId);
	
	Page<Order> findByStatus(String status, Pageable pageable);
	
	Page<Order> findByStatusAndUpdatedDtOfOps(String status,Long UpdatedDtOfOps,Pageable pageable);
	
	Page<Order> findByStatusAndCreatedAtBetween(String status, LocalDateTime start, LocalDateTime end, Pageable pageable);	
	
	Optional<Order> findByTrackingKey(String trckngKey);
	
	Page<Order> findByStatusAndTrackingKey(String status,String trackingKey,Pageable pageable);

	Page<Order> findByStatusAndUpdatedDtOfOpsAndTrackingKey(String status, Long updatedDtOfOps,String trackingKey, Pageable pageable);
	
	
	
	
	
	
	

}
