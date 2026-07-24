package e_commerce.khilat.controller;


import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.razorpay.Utils;

import e_commerce.khilat.service.OrderService;

@RestController
@RequestMapping("/api/razorpay/webhook")
public class RazorpayWebhookController {

	@Autowired
    private  OrderService orderService;
    
    @Value("${razorpay.webhook.secret}")
    private String webhookSecret;

    @PostMapping
    public ResponseEntity<String> handleWebhook(@RequestBody String payload,
                                                @RequestHeader("X-Razorpay-Signature") String signature) {
        try {

        	
        	Utils.verifyWebhookSignature(payload, signature, webhookSecret);
        	
            JSONObject json = new JSONObject(payload);
            String event = json.getString("event");
            
            System.out.println("RAZORPAY EVENT = " + event);

            if ("order.paid".equals(event)) {

                JSONObject paymentEntity = json.getJSONObject("payload")
                        .getJSONObject("payment")
                        .getJSONObject("entity");
                
                
                

                String razorpayOrderId = paymentEntity.getString("order_id");
                String paymentId = paymentEntity.getString("id");
                
                if(orderService.isOrderAlreadyCreated(paymentId)){
                    System.out.println("Order already processed for " + razorpayOrderId);
                    return ResponseEntity.ok("Already processed");
                }

                orderService.createOrderAfterPayment(razorpayOrderId, paymentId);
                
                

            }

            else if ("refund.processed".equals(event)) {
            	System.out.println("RAZORPAY EVENT = " + event);
                JSONObject refundEntity = json.getJSONObject("payload")
                        .getJSONObject("refund")
                        .getJSONObject("entity");

                String paymentId = refundEntity.getString("payment_id");

                orderService.updatePaymentStatusToRefunded(paymentId);
            }

            return ResponseEntity.ok("Ok");

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Error");
        }
    }
}