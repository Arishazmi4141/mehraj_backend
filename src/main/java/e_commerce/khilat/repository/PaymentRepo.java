package e_commerce.khilat.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import e_commerce.khilat.entity.Payment;

@Repository
public interface PaymentRepo extends JpaRepository<Payment, Long>{
	
	Optional<Payment> findByTransactionId(String transactionId);
	
	Optional<Payment> findByOrderId(Long orderId);
	
	boolean existsByTransactionId(String transactionId);

}
