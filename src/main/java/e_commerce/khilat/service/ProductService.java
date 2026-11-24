package e_commerce.khilat.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import e_commerce.khilat.dtomodels.ProductRequest;
import e_commerce.khilat.entity.Category;
import e_commerce.khilat.entity.Product;
import e_commerce.khilat.entity.ProductImage;
import e_commerce.khilat.entity.ProductVariant;
import e_commerce.khilat.entity.SubCategory;
import e_commerce.khilat.repository.CategoryRepo;
import e_commerce.khilat.repository.ProductRepo;
import e_commerce.khilat.repository.ReviewRepo;
import e_commerce.khilat.repository.SubCategoryRepo;
import e_commerce.khilat.util.DateUtil;

@Service
public class ProductService {

	private static final Logger LOGGER = LoggerFactory.getLogger(ProductService.class);

	@Autowired
	private ProductRepo productRepo;

	@Autowired
	private CategoryRepo categoryRepo;

	@Autowired
	private SubCategoryRepo subCategoryRepo;

	@Autowired
	private ReviewRepo reviewRepo;

	@Autowired
	private ProductImageService productImageService;

	// ===================== MAPPER =====================

	public ProductRequest toDto(Product product) {
		ProductRequest dto = new ProductRequest();
		dto.setId(product.getId());
		dto.setName(product.getName());
		dto.setDescription(product.getDescription());
		dto.setTrending(product.getTrending());
		dto.setIsActive(product.getIsActive());
		dto.setCreatedAt(product.getCreatedAt());
		dto.setDtOfOps(product.getDtOfOps());

		dto.setCategory(product.getCategory());
		dto.setCategoryId(product.getCategory() != null ? product.getCategory().getId() : null);
		dto.setSubCategory(product.getSubCategory());
		dto.setSubCategoryId(product.getSubCategory() != null ? product.getSubCategory().getId() : null);

		// lazy collections transaction ke andar load hongi
		dto.setProductImages(
				product.getProductImages() != null ? new ArrayList<>(product.getProductImages()) : new ArrayList<>());
		dto.setVariants(product.getVariants() != null ? new ArrayList<>(product.getVariants()) : new ArrayList<>());
		return dto;
	}

	// ===================== READ =====================

	@Transactional(readOnly = true)
	public List<ProductRequest> getLatestProducts(int limit) {
		if (limit <= 0 || limit > 50) {
			limit = 8; // safe default
		}
		Pageable pageable = PageRequest.of(0, limit, Sort.by(Sort.Direction.DESC, "createdAt"));
		return productRepo.findLatestProducts(pageable).stream().map(this::toDto).toList();
	}

	@Transactional(readOnly = true)
	public List<ProductRequest> getTrendingProducts(int limit) {
		Pageable pageable = PageRequest.of(0, limit, Sort.by(Sort.Direction.DESC, "createdAt"));
		return productRepo.findTrendingProducts(pageable).stream().map(this::toDto).toList();
	}

	@Transactional(readOnly = true)
	public ProductRequest getProductById(Long id) {
		Product product = productRepo.findById(id)
				.orElseThrow(() -> new RuntimeException("Product not found with id: " + id));

		ProductRequest dto = toDto(product);
		dto.setReviews(reviewRepo.findTop5ByProductIdOrderByIdDesc(id));
		return dto;
	}

	@Transactional(readOnly = true)
	@Cacheable(value = "products", key = "T(String).valueOf(#pageable.pageNumber) + '-' + T(String).valueOf(#pageable.pageSize) + '-' + #keyword + '-' + #category + '-' + #minPrice + '-' + #maxPrice")
	public Page<ProductRequest> filterProducts(String keyword, String category, BigDecimal minPrice,
			BigDecimal maxPrice, Pageable pageable) {

		Page<Product> page;
		if (keyword == null && category == null && minPrice == null && maxPrice == null) {
			page = productRepo.findAll(pageable);
		} else {
			page = productRepo.filterProducts(keyword, category, minPrice, maxPrice, pageable);
		}
		return page.map(this::toDto);
	}

	// ===================== CREATE =====================

	@Transactional
	@CacheEvict(value = { "products", "productsDetail" }, allEntries = true)
	public Product createProduct(ProductRequest request) {

		if (request.getCategoryId() == null) {
			throw new IllegalArgumentException("Category ID is required");
		}

		Category category = categoryRepo.findById(request.getCategoryId())
				.orElseThrow(() -> new RuntimeException("Category not found"));

		Product product = new Product();
		product.setCategory(category);

		// SubCategory optional hai, agar bheji ho to validate + set karo
		if (request.getSubCategoryId() != null) {
			SubCategory subCategory = subCategoryRepo.findById(request.getSubCategoryId())
					.orElseThrow(() -> new RuntimeException("SubCategory not found"));

			// safety check: subCategory usi category ke andar honi chahiye
			if (!subCategory.getCategory().getId().equals(category.getId())) {
				throw new IllegalArgumentException("SubCategory does not belong to selected Category");
			}

			product.setSubCategory(subCategory);
		}

		product.setName(request.getName());
		product.setDescription(request.getDescription());

		if (request.getVariants() != null) {
			for (ProductVariant variant : request.getVariants()) {
				variant.setProduct(product);
			}
			product.setVariants(request.getVariants());
		}

		product.setIsActive(request.getIsActive() != null ? request.getIsActive() : true);
		product.setTrending(request.getTrending() != null ? request.getTrending() : "n");
		product.setCreatedAt(LocalDateTime.now());
		product.setDtOfOps(DateUtil.dateConverterToLong(product.getCreatedAt()));

		return productRepo.save(product);
	}

	@Transactional
	@CacheEvict(value = { "products", "productsDetail" }, allEntries = true)
	public ProductRequest createProductWithImages(ProductRequest request, List<MultipartFile> images) {

		Product savedProduct = createProduct(request);

		if (images != null && !images.isEmpty()) {
			LOGGER.debug("Uploading {} images for product {}", images.size(), savedProduct.getId());
			for (MultipartFile file : images) {
				ProductImage savedImage = productImageService.uploadProductImage(savedProduct.getId(), file);
				savedProduct.getProductImages().add(savedImage);
			}
		}

		return toDto(savedProduct);
	}

	// ===================== UPDATE =====================

	@CacheEvict(value = { "products", "productsDetail" }, allEntries = true)
	@Transactional
	public ProductRequest updateProductWithImages(Long productId, ProductRequest request,
			List<MultipartFile> images, List<Long> deleteImageIds) {

		Product product = productRepo.findById(productId)
				.orElseThrow(() -> new RuntimeException("Product not found"));

		product.setName(request.getName());
		product.setDescription(request.getDescription());
		product.setIsActive(request.getIsActive() != null ? request.getIsActive() : product.getIsActive());
		product.setTrending(request.getTrending() != null ? request.getTrending() : product.getTrending());

		if (request.getVariants() != null) {
			if (product.getVariants() == null) {
				product.setVariants(new ArrayList<>());
			}
			product.getVariants().clear();
			for (ProductVariant variant : request.getVariants()) {
				variant.setProduct(product);
				product.getVariants().add(variant);
			}
		}

		// Category update
		Category category = product.getCategory();
		if (request.getCategoryId() != null) {
			category = categoryRepo.findById(request.getCategoryId())
					.orElseThrow(() -> new RuntimeException("Category not found"));
			product.setCategory(category);
		}

		// SubCategory update
		if (request.getSubCategoryId() != null) {
			SubCategory subCategory = subCategoryRepo.findById(request.getSubCategoryId())
					.orElseThrow(() -> new RuntimeException("SubCategory not found"));

			if (!subCategory.getCategory().getId().equals(category.getId())) {
				throw new IllegalArgumentException("SubCategory does not belong to selected Category");
			}

			product.setSubCategory(subCategory);
		}

		// Images delete
		if (deleteImageIds != null && !deleteImageIds.isEmpty()) {
			for (Long imageId : deleteImageIds) {
				productImageService.deleteProductImage(imageId);
			}
			product.getProductImages().removeIf(img -> deleteImageIds.contains(img.getId()));
		}

		// Images add
		if (images != null && !images.isEmpty()) {
			for (MultipartFile file : images) {
				ProductImage image = productImageService.uploadProductImage(product.getId(), file);
				product.getProductImages().add(image);
			}
		}

		return toDto(productRepo.save(product));
	}

	// ===================== DELETE =====================

	@CacheEvict(value = { "products", "productsDetail" }, allEntries = true)
	@Transactional
	public void deleteProduct(Long productId) {
		Product product = productRepo.findById(productId)
				.orElseThrow(() -> new RuntimeException("Product not found"));

		product.setDeleted(true);
		if (product.getVariants() != null) {
			product.getVariants().forEach(v -> v.setDeleted(true));
		}
		productRepo.save(product);
	}
}