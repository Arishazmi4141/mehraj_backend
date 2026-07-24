package e_commerce.khilat.util;

import java.security.SecureRandom;

public class CommonConstant {

	public static final String SUCCESS = "SUCCESS";
	public static final String CANCELLED = "CANCELLED";
	public static final String PENDING = "PENDING";
	public static final String REFUNDED = "REFUNDED";
	public static final String ALPHABET = "23456789ABCDEFGHJKLMNPQRSTUVWXYZ";
	public static final String DISPATCHED = "DISPATCHED";
	public static final String DELIVERED = "DELIVERED";
	public static final String CREATED = "CREATED";

	public static final SecureRandom RANDOM = new SecureRandom();

	public static final String ORDER_PLACED = "Aapka order successfully place ho gaya hai! ✅\n"
	        + "Humne aapka payment receive kar liya hai aur hum jald hi aapka order dispatch karenge.\n\n"
	        + "Thank you for shopping with Khilat!\n"
	        + "Best Regards,\nKhilat Team";

	public static String getDispatchMessage(String guestName, String orderId) {
		String name = (guestName != null) ? guestName : "Customer";

		return "Hi " + name + ",\n\n" + "Aapka order #" + orderId + " dispatch ho gaya hai aur raste mein hai! 📦\n"
				+ "Jald hi aapko ye mil jayega.\n\n" + "Thank you for shopping with Khilat!\n" + "Best Regards,\n"
				+ "Khilat Team";
	}

	public static final String ORDER_DISPATCH_EMAIL_TEMPLATE = "Dear %s,\n\n"
			+ "Your order #%s has been dispatched and is currently in transit. 📦\n"
			+ "You will receive it shortly.\n\n" + "Thank you for shopping with Khilat!\n\n" + "Best Regards,\n"
			+ "Team Khilat";

	public static final String ORDER_CANCELLED_EMAIL_TEMPLATE = "Dear %s,\n\n"
			+ "We are writing to confirm that your order #%s has been successfully cancelled. ❌\n"
			+ "If any payment was processed, the refund will be initiated and should reflect in your account within 5-7 business days.\n\n"
			+ "We’re sorry it didn't work out this time, but we hope to see you again soon!\n\n" + "Best Regards,\n"
			+ "Team Khilat";

	public static final String ORDER_DELIVERED_EMAIL_TEMPLATE = "Dear %s,\n\n"
			+ "Great news! Your order #%s has been delivered. 🏁\n"
			+ "We hope you love your new purchase from Khilat!\n\n"
			+ "If you have any questions or feedback, feel free to reach out to us.\n\n"
			+ "Thank you for being our valued customer!\n\n" + "Best Regards,\n" + "Team Khilat";
}
