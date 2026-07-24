package e_commerce.khilat.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Where;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
@Table(name = "products")
@Where(clause = "deleted = false")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;
    
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference
    @BatchSize(size = 20)
    private List<ProductImage> productImages = new ArrayList<>(); 
    
    private String name;

    private String description;
    
    @Column(name = "trending", length = 15)
    private String trending;
    
    @SuppressWarnings("deprecation")
	@OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProductVariant> variants;
    


    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    @Column(name = "dt_of_ops")
    private Long dtOfOps;
    
    @Column(name="deleted")
    private boolean deleted = false;
    


	public List<ProductVariant> getVariants() {
		return variants;
	}
    
    

	public void setVariants(List<ProductVariant> variants) {
		this.variants = variants;
	}

	public List<ProductImage> getProductImages() {
		return productImages;
	}

	public void setProductImages(List<ProductImage> productImages) {
		this.productImages = productImages;
	}


	public String getTrending() {
		return trending;
	}

	public void setTrending(String trending) {
		this.trending = trending;
	}

	@Column(name = "is_active")
	private Boolean isActive;

	public Boolean getIsActive() { return isActive; }
	public void setIsActive(Boolean isActive) { this.isActive = isActive; }

	
	

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
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


	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

    public Long getDtOfOps() {
		return dtOfOps;
	}



	public boolean isDeleted() {
		return deleted;
	}



	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}



	public void setDtOfOps(Long dtOfOps) {
		this.dtOfOps = dtOfOps;
	}

	
	@Override
	public String toString() {
		return "Product [id=" + id + ", category=" + category + ", name=" + name + ", description=" + description
				+  ", trending=" + trending + ", createdAt=" + createdAt + "]";
	}   
}