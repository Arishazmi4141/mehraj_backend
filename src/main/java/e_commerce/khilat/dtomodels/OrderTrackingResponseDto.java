package e_commerce.khilat.dtomodels;

public class OrderTrackingResponseDto {
	
//    private String trackingKey;
    private String status;
    
    private String name;
    
    private String email;
    
    
    
//	public String getTrackingKey() {
//		return trackingKey;
//	}
//	public void setTrackingKey(String trackingKey) {
//		this.trackingKey = trackingKey;
//	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
    
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name=name;
	}
	
	public String getEmail() {
		return email;
	}
    
	public void setEmail(String email) {
		this.email=email;
	}
    
}
