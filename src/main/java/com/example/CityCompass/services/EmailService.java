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



}
