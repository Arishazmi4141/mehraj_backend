package e_commerce.khilat.dtomodels;

import java.math.BigDecimal;

public class CartItemResponseDTO {
	
    private Long cartItemId;
    
    private Long productId;
    
    private Long variantId;
    
    private String productName;
    
    private String imageUrl; // From ProductImage
    
    private String size;     // From ProductVariant
    
    private Integer quantity;
    
    private BigDecimal price;        // Current price from ProductVariant
    
    private Integer stockAvailable;
    
    
    
    
    
	public Long getCartItemId() {
		return cartItemId;
	}
	public void setCartItemId(Long cartItemId) {
		this.cartItemId = cartItemId;
	}
	public Long getProductId() {
		return productId;
	}
	public void setProductId(Long productId) {
		this.productId = productId;
	}
	public Long getVariantId() {
		return variantId;
	}
	public void setVariantId(Long variantId) {
		this.variantId = variantId;
	}
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public String getImageUrl() {
		return imageUrl;
	}
	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}
	public String getSize() {
		return size;
	}
	public void setSize(String size) {
		this.size = size;
	}
	public Integer getQuantity() {
		return quantity;
	}
	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public BigDecimal getCurrentPrice() {
		return price;
	}
	public void setPrice(BigDecimal price) {
		this.price = price;
	}
	public Integer getStockAvailable() {
		return stockAvailable;
	}
	public void setStockAvailable(Integer stockAvailable) {
		this.stockAvailable = stockAvailable;
	}

    
}
