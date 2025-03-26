package vttp.batch_b.min_project.server.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
    
    @Autowired
    private EmailServiceImplementation emailSvcImpl;

    public void sendPaymentConfirmation() {
        String email = "";
        String username = "";
        String text = """
                Dear %s,\n\n
                Your flight booking is successful! \n\n
                Your flight details is as followed:\n
                \n
                TPE ----> SIN 23 March 2025 5.50pm    Price: $230.50\n\n
                
                Enjoy your trip!\n\n

                Best Regards,\n
                FlyEasy Team\n
                """.formatted(username);
        emailSvcImpl.sendEmailReminder(email, "[FlyEasy] Booking Confirmation", text);
    }

}
