package com.nuria.cvplatform.service;

import com.nuria.cvplatform.dto.request.ContactRequest;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;

@Slf4j
@Service
@RequiredArgsConstructor
public class ContactService {

    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String sender;

    @Value("${spring.mail.emailTo}")
    private String myEmail;

    public void sendContact(ContactRequest request) {
        sendNotificationToMe(request);
        try {
            sendConfirmationToRecruiter(request);
        } catch (Exception e) {
            log.warn("Failed to send confirmation to recruiter {}: {}", request.getEmail(), e.getMessage());
        }
        log.info("Contact email sent from {}", request.getEmail());
    }

    private MimeMessage makeMimeMessage(String displayName) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            mimeMessage.setFrom(new InternetAddress(sender, displayName));
            return mimeMessage;
        } catch (Exception e) {
            throw new RuntimeException("Failed to create email", e);
        }
    }

    private void sendNotificationToMe(ContactRequest request) {
        try {
            MimeMessage message = makeMimeMessage("Nuria Olivares");
            MimeMessageHelper helper = new MimeMessageHelper(message, StandardCharsets.UTF_8.name());
            helper.setTo(myEmail);
            helper.setSubject("CV Platform — New message from " + request.getName());
            helper.setText("""
                New contact from your CV platform:
                
                Name: %s
                Email: %s
                
                Message:
                %s
                """.formatted(request.getName(), request.getEmail(), request.getMessage()));
            mailSender.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException("Failed to send notification email", e);
        }
    }

    private void sendConfirmationToRecruiter(ContactRequest request) {
        try {
            MimeMessage message = makeMimeMessage("Nuria Olivares");
            MimeMessageHelper helper = new MimeMessageHelper(message, StandardCharsets.UTF_8.name());
            helper.setTo(request.getEmail());
            helper.setSubject("Thanks for reaching out!");
            helper.setText("""
                Hi %s,
                
                Thanks for getting in touch! I've received your message and will get back to you as soon as possible.
                
                Best regards,
                Nuria Olivares
                Senior Software Engineer
                """.formatted(request.getName()));
            mailSender.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException("Failed to send confirmation email", e);
        }
    }
}
