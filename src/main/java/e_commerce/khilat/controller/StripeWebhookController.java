//package e_commerce.khilat.controller;
//
//import com.stripe.Stripe;
//import com.stripe.model.Event;
//import com.stripe.model.EventDataObjectDeserializer;
//import com.stripe.model.PaymentIntent;
//import com.stripe.model.StripeObject;
//import com.stripe.net.Webhook;
//import e_commerce.khilat.service.OrderService;
//import jakarta.annotation.PostConstruct;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//@RestController
//@RequestMapping("/api/stripe/webhook")
//public class StripeWebhookController {
//
//	private final OrderService orderService;
//
//	@Value("${stripe.webhook.secret}")
//	private String webhookSecret;
//
//	@Value("${stripe.secret.key}") // Matches your properties file key
//	private String stripeApiKey;
//
//	public StripeWebhookController(OrderService orderService) {
//		this.orderService = orderService;
//	}
//
//	@PostConstruct
//	public void setupStripe() {
//		Stripe.apiKey = stripeApiKey;
//		// Note: version pinning is internal in SDK 24.10.0
//	}
//
//	@PostMapping
//	public ResponseEntity<String> handleWebhook(@RequestBody String payload,
//			@RequestHeader("Stripe-Signature") String sigHeader) {
//
//		Event event;
//
//		try {
//			event = Webhook.constructEvent(payload, sigHeader, webhookSecret);
//		} catch (Exception e) {
//			return ResponseEntity.badRequest().body("Invalid signature");
//		}
//
//		// Inside StripeWebhookController.java
//
//		// Check for any refund-related event
//		if ("charge.refunded".equals(event.getType()) || "charge.refund.updated".equals(event.getType())
//				|| "refund.created".equals(event.getType())) {
//
//			EventDataObjectDeserializer dataObjectDeserializer = event.getDataObjectDeserializer();
//			String paymentIntentId = null;
//
//			// Use your existing fallback logic to get the object
//			StripeObject stripeObject = dataObjectDeserializer.getObject().orElseGet(() -> {
//				try {
//					return dataObjectDeserializer.deserializeUnsafe();
//				} catch (Exception e) {
//					return null;
//				}
//			});
//
//			if (stripeObject instanceof com.stripe.model.Charge charge) {
//				paymentIntentId = charge.getPaymentIntent();
//			} else if (stripeObject instanceof com.stripe.model.Refund refund) {
//				paymentIntentId = refund.getPaymentIntent();
//			}
//
//			if (paymentIntentId != null) {
//				orderService.updatePaymentStatusToRefunded(paymentIntentId);
//				return ResponseEntity.ok("Refund Processed");
//			}
//		}
//
//		if ("payment_intent.succeeded".equals(event.getType())) {
//
//			EventDataObjectDeserializer dataObjectDeserializer = event.getDataObjectDeserializer();
//			PaymentIntent intent = null;
//
//			// 1. Try safe deserialization (works if versions match)
//			if (dataObjectDeserializer.getObject().isPresent()) {
//				intent = (PaymentIntent) dataObjectDeserializer.getObject().get();
//			}
//			// 2. PERMANENT FALLBACK: If versions mismatch (like your 2026 CLI vs 2024 SDK)
//			else {
//				try {
//					// deserializeUnsafe() forces mapping even if the API versions differ
//					StripeObject stripeObject = dataObjectDeserializer.deserializeUnsafe();
//					if (stripeObject instanceof PaymentIntent) {
//						intent = (PaymentIntent) stripeObject;
//					}
//				} catch (Exception e) {
//					System.err.println("Fallback deserialization failed: " + e.getMessage());
//				}
//			}
//
//			if (intent != null) {
//				try {
//					orderService.createOrderAfterPayment(intent);
//					return ResponseEntity.ok("Order Created");
//				} catch (Exception e) {
//					e.printStackTrace();
//					// Return 500 so Stripe retries the webhook
//					return ResponseEntity.internalServerError().body("DB Update Failed");
//				}
//			} else {
//				return ResponseEntity.status(422).body("Could not parse PaymentIntent");
//			}
//		}
//
//		return ResponseEntity.ok("Received");
//	}
//}