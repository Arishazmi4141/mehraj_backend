package e_commerce.khilat.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "review_message")
public class ReviewMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long productId;
    
    
    private String reviewerName;
    
    
    
    @Column(columnDefinition = "TEXT")
    private String reviewMsg;
    
    private Integer rating;
    
    

    @Column(name = "created_at")
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



	@Override
	public String toString() {
		return "ReviewMessage [id=" + id + ", productId=" + productId + ", reviewerName=" + reviewerName
				+ ", reviewMsg=" + reviewMsg + ", rating=" + rating + ", createdAt=" + createdAt + "]";
	}
    
    
}
