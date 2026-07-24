package e_commerce.khilat.dtomodels;

public class CheckoutResponse {
	
	
//    private String clientSecret;
    
    private String razorpayOrderId;
    
    private String keyId;
    
    private Long amount;
    
//	public String getClientSecret() {
//		return clientSecret;
//	}
//	public void setClientSecret(String clientSecret) {
//		this.clientSecret = clientSecret;
//	}
    
    
    public String getRazorpayOrderId() {
        return razorpayOrderId;
    }
    public void setRazorpayOrderId(String razorpayOrderId) {
        this.razorpayOrderId = razorpayOrderId;
    }
    
    public String getKeyId() {
        return keyId;
    }
    public void setKeyId(String keyId) {
        this.keyId = keyId;
    }
    
    
    
	public Long getAmount() {
		return amount;
	}
	public void setAmount(Long amount) {
		this.amount = amount;
	}
    
	
    
    
}
