package e_commerce.khilat.dtomodels;

import java.util.UUID;

public class PaymentRequest {
    private Long amount;   // in paise (₹500 = 50000)
    private String currency;// "inr"

    private String guestId;
    
    
	public String getGuestId() {
		return guestId;
	}

	public void setGuestId(String guestId) {
		this.guestId = guestId;
	}

	public Long getAmount() {
		return amount;
	}
	
	public void setAmount(Long amount) {
		this.amount = amount;
	}
	
	public String getCurrency() {
		return currency;
	}
	
	public void setCurrency(String currency) {
		this.currency = currency;
	}
    
    
    
}
