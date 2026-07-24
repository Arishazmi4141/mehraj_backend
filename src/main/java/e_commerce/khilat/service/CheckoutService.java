package e_commerce.khilat.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;

import e_commerce.khilat.controller.ProductController;

//import com.stripe.exception.StripeException;
//import com.stripe.model.PaymentIntent;

import e_commerce.khilat.dtomodels.CheckoutRequest;
import e_commerce.khilat.dtomodels.CheckoutResponse;
import e_commerce.khilat.dtomodels.OrderDto;
import e_commerce.khilat.dtomodels.OrderRequest;
import e_commerce.khilat.entity.Cart;
import e_commerce.khilat.entity.CartItem;
import e_commerce.khilat.entity.Order;
import e_commerce.khilat.entity.OrderItem;
import e_commerce.khilat.entity.Payment;
import e_commerce.khilat.repository.CartItemRepo;
import e_commerce.khilat.repository.CartRepo;
import e_commerce.khilat.repository.OrderItemRepo;
import e_commerce.khilat.repository.OrderRepo;
import e_commerce.khilat.repository.PaymentRepo;
import e_commerce.khilat.util.CommonConstant;
import e_commerce.khilat.util.Utility;
import jakarta.transaction.Transactional;

@Service
public class CheckoutService {
	
	private static final Logger LOGGER =
            LoggerFactory.getLogger(CheckoutService.class);
	
	@Value("${razorpay.key.id}")
    private String keyId;

    @Value("${razorpay.key.secret}")
    private String keySecret;
	
	

    private final CartRepo cartRepo;
    
    private final CartItemRepo cartItemRepo;
    
    private final PaymentRepo paymentRepo;
    
//    private final StripePaymentService stripePaymentService;
    
    private final OrderRepo orderRepo;
    
    private final OrderItemRepo orderItemRepo;

    @Autowired							
    private RazorpayPaymentService razorpayPaymentService;
    
    public CheckoutService(
            CartRepo cartRepo,
            CartItemRepo cartItemRepo,
            PaymentRepo paymentRepo,
            OrderRepo orderRepo,
            OrderItemRepo orderItemRepo) {

        this.cartRepo = cartRepo;
        this.cartItemRepo = cartItemRepo;
        this.paymentRepo = paymentRepo;
//        this.stripePaymentService = stripePaymentService;
        this.orderRepo =  orderRepo;
        this.orderItemRepo = orderItemRepo;
        
    }
    
    
    
    @CacheEvict(value = {"orders", "payments"}, allEntries = true)
    @Transactional
    public CheckoutResponse createPaymentIntent(OrderRequest request) throws Exception {
    	
    	System.out.println("Create payment service tart");


        // 1️⃣ Fetch cart
        Cart cart = cartRepo.findByGuestId(request.getGuestId())
                .orElseThrow(() -> new RuntimeException("Cart not found"));

        // 2️⃣ Fetch cart items
        List<CartItem> cartItems = cartItemRepo.findByCart(cart);

        if (cartItems.isEmpty()) {
            throw new RuntimeException("Cart is empty");
        }

        // 3️⃣ Calculate total amount (₹ → paise)
        BigDecimal totalAmount = BigDecimal.ZERO;

        for (CartItem item : cartItems) {
            totalAmount = totalAmount.add(
                    item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity()))
            );
        }

        long amountInPaise = totalAmount.multiply(BigDecimal.valueOf(100)).longValue();
        

        Long amount = Long.valueOf(amountInPaise);
        String currency = request.getCurrency();
        String guestId = (request.getGuestId() != null) ? request.getGuestId().toString() : "anonymous";
        
        LOGGER.debug("""
        	    === RAZORPAY DEBUG ===
        	    Key ID      : [{}]
        	    Key Secret  : [{}]
        	    Amount      : {}
        	    Currency    : {}
        	    Guest ID    : {}
        	    ======================\
        	    """, 
        	    keyId, 
        	    (keySecret != null ? keySecret.substring(0, 4) + "****" : "NULL"), 
        	    amountInPaise, 
        	    currency, 
        	    guestId
        	);
        
        com.razorpay.Order razorpayOrder = razorpayPaymentService.createRazorpayOrder(
                amountInPaise, 
                request.getCurrency(), 
                request.getGuestId().toString()
        );
        
        
        Order order = new Order();
        order.setGuestId(request.getGuestId());
        order.setAddress(request.getAddress()); 
        order.setEmail(request.getEmail());
        order.setName(request.getName());
        order.setPhone(request.getPhone());
        order.setTrackingKey(Utility.generateTrackingKey());
        order.setStatus(CommonConstant.CREATED);
        
        Order savedOrder = orderRepo.save(order);
        
        for (CartItem cartItem : cartItems) {
            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(savedOrder);
            orderItem.setVariant(cartItem.getVariant());
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setPrice(cartItem.getPrice());
            orderItemRepo.save(orderItem); // Ensure you have orderItemRepo injected
        }
        
        Payment payment = new Payment();
        payment.setGateway("RAZORPAY");
        payment.setTransactionId(razorpayOrder.get("id")); 
        payment.setStatus(CommonConstant.CREATED);
        payment.setAmount(totalAmount);
        payment.setCreatedAt(LocalDateTime.now());
        
        payment.setOrder(savedOrder);
        
        Payment savedPayment = paymentRepo.save(payment);
        
        savedOrder.setPayment(savedPayment);
        
        orderRepo.save(savedOrder);
        
        CheckoutResponse response = new CheckoutResponse();
        response.setRazorpayOrderId(razorpayOrder.get("id").toString());
        response.setAmount(amountInPaise);
        response.setKeyId(this.keyId); // Frontend needs this to open the UI
        return response;
    }
    
    
    
}
