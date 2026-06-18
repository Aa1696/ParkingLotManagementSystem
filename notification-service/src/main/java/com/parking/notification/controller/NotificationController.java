package com.parking.notification.controller;

import com.parking.notification.service.EmailService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/notifications")
@CrossOrigin(origins = "*")
public class NotificationController {

    private final EmailService emailService;

    public NotificationController(EmailService emailService) {
        this.emailService = emailService;
    }

    @GetMapping("/recent")
    public ResponseEntity<List<Map<String, String>>> getRecentEmails() {
        return ResponseEntity.ok(emailService.getRecentEmails());
    }
}
