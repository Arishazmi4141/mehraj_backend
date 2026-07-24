//package e_commerce.khilat.util;



package e_commerce.khilat.util;

import com.resend.*;
import com.resend.services.emails.model.CreateEmailOptions;
import com.resend.services.emails.model.CreateEmailResponse;

import jakarta.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class EmailHandler {

    @Value("${resend.api.key}")
    private String resendApiKey;

    private static final String FROM_EMAIL = "onboarding@resend.dev";   // 👆 Use this until you verify your own domain on Resend
    // After domain verification, replace with: "Khilat Store <no-reply@yourdomain.com>"


    
//    @PostConstruct
//    public void debugEnv() {
//        System.out.println("ENV RESEND_API_KEY: " + System.getenv("RESEND_API_KEY"));
//        System.out.println("PROPERTY resend.api.key: " + resendApiKey);
//    }
    // ─── Order Placed ─────────────────────────────────────────────
    @Async
    public void sendEmailtoGuest(String guestEmail, String guestName, String trckngKey) {
        System.out.println("📨 sendEmailtoGuest called for: " + guestEmail);
        System.out.println("👤 Guest Name: " + guestName);
        System.out.println("🔑 Tracking Key: " + trckngKey);
        System.out.println("🔐 Resend API Key present: " + (resendApiKey != null && !resendApiKey.isEmpty()));

        String displayName = (guestName != null) ? guestName : "Customer";

        String html = "<h2>Hi " + displayName + "! 🎉</h2>"
                + "<p>Your order has been placed successfully.</p>"
                + "<p><strong>Order ID:</strong> " + trckngKey + "</p>"
                + "<p>Aapka order successfully place ho gaya hai! ✅</p>"
                + "<p>Humne aapka payment receive kar liya hai aur hum jald hi aapka order dispatch karenge.</p>"
                + "<br>"
                + "<p>Thank you for shopping with Khilat!</p>"
                + "<p><strong>Best Regards,</strong><br>Khilat Team</p>";

        System.out.println("📧 Preparing to send email to: " + guestEmail);
        System.out.println("📝 Subject: Order Confirmation - Khilat Store 🎉");

        sendEmail(guestEmail, "Order Confirmation - Khilat Store 🎉", html);

        System.out.println("✅ sendEmailtoGuest() completed — check sendEmail() logs below");
    }


    // ─── Order Dispatched ──────────────────────────────────────────
    @Async
    public void sendDispatchEmail(String guestEmail, String guestName, String trckngKey) {
        System.out.println("📨 sendDispatchEmail called for: " + guestEmail);

        String displayName = (guestName != null) ? guestName : "Customer";

        String html = "<h2>Great News, " + displayName + "! 🚚</h2>"
                + "<p>Your order <strong>#" + trckngKey + "</strong> has been dispatched and is currently in transit. 📦</p>"
                + "<p>You will receive it shortly.</p>"
                + "<br>"
                + "<p>Thank you for shopping with Khilat!</p>"
                + "<p><strong>Best Regards,</strong><br>Team Khilat</p>";

        sendEmail(guestEmail, "Your Order #" + trckngKey + " is Dispatched 🚚", html);
    }


    // ─── Order Cancelled ──────────────────────────────────────────
    @Async
    public void sendCancelEmail(String email, String name, String trckngKey) {
        System.out.println("📨 sendCancelEmail called for: " + email);

        String displayName = (name != null) ? name : "Customer";

        String html = "<h2>Hi " + displayName + ",</h2>"
                + "<p>Your cancellation request for order <strong>#" + trckngKey + "</strong> has been successfully processed. ❌</p>"
                + "<p>If any payment was processed, the refund will be initiated and should reflect in your account within <strong>5-7 business days</strong>.</p>"
                + "<br>"
                + "<p>We're sorry it didn't work out this time, but we hope to see you again soon!</p>"
                + "<p><strong>Best Regards,</strong><br>Team Khilat</p>";

        sendEmail(email, "Order #" + trckngKey + " Cancellation Update", html);
    }


    // ─── Order Delivered ──────────────────────────────────────────
    @Async
    public void sendDeliveredEmail(String guestEmail, String guestName, String trckngKey) {
        System.out.println("📨 sendDeliveredEmail called for: " + guestEmail);

        String displayName = (guestName != null) ? guestName : "Customer";

        String html = "<h2>Your Order is Delivered, " + displayName + "! 🎁</h2>"
                + "<p>Great news! Your order <strong>#" + trckngKey + "</strong> has been delivered successfully. 🏁</p>"
                + "<p>We hope you love your new purchase from Khilat!</p>"
                + "<p>If you have any questions or feedback, feel free to reach out to us.</p>"
                + "<br>"
                + "<p>Thank you for being our valued customer! 😊</p>"
                + "<p><strong>Best Regards,</strong><br>Team Khilat</p>";

        sendEmail(guestEmail, "Your Order #" + trckngKey + " is Delivered 🎁", html);
    }


    // ─── Core Send Method (shared by all) ─────────────────────────
    private void sendEmail(String to, String subject, String html) {
        System.out.println("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
        System.out.println("🚀 sendEmail() triggered");
        System.out.println("📬 To: " + to);
        System.out.println("📌 Subject: " + subject);
        System.out.println("🔑 API Key (first 8 chars): " + 
            (resendApiKey != null && resendApiKey.length() > 8 
                ? resendApiKey.substring(0, 8) + "..." 
                : "❌ NULL OR TOO SHORT"));
        try {
            Resend resend = new Resend(resendApiKey);
            System.out.println("✅ Resend client created");

            CreateEmailOptions params = CreateEmailOptions.builder()
                    .from(FROM_EMAIL)
                    .to(to)
                    .subject(subject)
                    .html(html)
                    .build();
            System.out.println("✅ Email options built");

            CreateEmailResponse response = resend.emails().send(params);
            System.out.println("✅ Email sent! Resend ID: " + response.getId());

        } catch (Exception e) {
            System.err.println("❌ sendEmail() FAILED");
            System.err.println("❌ Exception type: " + e.getClass().getName());
            System.err.println("❌ Message: " + e.getMessage());
            e.printStackTrace();
        }
        System.out.println("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
    }
}
