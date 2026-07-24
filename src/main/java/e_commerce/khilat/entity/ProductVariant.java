package e_commerce.khilat.entity;

import java.math.BigDecimal;

import org.hibernate.annotations.Where;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "product_variant")
@Where(clause = "deleted = false")
public class ProductVariant {
    

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String size;
//    private String color;
    private BigDecimal price; 
    private Integer stock;

    @ManyToOne
    @JoinColumn(name = "product_id")
    @JsonIgnore
    private Product product;
    
    @Column(name="deleted")
    private boolean deleted = false;
    
    
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

    
    
    public boolean isDeleted() {
		return deleted;
	}

	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}


	public String getSize() {
		return size;
	}

	public void setSize(String size) {
		this.size = size;
	}

//	public String getColor() {
//		return color;
//	}
//
//	public void setColor(String color) {
//		this.color = color;
//	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public Integer getStock() {
		return stock;
	}

	public void setStock(Integer stock) {
		this.stock = stock;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}
}