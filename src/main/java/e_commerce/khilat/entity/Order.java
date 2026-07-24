package e_commerce.khilat.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "orders")
public class Order {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "guest_id", nullable = false)
	private UUID guestId;

	private String email;

	private String name;

	private String address;

	private Long phone;

	@Column(name = "tracking_key", nullable = false, unique = true)
	private String trackingKey;

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "payment_id", referencedColumnName = "id")
	private Payment payment;

	private String status;

	@Column(name = "total_amount")
	private BigDecimal totalAmount;

	@Column(name = "dt_of_ops")
	private Long dtOfOps;

	@Column(name = "created_at")
	private LocalDateTime createdAt;
	
	
	@Column(name = "updated_dt_of_ops")
	private Long updatedDtOfOps;

	@Column(name = "updated_at")
	private LocalDateTime updatedAt;
	
	
	
	

	


	public Long getDtOfOps() {
		return dtOfOps;
	}

	public void setDtOfOps(Long dtOfOps) {
		this.dtOfOps = dtOfOps;
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
	
	public String getTrackingKey() {
		return trackingKey;
	}

	public void setTrackingKey(String trackingKey) {
		this.trackingKey = trackingKey;
	}

	public Long getPhone() {
		return phone;
	}

	public void setPhone(Long phone) {
		this.phone = phone;
	}

	public UUID getGuestId() {
		return guestId;
	}

	public void setGuestId(UUID guestId) {
		this.guestId = guestId;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public BigDecimal getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(BigDecimal totalAmount) {
		this.totalAmount = totalAmount;
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

	public Payment getPayment() {
		return payment;
	}

	public void setPayment(Payment payment) {
		this.payment = payment;
	}
	
	public Long getUpdatedDtOfOps() {
		return updatedDtOfOps;
	}

	public void setUpdatedDtOfOps(Long updatedDtOfOps) {
		this.updatedDtOfOps = updatedDtOfOps;
	}

	public LocalDateTime getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(LocalDateTime updatedAt) {
		this.updatedAt = updatedAt;
	}

	@Override
	public String toString() {
		return "Order [id=" + id + ", guestId=" + guestId + ", email=" + email + ", name=" + name + ", address="
				+ address + ", phone=" + phone + ", trackingKey=" + trackingKey + ", payment=" + payment + ", status="
				+ status + ", totalAmount=" + totalAmount + ", dtOfOps=" + dtOfOps + ", createdAt=" + createdAt
				+ ", updatedDtOfOps=" + updatedDtOfOps + ", updatedAt=" + updatedAt + "]";
	}


}