package e_commerce.khilat.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.management.RuntimeErrorException;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;

import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

//import com.razorpay.RazorpayClient;
import com.razorpay.Refund;
import com.razorpay.RazorpayException;
import com.razorpay.RazorpayClient;

import e_commerce.khilat.entity.Cart;
import e_commerce.khilat.entity.CartItem;
import e_commerce.khilat.entity.Order;
import e_commerce.khilat.entity.OrderItem;
import e_commerce.khilat.entity.Payment;
import e_commerce.khilat.entity.Product;
import e_commerce.khilat.entity.ProductVariant;
import e_commerce.khilat.ourexception.OrderProcessingException;
import e_commerce.khilat.repository.CartItemRepo;
import e_commerce.khilat.repository.CartRepo;
import e_commerce.khilat.repository.OrderItemRepo;
import e_commerce.khilat.repository.OrderRepo;
import e_commerce.khilat.repository.PaymentRepo;
import e_commerce.khilat.repository.ProductRepo;
import e_commerce.khilat.repository.ProductVariantRepo;
import e_commerce.khilat.util.CommonConstant;
import e_commerce.khilat.util.DateUtil;
import e_commerce.khilat.util.EmailHandler;
import jakarta.transaction.Transactional;
import e_commerce.khilat.admin.AdminOrderController;
import e_commerce.khilat.dtomodels.CancelOrderDto;
import e_commerce.khilat.dtomodels.OrderDto;
import e_commerce.khilat.dtomodels.OrderItemDto;
import e_commerce.khilat.dtomodels.OrderSummaryDto;
import e_commerce.khilat.dtomodels.OrderTrackingResponseDto;
import e_commerce.khilat.dtomodels.PaymentDto;
import e_commerce.khilat.dtomodels.ProductSummaryDto;

import org.springframework.data.domain.Pageable;

import org.springframework.mail.SimpleMailMessage; // Iski bhi zaroorat padegi

@Service
public class OrderService {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(AdminOrderController.class);


	// application.properties se username uthane ke liye
//	@Value("${spring.mail.username}")
//	private String fromEmail;

	@Autowired
	private PaymentRepo paymentRepository;

	@Autowired
	private TransactionTemplate transactionTemplate;
	@Autowired
	private CartRepo cartRepository;
	@Autowired
	private CartItemRepo cartItemRepository;
	@Autowired
	private OrderRepo orderRepository;
	@Autowired
	private OrderItemRepo orderItemRepository;
	@Autowired
	private ProductRepo productRepository;
	@Autowired
	private ProductVariantRepo productVariantRepo;

	@Autowired
	private RazorpayClient razorpayClient;

	@Autowired
	private OrderItemRepo orderItemRepo;

	@Autowired
	private EmailHandler emailHandler;

	@CacheEvict(value = { "orders", "orderDetail" }, allEntries = true)
	@Transactional
	public void createOrderAfterPayment(String razorpayOrderId, String paymentId) {
		// 1. Get Payment record from DB to update its status later

		LOGGER.info("Searching for transactionId {}",razorpayOrderId);
		
		Payment payment = paymentRepository.findByTransactionId(razorpayOrderId)
				
				.orElseThrow(() -> new RuntimeException("Payment record not found: "+ razorpayOrderId));

		if ("SUCCESS".equals(payment.getStatus()))
			return;

//		// 2. GET GUEST ID FROM STRIPE (Not from Payment table)
//		String guestIdStr = intent.getMetadata().get("guestId");

//		if (guestIdStr == null) {
//			throw new RuntimeException("Guest ID missing from Stripe Metadata");
//		}

		Order order = orderRepository.findByPaymentId(payment.getId())
				.orElseThrow(() -> new RuntimeException("Order record not found for payment"));

		UUID guestId = order.getGuestId();

		// 3. Find Cart
		Cart cart = cartRepository.findByGuestId(guestId)
				.orElseThrow(() -> new RuntimeException("Cart not found for guest"));

		List<CartItem> cartItems = cartItemRepository.findByCart(cart);

//		Order order = orderRepository.findByPaymentId(payment.getId())
//				.orElseThrow(() -> new RuntimeException("Payment record not found"));

		String trckngKey = order.getTrackingKey();

		order.setCreatedAt(LocalDateTime.now());
		order.setDtOfOps(DateUtil.dateConverterToLong(order.getCreatedAt()));

		order.setStatus(CommonConstant.PENDING);
		order.setUpdatedAt(LocalDateTime.now());
		order.setUpdatedDtOfOps(DateUtil.dateConverterToLong(order.getUpdatedAt()));

		order.setTotalAmount(payment.getAmount());

		order = orderRepository.save(order);

		// 5. Create Order Items & Update Stock (Refactored for Variants)
		for (CartItem cartItem : cartItems) {
			// 1. Get the specific variant from the cart item
			ProductVariant variant = cartItem.getVariant();

			// 2. Decrement stock from the Variant, not the Product
			int updatedStock = variant.getStock() - cartItem.getQuantity();
			if (updatedStock < 0) {
				throw new RuntimeException("Insufficient stock for: " + variant.getProduct().getName() + " ("
						+ variant.getSize() + "/" + ")");
			}
			variant.setStock(updatedStock);
			productVariantRepo.save(variant); //

			// 3. Create OrderItem pointing to the Variant
			OrderItem orderItem = new OrderItem();
			orderItem.setOrder(order);
			orderItem.setVariant(variant); // Changed from setProduct
			orderItem.setQuantity(cartItem.getQuantity());

			// Note: Use variant price or cartItem price.
			// Usually, price * quantity is handled here.
			orderItem.setPrice(cartItem.getPrice().multiply(new BigDecimal(cartItem.getQuantity())));

			orderItemRepository.save(orderItem);
		}

		// 6. Finalize Payment in DB
		payment.setOrder(order); // Now you link them!
		payment.setTransactionId(paymentId);
		payment.setStatus(CommonConstant.SUCCESS);
		paymentRepository.save(payment);

		System.out.println("status of pmt : " + payment.getStatus());

		String guestEmail = order.getEmail();
		String guestName = order.getName();

		
//		emailHandler.sendEmailtoGuest(guestEmail, guestName, trckngKey);
		TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
	        @Override
	        public void afterCommit() {
	            // This ONLY runs if the Database successfully saves everything
	            emailHandler.sendEmailtoGuest(guestEmail, guestName, trckngKey);
	        }
	    });

		// 7. Cleanup
		cartItemRepository.deleteAll(cartItems);
		cartRepository.delete(cart);
	}

	@Cacheable(value = "orderDetail", key = "#orderId")
	public OrderDto getOrderDetail(Long orderId) {

		Order order = orderRepository.findById(orderId).orElseThrow(() -> new RuntimeException("Order not found"));

		OrderDto response = new OrderDto();
		response.setAddress(order.getAddress());
		response.setCreatedAt(order.getCreatedAt());
		response.setEmail(order.getEmail());
		response.setId(order.getId());
		response.setName(order.getName());
		response.setPhone(order.getPhone());
		response.setStatus(order.getStatus());
		response.setTrackingKey(order.getTrackingKey());

		Payment payment = order.getPayment();

		if (payment != null) {
			PaymentDto pmtDto = new PaymentDto();
			pmtDto.setId(payment.getId());
			pmtDto.setAmount(payment.getAmount());
			pmtDto.setCreatedAt(payment.getCreatedAt());
			pmtDto.setPaymentstatus(payment.getStatus());
			response.setPayment(pmtDto);
		}

		List<OrderItem> orderItems = orderItemRepo.findByOrderId(orderId);

		List<OrderItemDto> itemDtos = orderItems.stream().map(item -> {
			OrderItemDto dto = new OrderItemDto();
			ProductVariant variant = item.getVariant();

			dto.setId(item.getId());
			dto.setOrderid(orderId);
			dto.setQuantity(item.getQuantity());
			dto.setPrice(item.getPrice());

			if (variant != null) {
				dto.setSize(variant.getSize());
//				dto.setStockLeft(variant.getStock());

				Product product = variant.getProduct();
				if (product != null) {
					// Only access product methods if product is NOT null
					dto.setProductId(product.getId());
					dto.setProductName(product.getName());

					if (product.getCategory() != null) {
						dto.setCategoryName(product.getCategory().getName());
					}

					if (product.getProductImages() != null && !product.getProductImages().isEmpty()) {
						dto.setImageUrl(product.getProductImages().get(0).getImageUrl());
					}
				} else {
					// Optional: Handle the case where the product is missing
					dto.setProductName("Unknown Product (Deleted)");
				}
			}

			return dto;
		}).collect(Collectors.toList());

		response.setItems(itemDtos);

		return response; // Successfully returns the type OrderDto
	}

	@Cacheable(value = "orders", key = "#status + '-' + #date + '-' + #trckngKey + '-' + #pageable.pageNumber + '-' + #pageable.pageSize")
	public Page<OrderSummaryDto> getOrderSummaries(String status, Long date, String trckngKey, Pageable pageable) {

		// 1. Fetch the data
		Page<Order> ordersPage;

		if (date != null && trckngKey != null && !trckngKey.isEmpty()) {
			// status + date + trackingKey
			ordersPage = orderRepository.findByStatusAndUpdatedDtOfOpsAndTrackingKey(status, date, trckngKey, pageable);
		} else if (trckngKey != null && !trckngKey.isEmpty()) {
			// status + trackingKey
			ordersPage = orderRepository.findByStatusAndTrackingKey(status, trckngKey, pageable);
		} else if (date != null) {
			// status + date
			ordersPage = orderRepository.findByStatusAndUpdatedDtOfOps(status, date, pageable);
		} else {
			// status only
			ordersPage = orderRepository.findByStatus(status, pageable);
		}

		List<OrderSummaryDto> dtoList = new ArrayList<>();

		// 2. Simple for-each loop instead of .map()
		for (Order order : ordersPage) {
			OrderSummaryDto dto = new OrderSummaryDto();
			dto.setOrderId(order.getId());
			dto.setName(order.getName());
			dto.setPhone(order.getPhone());
			dto.setAmount(order.getTotalAmount());
			dto.setOrderStatus(order.getStatus());
			dto.setDtOfOps(order.getDtOfOps());
			dto.setUpdatedDtOfOps(order.getUpdatedDtOfOps());
			dto.setTrckngKey(order.getTrackingKey());

			// Get payment status simply
			paymentRepository.findByOrderId(order.getId()).ifPresent(p -> dto.setPaymentStatus(p.getStatus()));

			dtoList.add(dto);
		}

		// 3. Return as a Page object again
		return new PageImpl<>(dtoList, pageable, ordersPage.getTotalElements());

	}

	@CacheEvict(value = { "orders", "orderDetail" }, allEntries = true)
	public String cancelOrderService(CancelOrderDto request) {

		// 1. First, Update and COMMIT the status to CANCELLED
		Boolean updateSuccess = transactionTemplate.execute(status -> {

			Order order = orderRepository.findByTrackingKey(request.getTrckngKey())
					.orElseThrow(() -> new RuntimeException("Order Id not found"));

			// Validate status
			if (!order.getStatus().equalsIgnoreCase(CommonConstant.PENDING)) {
				throw new OrderProcessingException("Order is " + order.getStatus() + " and cannot be cancelled.");
			}

			order.setStatus(CommonConstant.CANCELLED);
			order.setUpdatedAt(LocalDateTime.now());
			order.setUpdatedDtOfOps(DateUtil.dateConverterToLong(order.getUpdatedAt()));

			orderRepository.save(order);

			return true;
		});

		if (Boolean.FALSE.equals(updateSuccess)) {
			return "Order cannot be cancelled.";
		}

		// 2. NOW that DB is committed as CANCELLED → call Razorpay refund
		try {

			Order order = orderRepository.findByTrackingKey(request.getTrckngKey())
					.orElseThrow(() -> new RuntimeException("Order not found"));

			BigDecimal amount = order.getPayment().getAmount().multiply(BigDecimal.valueOf(100));

			JSONObject refundRequest = new JSONObject();
			refundRequest.put("amount", amount.longValue());

			Refund refund = razorpayClient.payments.refund(order.getPayment().getTransactionId());
			System.out.println("Refund Id: " + refund.get("id"));
			emailHandler.sendCancelEmail(request.getEmail(), request.getName(), request.getTrckngKey());

			return "Your Order Has Been Cancelled. Refund initiated.";

		} catch (Exception e) {

			throw new RuntimeException("Refund failed: " + e.getMessage());
			
		}
	}

	@CacheEvict(value = { "orders", "orderDetail" }, allEntries = true)
	@Transactional
	public void updatePaymentStatusToRefunded(String transactionId) {
		Payment payment = paymentRepository.findByTransactionId(transactionId)
				.orElseThrow(() -> new RuntimeException("Payment not found"));
		
		if(CommonConstant.REFUNDED.equals(payment.getStatus())){
		    return;
		}

		payment.setStatus(CommonConstant.REFUNDED);

		Order order = payment.getOrder();
		if (order != null) {
			// LOGGING FOR PRODUCTION DEBUGGING

			if (order.getStatus().trim().equalsIgnoreCase(CommonConstant.CANCELLED.trim())) {
				order.setStatus(CommonConstant.REFUNDED);
				order.setUpdatedAt(LocalDateTime.now());
				order.setUpdatedDtOfOps(DateUtil.dateConverterToLong(order.getUpdatedAt()));
			}
		}
	}

	@CacheEvict(value = { "orders", "orderDetail" }, allEntries = true)
	@Transactional
	public void markOrderAsDispatched(Long orderId) {
		// 1. Order ko DB se find karein
		Order order = orderRepository.findById(orderId)
				.orElseThrow(() -> new RuntimeException("Order not found with ID: " + orderId));

		// 2. Status update karein
		order.setStatus(CommonConstant.DISPATCHED);
		order.setUpdatedAt(LocalDateTime.now());
		order.setUpdatedDtOfOps(DateUtil.dateConverterToLong(order.getUpdatedAt()));
		orderRepository.save(order);

		// 3. User ko Dispatch ka email bhejein
		emailHandler.sendDispatchEmail(order.getEmail(), order.getName(), order.getTrackingKey());
	}

	@CacheEvict(value = { "orders", "orderDetail" }, allEntries = true)
	@Transactional
	public void markOrderAsDelivered(Long orderId) {

		Order order = orderRepository.findById(orderId)
				.orElseThrow(() -> new RuntimeException("Order not found with ID: " + orderId));

		if (!CommonConstant.DISPATCHED.equals(order.getStatus())) {
			throw new RuntimeException("Order is not in dispatched state. Current status: " + order.getStatus());
		}

		order.setStatus(CommonConstant.DELIVERED);
		order.setUpdatedAt(LocalDateTime.now());
		order.setUpdatedDtOfOps(DateUtil.dateConverterToLong(order.getUpdatedAt()));
		orderRepository.save(order);

		emailHandler.sendDeliveredEmail(order.getEmail(), order.getName(), order.getTrackingKey());
	}

	public OrderTrackingResponseDto getOrderUpdate(String trackingKey) {

		Order order = orderRepository.findByTrackingKey(trackingKey)
				.orElseThrow(() -> new RuntimeException("order not found with key:" + trackingKey));

		OrderTrackingResponseDto response = new OrderTrackingResponseDto();

		response.setStatus(order.getStatus());
		response.setName(order.getName());
		response.setEmail(order.getEmail());

		return response;

	}
	
	public boolean isOrderAlreadyCreated(String razorpayPmtIdea){
	    return paymentRepository.existsByTransactionId(razorpayPmtIdea);
	}

}
