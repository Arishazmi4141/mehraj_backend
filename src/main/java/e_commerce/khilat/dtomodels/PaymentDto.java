package e_commerce.khilat.dtomodels;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import e_commerce.khilat.entity.Order;
public class PaymentDto {
	
    private Long id;


    private String Paymentstatus;

    private BigDecimal amount;
    
    private LocalDateTime createdAt;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getPaymentstatus() {
		return Paymentstatus;
	}

	public void setPaymentstatus(String paymentstatus) {
		Paymentstatus = paymentstatus;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}
    
    
    
    

}
