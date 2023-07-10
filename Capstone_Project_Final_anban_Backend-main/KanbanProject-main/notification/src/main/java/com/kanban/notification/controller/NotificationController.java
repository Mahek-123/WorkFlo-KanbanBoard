package com.kanban.notification.controller;

import com.kanban.notification.config.MailSender;
import com.kanban.notification.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/notifications")
public class NotificationController {
    @Autowired
    private NotificationService notificationService;
    private final MailSender mailSender;

    public NotificationController(@Value("${spring.mail.username}") String username,
                          @Value("${spring.mail.password}") String password) {
        this.mailSender = new MailSender(username, password);
    }

    @GetMapping("/{name}")
    public ResponseEntity<?> getNotification(@PathVariable String name) {
        return new ResponseEntity<>(notificationService.getNotification(name), HttpStatus.OK);
    }

    @GetMapping("/allRead/{name}")
    public ResponseEntity<?> markAllAsRead(@PathVariable String name) {
        return new ResponseEntity<>(notificationService.markAllRead(name), HttpStatus.OK);
    }

    @GetMapping("/read/{name}/{message}")
    public ResponseEntity<?> markAsRead(@PathVariable String name, @PathVariable String message) {
        return new ResponseEntity<>(notificationService.markRead(name, message), HttpStatus.OK);
    }

    @PostMapping("/email/{email}")
    public String sendRegistrationEmail(@PathVariable String email, @RequestBody String message) {
        System.out.println(message);
        System.out.println(email);
        String subject = "Welcome to WorkFlo – Unleash Your Productivity with Kanban Boards!";
        String content = "Dear " + email + ",\n\n" +
                message;

        mailSender.sendEmail(email, subject, content);
        return "Email sent!";
    }
}
