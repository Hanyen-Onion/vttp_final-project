package vttp.batch_b.min_project.server.services;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;

import vttp.batch_b.min_project.server.models.FlightOffer;
import vttp.batch_b.min_project.server.models.dtos.CartDTO;
import vttp.batch_b.min_project.server.repository.PaymentRepository;

@Service
public class PaymentService {
    @Autowired
    private PaymentRepository pRepo;

    public long calculateTotal(CartDTO cartObj) {
        
        return Math.round(
            cartObj.flights().stream()
                .mapToDouble(FlightOffer::getPrice)
                .sum()
        );
    }

    public PaymentIntent generatePaymentIntent(long totalAmount, CartDTO cartObj, String bookingRef) throws StripeException {

        Map<String, String> metadata = new HashMap<>();
        cartObj.flights().forEach(f -> {
            metadata.put("departure", f.getDepCode());
            metadata.put("arrival", f.getArrCode());
            metadata.put("bookingRef", bookingRef);
        });
        String currency = cartObj.flights().get(0).getCurrency().toLowerCase();

        PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()
                .setAmount(totalAmount)
                .setCurrency(currency)
                .setDescription("Airline Ticket Purchase")
                .putAllMetadata(metadata)
                .setReceiptEmail(cartObj.email())
                .setStatementDescriptor("AIRLINE*TICKETS")
                .setCaptureMethod(PaymentIntentCreateParams.CaptureMethod.AUTOMATIC)
                .setAutomaticPaymentMethods(
                    PaymentIntentCreateParams.AutomaticPaymentMethods.builder()
                        .setEnabled(true)
                        .build()
                )
                .build();
        return PaymentIntent.create(params);
    }

    public String generateBookingRef() {
        return "AIR" + System.currentTimeMillis() % 1000000;
    }


}
