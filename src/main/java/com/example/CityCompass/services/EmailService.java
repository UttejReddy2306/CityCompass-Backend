package com.example.CityCompass.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender javaMailSender;

    public void sendBookingConfirmation(String toEmail, String userName, String serviceName) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject("New Service Booking");
        message.setText("User " + userName + " has booked your service: " + serviceName + ".");
        javaMailSender.send(message);
    }

    public void sendProviderResponse(String toEmail, String providerName, String serviceName, boolean isAccepted) {
        String status = isAccepted ? "accepted" : "rejected";
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject("Service Provider Response");
        message.setText("The service provider " + providerName + " has " + status + " your service booking for: " + serviceName + ".");
        javaMailSender.send(message);
    }


    public void sendForgotPasswordResponse(String userEmail, String resetLink) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(userEmail);
        message.setSubject("Password Reset Request");

        String emailContent = "You're receiving this e-mail because you or someone else has requested a password reset "
                + "for your user account at City Compass.\n\n"
                + "Click the link below to reset your password:\n"
                + resetLink + "\n\n"
                + "If you did not request a password reset, you can safely ignore this email.";

        message.setText(emailContent);
        javaMailSender.send(message);

    }
}
