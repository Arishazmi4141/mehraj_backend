package e_commerce.khilat.admin;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;

import e_commerce.khilat.dtomodels.ProductRequest;
import e_commerce.khilat.entity.Category;
import e_commerce.khilat.entity.SubCategory;
import e_commerce.khilat.entity.User;
import e_commerce.khilat.service.CategoryService;
import e_commerce.khilat.service.ProductService;
import e_commerce.khilat.service.SubCategoryService;
import e_commerce.khilat.service.UserService;

@RestController
@RequestMapping("/api/admin")
@CrossOrigin
public class AdminProductController {

	private static final Logger LOGGER = LoggerFactory.getLogger(AdminProductController.class);

	@Autowired
	private SubCategoryService subCategoryService;

	@Autowired
	private CategoryService categoryService;

	@Autowired
	private ProductService productService;

	@Autowired
	private UserService userService;

	@Autowired
	private JwtUtil jwtUtil;

	@Autowired
	private ObjectMapper mapper;

	// ===================== AUTH =====================

	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody User user) {
		User loggedInUser = userService.login(user.getEmail(), user.getPassword());
		String token = jwtUtil.generateToken(loggedInUser.getEmail());

		return ResponseEntity.ok(Map.of("token", token, "message", "Admin login successful"));
	}

	@PostMapping("/signup")
	public ResponseEntity<User> signup(@RequestBody User user) {
		return ResponseEntity.ok(userService.register(user));
	}

	// ===================== PRODUCTS =====================

	@PostMapping(value = "/addproducts", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<ProductRequest> createProductWithImages(
			@RequestPart("product") String productJson,
			@RequestPart(value = "images", required = false) List<MultipartFile> images) throws Exception {

		LOGGER.debug("process started of saving product with image");
		LOGGER.debug("productJson value : {}", productJson);

		ProductRequest request = mapper.readValue(productJson, ProductRequest.class);

		return ResponseEntity.ok(productService.createProductWithImages(request, images));
	}

	@PostMapping(value = "/updateproduct/{productId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<ProductRequest> updateProductWithImages(
			@PathVariable Long productId,
			@RequestPart("product") String productJson,
			@RequestPart(value = "images", required = false) List<MultipartFile> images,
			@RequestPart(value = "deleteImageIds", required = false) String deleteImageIds) throws Exception {

		ProductRequest request = mapper.readValue(productJson, ProductRequest.class);

		List<Long> deleteIds = null;
		if (deleteImageIds != null && !deleteImageIds.isBlank()) {
			deleteIds = Arrays.stream(deleteImageIds.split(","))
					.map(String::trim)
					.filter(s -> !s.isEmpty())
					.map(Long::parseLong)
					.toList();
		}

		ProductRequest updated = productService.updateProductWithImages(productId, request, images, deleteIds);

		return ResponseEntity.ok(updated);
	}

	// cache evict service layer mein already ho raha hai
	@DeleteMapping("/deleteproduct/{productId}")
	public ResponseEntity<String> deleteProduct(@PathVariable Long productId) {
		productService.deleteProduct(productId);
		return ResponseEntity.ok("Product deleted successfully");
	}

	@GetMapping("/getallproducts")
	public ResponseEntity<Page<ProductRequest>> getProducts(
			@RequestParam(required = false) String search,
			@RequestParam(required = false) String category,
			@RequestParam(required = false) BigDecimal minPrice,
			@RequestParam(required = false) BigDecimal maxPrice,
			@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size) {

		LOGGER.debug("getallproducts keyword : {}", search);

		Pageable pageable = PageRequest.of(page, size);
		Page<ProductRequest> result = productService.filterProducts(search, category, minPrice, maxPrice, pageable);

		return ResponseEntity.ok(result);
	}

	// ===================== CATEGORY =====================

	@PostMapping(value = "/addCategory", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<Category> addCategory(
			@RequestPart("category") String categoryJson,
			@RequestPart(value = "image", required = false) MultipartFile image) throws Exception {

		Category category = mapper.readValue(categoryJson, Category.class);
		Category saved = categoryService.addCategoryWithImage(category, image);
		return ResponseEntity.ok(saved);
	}

	@PostMapping("/bulk")
	public List<Category> addMultipleCategories(@RequestBody List<Category> categories) {
		return categoryService.addMultipleCategories(categories);
	}

	@PutMapping(value = "/updateCategory/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<Category> updateCategory(
			@PathVariable Long id,
			@RequestPart("category") String categoryJson,
			@RequestPart(value = "image", required = false) MultipartFile image) throws Exception {

		Category category = mapper.readValue(categoryJson, Category.class);
		Category updated = categoryService.updateCategoryWithImage(id, category, image);
		return ResponseEntity.ok(updated);
	}

	@DeleteMapping("/deleteCategory/{id}")
	public ResponseEntity<String> deleteCategory(@PathVariable Long id) {
		categoryService.deleteCategory(id);
		return ResponseEntity.ok("Category deleted successfully with id: " + id);
	}

	// ===================== SUB CATEGORY =====================

	@GetMapping("/getSubCategoriesByCategory/{categoryId}")
	public ResponseEntity<List<SubCategory>> getSubCategoriesByCategory(@PathVariable Long categoryId) {
		return ResponseEntity.ok(subCategoryService.getSubCategoriesByCategory(categoryId));
	}

	@DeleteMapping("/deleteSubCategory/{id}")
	public ResponseEntity<String> deleteSubCategory(@PathVariable Long id) {
		subCategoryService.deleteSubCategory(id);
		return ResponseEntity.ok("SubCategory deleted successfully with id: " + id);
	}

	@PostMapping(value = "/addSubCategory/{categoryId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<SubCategory> addSubCategoryWithImage(
			@PathVariable Long categoryId,
			@RequestPart("subCategory") String subCategoryJson,
			@RequestPart(value = "image", required = false) MultipartFile image) throws Exception {

		SubCategory subCategory = mapper.readValue(subCategoryJson, SubCategory.class);
		SubCategory saved = subCategoryService.addSubCategoryWithImage(categoryId, subCategory, image);
		return ResponseEntity.ok(saved);
	}

	@PutMapping(value = "/updateSubCategory/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<SubCategory> updateSubCategoryWithImage(
			@PathVariable Long id,
			@RequestPart("subCategory") String subCategoryJson,
			@RequestPart(value = "image", required = false) MultipartFile image) throws Exception {

		SubCategory subCategory = mapper.readValue(subCategoryJson, SubCategory.class);
		SubCategory updated = subCategoryService.updateSubCategoryWithImage(id, subCategory, image);
		return ResponseEntity.ok(updated);
	}
}