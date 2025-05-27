package com.puc.moeda.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendSimpleEmail(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        // TODO: Set the 'from' address, maybe from application.properties
        // message.setFrom("your-email@example.com");
        mailSender.send(message);
    }

    // TODO: Add methods for more complex emails (HTML content, attachments) if needed
}
