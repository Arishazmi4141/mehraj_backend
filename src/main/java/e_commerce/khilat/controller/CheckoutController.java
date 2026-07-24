package e_commerce.khilat.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import e_commerce.khilat.dtomodels.CheckoutRequest;
import e_commerce.khilat.dtomodels.CheckoutResponse;
import e_commerce.khilat.service.CheckoutService;

@RestController
@RequestMapping("/api/checkout")
@CrossOrigin(origins = "http://localhost:4200")
public class CheckoutController {

    private final CheckoutService checkoutService;

    public CheckoutController(CheckoutService checkoutService) {
        this.checkoutService = checkoutService;
    }
    
    

//    @PostMapping("/create-payment-intent")
//    public ResponseEntity<?> checkout(@RequestBody CheckoutRequest request) {
//        try {
//            CheckoutResponse response = checkoutService.createPaymentIntent(request);
//            return ResponseEntity.ok(response);
//        } catch (Exception e) {
//            return ResponseEntity.badRequest().body(e.getMessage());
//        }
//    }
    
    
}
