package com.nuria.cvplatform.controller;

import com.nuria.cvplatform.dto.request.CertificationRequest;
import com.nuria.cvplatform.dto.response.CertificationResponse;
import com.nuria.cvplatform.service.CertificationService;
import com.nuria.cvplatform.service.ProfileService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/certifications")
@RequiredArgsConstructor
public class CertificationController {

    private final CertificationService certificationService;
    private final ProfileService profileService;

    @GetMapping
    public ResponseEntity<List<CertificationResponse>> getAll() {
        Long profileId = profileService.getProfileEntity().getId();
        return ResponseEntity.ok(certificationService.getCertificationsByProfile(profileId));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CertificationResponse> create(
            @Valid @RequestBody CertificationRequest request
    ) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(certificationService.create(request));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CertificationResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody CertificationRequest request
    ) {
        return ResponseEntity.ok(certificationService.update(id, request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        certificationService.delete(id);
        return ResponseEntity.noContent().build();
    }
}