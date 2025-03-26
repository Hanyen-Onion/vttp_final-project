package vttp.batch_b.min_project.server.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import org.springframework.stereotype.Service;

@Service
public class EmailServiceImplementation {
    
    @Autowired
    private JavaMailSender mailSender;

    public void sendEmailReminder(String to, String subject, String text) {
        SimpleMailMessage msg = new SimpleMailMessage();

        msg.setFrom("noreply-payment-receipt@flyeasy.com");
        msg.setTo(to);
        msg.setSubject(subject);
        msg.setText(text);
        mailSender.send(msg);
    }
}
