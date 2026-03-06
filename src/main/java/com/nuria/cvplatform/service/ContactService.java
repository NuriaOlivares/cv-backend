package com.nuria.cvplatform.service;

import com.nuria.cvplatform.dto.request.ContactRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ContactService {

    private final JavaMailSender mailSender;

    @Value("${spring.mail.emailTo}")
    private String myEmail;

    public void sendContact(ContactRequest request) {
        sendNotificationToMe(request);
        sendConfirmationToRecruiter(request);
        log.info("Contact email sent from {}", request.getEmail());
    }

    private void sendNotificationToMe (ContactRequest request) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(myEmail);
        message.setSubject("CV Platform — New message from " + request.getName());
        message.setText("""
                New contact from your CV platform:
                
                Name: %s
                Email: %s
                
                Message:
                %s
                """.formatted(request.getName(), request.getEmail(), request.getMessage())
        );
        mailSender.send(message);
    }

    private void sendConfirmationToRecruiter (ContactRequest request) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(request.getEmail());
        message.setSubject("Thanks for reaching out!");
        message.setText("""
                Hi %s,
                
                Thanks for getting in touch! I've received your message and will get back to you as soon as possible.
                
                Best regards,
                Nuria Olivares
                Senior Software Engineer
                """.formatted(request.getName()));
        mailSender.send(message);
    }
}
