package com.niit.user_service.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;


@Service
public class EmailService {

    @Autowired
    private JavaMailSender emailSender;

    public void sendRegistrationEmail(String email) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("Welcome to BoardifyNow!");
        message.setText("Dear User,\n\nWelcome to BoardifyNow! We're thrilled to have you as a member of our community.\n\nBest regards,\nThe BoardifyNow Team");
        emailSender.send(message);
    }
}
