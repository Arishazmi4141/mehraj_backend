package e_commerce.khilat.controller;

import java.math.BigDecimal;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import e_commerce.khilat.dtomodels.ProductRequest;
import e_commerce.khilat.service.ProductService;

@RestController
@RequestMapping("/api/product")
@CrossOrigin
public class ProductController {

	private static final Logger LOGGER = LoggerFactory.getLogger(ProductController.class);

	@Autowired
	private ProductService productService;

	@GetMapping("/latest")
	public ResponseEntity<?> getLatestProducts(@RequestParam(defaultValue = "8") int limit) {
		try {
			return ResponseEntity.ok(productService.getLatestProducts(limit));
		} catch (Exception ex) {
			LOGGER.error("Failed to fetch latest products", ex);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to fetch latest products");
		}
	}

	@GetMapping("/trending")
	public ResponseEntity<?> getTrendingProducts(@RequestParam(defaultValue = "8") int limit) {
		try {
			List<ProductRequest> trendingProducts = productService.getTrendingProducts(limit);
			return ResponseEntity.ok(trendingProducts);
		} catch (Exception ex) {
			LOGGER.error("Failed to fetch trending products", ex);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to fetch trending products");
		}
	}

	@GetMapping("/getProductById/{id}")
	public ResponseEntity<?> getProductByID(@PathVariable Long id) {
		try {
			ProductRequest product = productService.getProductById(id);
			return ResponseEntity.ok(product);
		} catch (Exception ex) {
			LOGGER.error("Failed to fetch product with id {}", id, ex);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to fetch product");
		}
	}

	@GetMapping("/getallproducts")
	public ResponseEntity<Page<ProductRequest>> getProducts(
			@RequestParam(required = false) String keyword,
			@RequestParam(required = false) String category,
			@RequestParam(required = false) BigDecimal minPrice,
			@RequestParam(required = false) BigDecimal maxPrice,
			@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size) {

		Pageable pageable = PageRequest.of(page, size);
		Page<ProductRequest> result = productService.filterProducts(keyword, category, minPrice, maxPrice, pageable);

		return ResponseEntity.ok(result);
	}
}