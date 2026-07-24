package e_commerce.khilat.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "cart_items")
public class CartItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "cart_id")
    private Cart cart;

    @ManyToOne
    @JoinColumn(name = "variant_id")
    private ProductVariant variant;

    private Integer quantity;

    private BigDecimal price;
    
    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;
    
    
    

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Cart getCart() {
		return cart;
	}

	public void setCart(Cart cart) {
		this.cart = cart;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}
	
	public ProductVariant getVariant() {
		return variant;
	}

	public void setVariant(ProductVariant variant) {
		this.variant = variant;
	}
    

	@Override
	public String toString() {
		return "CartItem [id=" + id + ", cart=" + cart + " , quantity=" + quantity + ", price="
				+ price + "]";
	}

	
    
}