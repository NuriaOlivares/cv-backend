package com.nuria.cvplatform.controller;

import com.nuria.cvplatform.dto.request.EducationRequest;
import com.nuria.cvplatform.dto.response.EducationResponse;
import com.nuria.cvplatform.service.EducationService;
import com.nuria.cvplatform.service.ProfileService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/education")
@RequiredArgsConstructor
public class EducationController {

    private final EducationService educationService;
    private final ProfileService profileService;

    @GetMapping
    public ResponseEntity<List<EducationResponse>> getAll() {
        Long profileId = profileService.getProfileEntity().getId();
        return ResponseEntity.ok(educationService.getEducationByProfile(profileId));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<EducationResponse> create(
            @Valid @RequestBody EducationRequest request
    ) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(educationService.create(request));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<EducationResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody EducationRequest request
    ) {
        return ResponseEntity.ok(educationService.update(id, request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        educationService.delete(id);
        return ResponseEntity.noContent().build();
    }
}