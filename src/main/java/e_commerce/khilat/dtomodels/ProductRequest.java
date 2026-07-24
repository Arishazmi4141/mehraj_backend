package e_commerce.khilat.dtomodels;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import e_commerce.khilat.entity.Category;
import e_commerce.khilat.entity.ProductImage;
import e_commerce.khilat.entity.ProductVariant;
import e_commerce.khilat.entity.ReviewMessage;

public class ProductRequest {

	private Long categoryId;

	public List<ReviewMessage> getReviews() {
		return reviews;
	}

	public void setReviews(List<ReviewMessage> reviews) {
		this.reviews = reviews;
	}

	private Category category;
	private String name;
	private String description;

	private List<ProductVariant> variants;
	private String trending; // 'y' or 'n'
	private Boolean isActive;

	private List<ProductImage> productImages = new ArrayList<>();
	
	private List<ReviewMessage> reviews;

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

	public List<ProductVariant> getVariants() {
		return variants;
	}

	public void setVariants(List<ProductVariant> variants) {
		this.variants = variants;
	}

	public void setTrending(String trending) {
		this.trending = trending;
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

}