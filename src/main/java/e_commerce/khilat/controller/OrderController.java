package e_commerce.khilat.controller;

import java.util.List;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;

import e_commerce.khilat.dtomodels.CancelOrderDto;
import e_commerce.khilat.dtomodels.CheckoutResponse;
import e_commerce.khilat.dtomodels.OrderRequest;
import e_commerce.khilat.dtomodels.OrderTrackingResponseDto;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

import e_commerce.khilat.service.CheckoutService;
import e_commerce.khilat.service.OrderService;


@RestController
@RequestMapping("/api/order")
@CrossOrigin
public class OrderController {
	
	
	@Autowired
	private OrderService orderService;
	
	@Autowired
	private CheckoutService checkoutService;
	
	@Autowired
	private RazorpayClient razorpayClient;
	
	
	@PostMapping(value = "/cancel-order")
	public ResponseEntity<String> cancelOrder(@RequestBody CancelOrderDto request) {
	    try {
	        String message = orderService.cancelOrderService(request);
	        
	        return ResponseEntity.ok(message);
	        
	    } catch (Exception e) {
	        // e.getMessage() will contain "Order is dispatched" or whatever you threw
	        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
	                             .body(e.getMessage());
	    }
	}
	
	@GetMapping("/track/{trackingKey}")
	public ResponseEntity<?> getOrderUpdate(@PathVariable String  trackingKey){
		
		try {
			OrderTrackingResponseDto response = orderService.getOrderUpdate(trackingKey); 
			return ResponseEntity.ok(response);
			
		}catch(RuntimeException e) {
			
			return ResponseEntity.status(400).body(e.getMessage());
		}
		
	}
	
//	@PostMapping("/create-guestorder")
//	public ResponseEntity<CheckoutResponse> createOrder(@RequestBody OrderRequest orderRequest) throws Exception {
//        try {
//
//            // 1. Prepare the Order Request
//            JSONObject orderOptions = new JSONObject();
//            orderOptions.put("amount", orderRequest.getAmount() * 100); // Convert to Paise
//            orderOptions.put("currency", "INR");
//            orderOptions.put("receipt", "txn_" + System.currentTimeMillis());
//
//            // 3. Create Order in Razorpay System
//            Order order = razorpayClient.orders.create(orderOptions);
//            
//            CheckoutResponse chekoutResponse = checkoutService.createPaymentIntent(orderRequest);
//            
//            
//            
//
//            // 4. (Optional) Save the order_id in your DB as 'PENDING' here
//            // String rzpOrderId = order.get("id");
//
//            // 5. Return the Order details to Frontend
//            return ResponseEntity.ok(chekoutResponse);
//            
//        } catch (RazorpayException e) {
//        	
//        	return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();        }
//    }
	
	@PostMapping("/create-guestorder")
	public ResponseEntity<CheckoutResponse> createOrder(@RequestBody OrderRequest orderRequest) {
	    try {
	        // We removed the redundant Razorpay order creation here.
	        // The service now handles all logic: Razorpay API, DB Order, and DB Payment.
	        CheckoutResponse checkoutResponse = checkoutService.createPaymentIntent(orderRequest);
	        
	        return ResponseEntity.ok(checkoutResponse);
	    } catch (Exception e) {
	        // Log the error for debugging
	        e.printStackTrace(); 
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
	    }
	}

}
