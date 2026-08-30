package e_commerce.khilat.dtomodels;

import java.time.LocalDateTime;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public class ConsultationRequestDto {

    @NotBlank(message = "Name is required")
    private String name;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;

    @NotBlank(message = "Phone number is required")
    @Pattern(regexp = "^[6-9]\\d{9}$", message = "Enter a valid 10-digit Indian phone number")
    private String phoneNumber;

    private String message;

    // Kam se kam ek slot mandatory hai — ye check ServiceImpl mein hoga
    private LocalDateTime preferredSlot1;
    private LocalDateTime preferredSlot2;
    private LocalDateTime preferredSlot3;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LocalDateTime getPreferredSlot1() {
        return preferredSlot1;
    }

    public void setPreferredSlot1(LocalDateTime preferredSlot1) {
        this.preferredSlot1 = preferredSlot1;
    }

    public LocalDateTime getPreferredSlot2() {
        return preferredSlot2;
    }

    public void setPreferredSlot2(LocalDateTime preferredSlot2) {
        this.preferredSlot2 = preferredSlot2;
    }

    public LocalDateTime getPreferredSlot3() {
        return preferredSlot3;
    }

    public void setPreferredSlot3(LocalDateTime preferredSlot3) {
        this.preferredSlot3 = preferredSlot3;
    }
}