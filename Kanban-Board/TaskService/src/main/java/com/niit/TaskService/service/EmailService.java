package com.niit.TaskService.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender emailSender;

    public void sendTaskAssignmentEmail(String assigneeEmail, String taskName) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(assigneeEmail);
        message.setSubject("Task Assigned: " + taskName);
        message.setText("You have been assigned a new task: " + taskName);
        emailSender.send(message);
    }
}
