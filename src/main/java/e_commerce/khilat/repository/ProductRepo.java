package e_commerce.khilat.repository;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import e_commerce.khilat.dtomodels.ProductSummaryDto;
import e_commerce.khilat.entity.Product;

@Repository
public interface ProductRepo extends JpaRepository<Product, Long> {

	@Query("""
	        SELECT p FROM Product p
	        JOIN FETCH p.category 
	        LEFT JOIN FETCH p.productImages
	        ORDER BY p.createdAt DESC
	    """)
	    List<Product> findLatestProducts(Pageable pageable);
	
	@Query("SELECT p FROM Product p WHERE p.trending = 'Y' ORDER BY p.createdAt DESC")
	List<Product> findTrendingProducts(Pageable pageable);
	
	
	
	
	@Query("""
		    SELECT DISTINCT p
		    FROM Product p
		    LEFT JOIN p.variants v
		    JOIN p.category c
		    WHERE (:keyword IS NULL 
		           OR LOWER(p.name) LIKE LOWER(CONCAT('%', CAST(:keyword AS string), '%')))
		      AND (:category IS NULL 
		           OR LOWER(c.name) = LOWER(CAST(:category AS string)))
		      AND (:minPrice IS NULL 
		           OR v.price >= :minPrice)
		      AND (:maxPrice IS NULL 
		           OR v.price <= :maxPrice)
		""")
		Page<Product> filterProducts(
		        @Param("keyword") String keyword,
		        @Param("category") String category,
		        @Param("minPrice") BigDecimal minPrice,
		        @Param("maxPrice") BigDecimal maxPrice,
		        Pageable pageable
		);
	
	
}
