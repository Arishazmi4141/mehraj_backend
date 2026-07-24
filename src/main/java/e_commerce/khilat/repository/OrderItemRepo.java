package e_commerce.khilat.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import e_commerce.khilat.entity.OrderItem;
import e_commerce.khilat.entity.Product;

@Repository
public interface OrderItemRepo extends JpaRepository<OrderItem, Long> {
	
	List<OrderItem> findByOrderId(Long orderId);
	
//	List<Product> findByProductId(Long productId);


}
