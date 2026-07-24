package e_commerce.khilat.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import e_commerce.khilat.entity.Cart;
import e_commerce.khilat.entity.CartItem;
import e_commerce.khilat.entity.Product;
import e_commerce.khilat.entity.ProductVariant;
import jakarta.transaction.Transactional;

@Repository
public interface CartItemRepo extends JpaRepository<CartItem, Long> {
	
	 List<CartItem> findByCart(Cart cart);
	 
	 @Query("""
			    SELECT DISTINCT ci
			    FROM CartItem ci
			    JOIN FETCH ci.variant v
			    JOIN FETCH v.product p
			    LEFT JOIN FETCH p.variants
			    WHERE ci.cart = :cart
			""")
			List<CartItem> findByCartWithProductDetails(@Param("cart") Cart cart);
	 Optional<CartItem> findByCartAndVariant(Cart cart, ProductVariant variant);
	 
	 
	 
	 @Modifying
	    @Transactional
	    @Query("DELETE FROM CartItem ci WHERE ci.cart = :cart")
	    void deleteAllByCart(@Param("cart") Cart cart);

}
