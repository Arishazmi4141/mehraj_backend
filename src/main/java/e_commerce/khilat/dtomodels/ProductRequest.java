package e_commerce.khilat.dtomodels;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import e_commerce.khilat.entity.Category;
import e_commerce.khilat.entity.ProductImage;
import e_commerce.khilat.entity.ProductVariant;
import e_commerce.khilat.entity.ReviewMessage;
import e_commerce.khilat.entity.SubCategory;

public class ProductRequest {

	private Long id;
	private LocalDateTime createdAt;
	private Long dtOfOps;
	

	private Long categoryId;
	private Category category;

	private Long subCategoryId;
	private SubCategory subCategory;

	private String name;
	private String description;

	private List<ProductVariant> variants;
	private String trending; // 'y' or 'n'
	private Boolean isActive;

	private List<ProductImage> productImages = new ArrayList<>();

	private List<ReviewMessage> reviews;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	public Long getDtOfOps() {
		return dtOfOps;
	}

	public void setDtOfOps(Long dtOfOps) {
		this.dtOfOps = dtOfOps;
	}

	public List<ReviewMessage> getReviews() {
		return reviews;
	}

	public void setReviews(List<ReviewMessage> reviews) {
		this.reviews = reviews;
	}

	public List<ProductImage> getProductImages() {
		return productImages;
	}

	public void setProductImages(List<ProductImage> productImages) {
		this.productImages = productImages;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getTrending() {
		return trending;
	}

	public void setTrending(String trending) {
		this.trending = trending;
	}

	public List<ProductVariant> getVariants() {
		return variants;
	}

	public void setVariants(List<ProductVariant> variants) {
		this.variants = variants;
	}

	public Boolean getIsActive() {
		return isActive;
	}

	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	public Long getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
	}

	public Long getSubCategoryId() {
		return subCategoryId;
	}

	public void setSubCategoryId(Long subCategoryId) {
		this.subCategoryId = subCategoryId;
	}

	public SubCategory getSubCategory() {
		return subCategory;
	}

	public void setSubCategory(SubCategory subCategory) {
		this.subCategory = subCategory;
	}
}