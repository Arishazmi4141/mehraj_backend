//package e_commerce.khilat.service;
//
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//import com.stripe.exception.StripeException;
//import com.stripe.model.PaymentIntent;
//
//import org.springframework.stereotype.Service;
//
//@Service
//public class StripePaymentService {
//
//	public PaymentIntent createPaymentIntent(Long amount, String currency, String guestId) throws StripeException {
//	    Map<String, Object> params = new HashMap<>();
//	    params.put("amount", amount);
//	    params.put("currency", currency);
//	    params.put("payment_method_types", List.of("card"));
//
//	    // Attach the guestId to Stripe's metadata
//	    Map<String, String> metadata = new HashMap<>();
//	    metadata.put("guestId", guestId); 
//	    params.put("metadata", metadata);
//
//	    return PaymentIntent.create(params);
//	}
//}
