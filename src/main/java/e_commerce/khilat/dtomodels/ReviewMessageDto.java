package e_commerce.khilat.dtomodels;

import java.time.LocalDateTime;



public class ReviewMessageDto {

	private Long id;

	private Long productId;

	private String reviewerName;

	private String reviewMsg;

	private Integer rating;

	private LocalDateTime createdAt;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getProductId() {
		return productId;
	}

	public void setProductId(Long productId) {
		this.productId = productId;
	}

	public String getReviewerName() {
		return reviewerName;
	}

	public void setReviewerName(String reviewerName) {
		this.reviewerName = reviewerName;
	}

	public String getReviewMsg() {
		return reviewMsg;
	}

	public void setReviewMsg(String reviewMsg) {
		this.reviewMsg = reviewMsg;
	}

	public Integer getRating() {
		return rating;
	}

	public void setRating(Integer rating) {
		this.rating = rating;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

}
