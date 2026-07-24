package e_commerce.khilat.dtomodels;

import java.util.UUID;

public class OrderRequest {
	
	private UUID guestId;
	
	private Long amount; // Amount in Rupees
	
    private String currency;
    
    private String receipt;
    
	private String name;

	private String address;

	private Long phone;
	
    private String email;
    
   
    
    
    
    
    
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
	public String getReceipt() {
		return receipt;
	}
	public void setReceipt(String receipt) {
		this.receipt = receipt;
	}
	
	public UUID getGuestId() {
		return guestId;
	}
	public void setGuestId(UUID guestId) {
		this.guestId = guestId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public Long getPhone() {
		return phone;
	}
	public void setPhone(Long phone) {
		this.phone = phone;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
    
    

}
