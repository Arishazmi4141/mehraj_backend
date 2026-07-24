package e_commerce.khilat.service;


import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;

@Service
public class RazorpayPaymentService {
	
	@Autowired
	private RazorpayClient razorpayClient;

    public com.razorpay.Order createRazorpayOrder(Long amount, String currency, String guestId) throws RazorpayException {
        JSONObject orderRequest = new JSONObject();
        orderRequest.put("amount", amount); // Amount in paise
        orderRequest.put("currency", currency);
        orderRequest.put("receipt", "receipt_" + System.currentTimeMillis());
        
        // Metadata in Razorpay is passed via 'notes'
        JSONObject notes = new JSONObject();
        notes.put("guestId", guestId);
        orderRequest.put("notes", notes);

        return razorpayClient.orders.create(orderRequest);
    }
}