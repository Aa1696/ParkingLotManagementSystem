package com.parking.notification.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

@Service
public class EmailService {

    private static final Logger log = LoggerFactory.getLogger(EmailService.class);

    // In-memory store for recent emails to show on UI
    private final List<Map<String, String>> recentEmails = new CopyOnWriteArrayList<>();

    @Autowired(required = false)
    private JavaMailSender javaMailSender;

    public void sendEmail(String to, String subject, String text) {
        // Always store email so UI can retrieve it!
        recentEmails.add(0, Map.of(
                "to", to,
                "subject", subject,
                "text", text,
                "timestamp", LocalDateTime.now().toString()
        ));
        // Keep only last 10
        if (recentEmails.size() > 10) {
            recentEmails.remove(10);
        }

        if (javaMailSender == null) {
            log.warn("JavaMailSender is not configured. Simulating email to: {}. Subject: {}", to, subject);
            log.info("Email Body: \n{}", text);
            return;
        }

        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(to);
            message.setSubject(subject);
            message.setText(text);
            javaMailSender.send(message);
            log.info("Successfully sent email to: {}", to);
        } catch (Exception e) {
            log.error("Failed to send email to: {}, simulating success for UI. Error: {}", to, e.getMessage());
        }
    }

    public List<Map<String, String>> getRecentEmails() {
        return Collections.unmodifiableList(recentEmails);
    }
}
