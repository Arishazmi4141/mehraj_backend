package e_commerce.khilat.dtomodels;

import java.util.UUID;

public class CheckoutRequest {

	private UUID guestId;
	
	private String currency; // "inr"

	private String name;

	private String address;

	private Long phone;
	
    private String email;


	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public UUID getGuestId() {
		return guestId;
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

	public void setGuestId(UUID guestId) {
		this.guestId = guestId;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

}
