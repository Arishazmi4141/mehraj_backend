package e_commerce.khilat.dtomodels;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonInclude;

import e_commerce.khilat.entity.Payment;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;


public class OrderDto {
	
	private Long id;
	
	@Column(name = "guest_id", nullable = false, unique = true)
    private UUID guestId;
	
	private PaymentDto payment; // SAHI: DTO ke andar DTO hi hona chahiye
	
	private String trackingKey;
	
	

	private String email;
    
    private String name;
    
    private String address;
    
    

	private String status;
    
	private Long phone;
    
	private LocalDateTime createdAt;
	
	private List<OrderItemDto> items;
	
	
	
	
	
	
	public String getTrackingKey() {
		return trackingKey;
	}

	public void setTrackingKey(String trackingKey) {
		this.trackingKey = trackingKey;
	}

	public UUID getGuestId() {
		return guestId;
	}

	public void setGuestId(UUID guestId) {
		this.guestId = guestId;
	}
    
    public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	
	
    
    public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
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


	
	public PaymentDto getPayment() {
        return payment;
    }

    public void setPayment(PaymentDto payment) {
        this.payment = payment;
    }

	

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	
	
    public List<OrderItemDto> getItems() {
        return items;
    }

    public void setItems(List<OrderItemDto> items) {
        this.items = items;
    }

}
