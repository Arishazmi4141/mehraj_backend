package e_commerce.khilat.admin;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.data.web.PageableDefault;
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
import e_commerce.khilat.entity.Product;
import e_commerce.khilat.entity.User;
import e_commerce.khilat.service.CategoryService;
import e_commerce.khilat.service.ProductService;
import e_commerce.khilat.service.UserService;

@RestController
@RequestMapping("/api/admin")
@CrossOrigin
public class AdminProductController {

	private static final Logger LOGGER = LoggerFactory.getLogger(AdminProductController.class);
	
	
	 @Autowired
	 private  CategoryService categoryService;

	@Autowired
	private ProductService productService;

	@Autowired
	private UserService userService;
	
	@Autowired
	private JwtUtil jwtUtil;

	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody User user) {

	    User loggedInUser =
	            userService.login(user.getEmail(), user.getPassword());

	    String token = jwtUtil.generateToken(loggedInUser.getEmail());

	    return ResponseEntity.ok(
	            Map.of(
	                "token", token,
	                "message", "Admin login successful"
	            )
	    );
	}
	
	

	@PostMapping("/signup")
	public ResponseEntity<User> signup(@RequestBody User user) {
		return ResponseEntity.ok(userService.register(user));
	}
	
	
	

	
	@PostMapping(value = "/addproducts", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<Product> createProductWithImages(@RequestPart("product") String productJson,
			@RequestPart(value = "images", required = false) List<MultipartFile> images) throws Exception {
		
		System.out.println("process started of saving product with image");
		LOGGER.debug("process started of saving product with image");

		LOGGER.debug("productJSon Vaue : {}", productJson.toString());

		ObjectMapper mapper = new ObjectMapper();
		ProductRequest request = mapper.readValue(productJson, ProductRequest.class);

		LOGGER.debug("request before sending to service Vaue : {}", request.toString());
		LOGGER.debug("image Vaue : {}", images);

		return ResponseEntity.ok(productService.createProductWithImages(request, images));
	}
	
	

	
	@PostMapping(value = "/updateproduct/{productId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<Product> updateProductWithImages(@PathVariable Long productId,
			@RequestPart("product") String productJson,
			@RequestPart(value = "images", required = false) List<MultipartFile> images,
			@RequestPart(value = "deleteImageIds", required = false) String deleteImageIds) throws Exception {

		ObjectMapper mapper = new ObjectMapper();
		ProductRequest request = mapper.readValue(productJson, ProductRequest.class);

		List<Long> deleteIds = null;
		if (deleteImageIds != null && !deleteImageIds.isEmpty()) {
			deleteIds = Arrays.stream(deleteImageIds.split(",")).map(String::trim).map(Long::parseLong).toList();
		}

		Product updated = productService.updateProductWithImages(productId, request, images, deleteIds);

		return ResponseEntity.ok(updated);
	}
	
	
	
	@CacheEvict(value = "products", allEntries = true)
	@DeleteMapping("/deleteproduct/{productId}")
    public ResponseEntity<String> deleteProduct(@PathVariable Long productId) {

        productService.deleteProduct(productId);

        return ResponseEntity.ok("Product deleted successfully");
    }
	
	@GetMapping("/getallproducts")
	 public ResponseEntity<Page<Product>> getProducts(
	            @RequestParam(required = false) String search,
	            @RequestParam(required = false) String category,
	            @RequestParam(required = false) BigDecimal minPrice,
	            @RequestParam(required = false) BigDecimal maxPrice,
	            @RequestParam(defaultValue = "0") int page,
		        @RequestParam(defaultValue = "10") int size) {
		
		String keyword = search;
		
		System.out.println("keyword :" + keyword);

			Pageable pageable = PageRequest.of(page, size);
	        Page<Product> result =
	                productService.filterProducts(
	                        keyword, category, minPrice, maxPrice, pageable);

	        return ResponseEntity.ok(result);
	    }
	  


		
	    @PostMapping("/addCategory")
	    public Category addCategory(@RequestBody Category category) {
	        return categoryService.addCategory(category);
	    }

	    @PostMapping("/bulk")
	    public List<Category> addMultipleCategories(@RequestBody List<Category> categories) {
	        return categoryService.addMultipleCategories(categories);
	    }
	    
	   
	    @PutMapping("/updateCategory/{id}")
	    public ResponseEntity<Category> updateCategory(@PathVariable Long id, @RequestBody Category category) {
	        Category updatedCategory = categoryService.updateCategory(id, category);
	        return ResponseEntity.ok(updatedCategory);
	    }

	    
	    @DeleteMapping("/deleteCategory/{id}")
	    public ResponseEntity<String> deleteCategory(@PathVariable Long id) {
	        categoryService.deleteCategory(id);
	        return ResponseEntity.ok("Category deleted successfully with id: " + id);
	    }
	

}
