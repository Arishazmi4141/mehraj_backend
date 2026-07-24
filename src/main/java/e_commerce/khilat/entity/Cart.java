package e_commerce.khilat.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "carts")
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "guest_id", nullable = false, unique = true)
    private UUID guestId;
    
    

	@Column(name = "created_at")
    private LocalDateTime createdAt;
    
    public UUID getGuestId() {
		return guestId;
	}

	public void setGuestId(UUID guestId) {
		this.guestId = guestId;
	}


	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}


	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	@Override
	public String toString() {
		return "Cart [id=" + id + ", createdAt=" + createdAt + "]";
	}
    
    
}