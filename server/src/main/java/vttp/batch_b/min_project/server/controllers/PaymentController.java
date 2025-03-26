package vttp.batch_b.min_project.server.controllers;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;

import jakarta.annotation.PostConstruct;
import vttp.batch_b.min_project.server.models.dtos.CartDTO;
import vttp.batch_b.min_project.server.services.EmailService;
import vttp.batch_b.min_project.server.services.PaymentService;

@RestController
@RequestMapping(path="/api", produces=MediaType.APPLICATION_JSON_VALUE)
public class PaymentController {

    @Value("${stripe.apikey}")
    private String stripeApiKey;
    
    @Autowired
    private PaymentService pSvc;

    @Autowired
    private EmailService eSvc;

    @PostConstruct
    public void init() {
        Stripe.apiKey = stripeApiKey;
    }
    
    @PostMapping("/create-payment")
    public ResponseEntity<Map<String, Object>> createPayment(@RequestBody CartDTO payload) {

        String bookingRef = pSvc.generateBookingRef();
        long totalAmount = pSvc.calculateTotal(payload);
        try {
            PaymentIntent paymentIntent = pSvc.generatePaymentIntent(totalAmount, payload, bookingRef);
            
            // Return payment info to client
            Map<String, Object> response = new HashMap<>();
            response.put("clientSecret", paymentIntent.getClientSecret());
            response.put("paymentId", paymentIntent.getId());
            response.put("bookingReference", bookingRef);
            
            return ResponseEntity.ok(response);
        } catch (StripeException e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

     @PostMapping("/webhook")
    public ResponseEntity<String> handleStripeWebhook(@RequestBody String payload,
                                                    @RequestHeader("Stripe-Signature") String signature) {
        try {
            
            
            return ResponseEntity.ok("Webhook received");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Webhook error");
        }
    }

    @PostMapping("/confirm-booking/{paymentId}")
    public ResponseEntity<Map<String, Object>> confirmBooking(@PathVariable String paymentId) {
        try {
            // Retrieve the payment to verify it was successful
            PaymentIntent paymentIntent = PaymentIntent.retrieve(paymentId);
            
            if ("succeeded".equals(paymentIntent.getStatus())) {
                
                // save flight to repo
                eSvc.sendPaymentConfirmation();
                
                Map<String, Object> response = new HashMap<>();
                response.put("status", "confirmed");
                response.put("bookingReference", paymentIntent.getMetadata().get("bookingRef"));
                //response.put("ticketNumbers", generateTicketNumbers(paymentIntent));
                
                return ResponseEntity.ok(response);
            } else {
                Map<String, Object> response = new HashMap<>();
                response.put("status", "failed");
                response.put("message", "Payment has not been completed");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }
        } catch (StripeException e) {
            Map<String, Object> response = new HashMap<>();
            response.put("status", "error");
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}