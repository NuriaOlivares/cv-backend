package com.nuria.cvplatform.controller;

import com.nuria.cvplatform.dto.request.ContactRequest;
import com.nuria.cvplatform.service.ContactService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/contact")
@RequiredArgsConstructor
public class ContactController {

    private final ContactService contactService;

    @PostMapping
    public ResponseEntity<Void> contact(@Valid @RequestBody ContactRequest request) {
        contactService.sendContact(request);
        return ResponseEntity.ok().build();
    }
}
