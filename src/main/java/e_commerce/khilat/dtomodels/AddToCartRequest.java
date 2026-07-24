package e_commerce.khilat.dtomodels;

import java.util.UUID;

public class AddToCartRequest {
	
	private UUID guestId;
    private Long variantId;
    private Integer quantity;
    
    
    
    
	public UUID getGuestId() {
		return guestId;
	}
	public void setGuestId(UUID guestId) {
		this.guestId = guestId;
	}
	public Long getVariantId() {
		return variantId;
	}
	public void setVariantId(Long variantId) {
		this.variantId = variantId;
	}
	public Integer getQuantity() {
		return quantity;
	}
	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}
    
    
    

}
