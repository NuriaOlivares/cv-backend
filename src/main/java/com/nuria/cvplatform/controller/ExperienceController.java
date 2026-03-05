package com.nuria.cvplatform.controller;

import com.nuria.cvplatform.dto.request.ExperienceRequest;
import com.nuria.cvplatform.dto.response.ExperienceResponse;
import com.nuria.cvplatform.service.ExperienceService;
import com.nuria.cvplatform.service.ProfileService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/experiences")
@RequiredArgsConstructor
public class ExperienceController {

    private final ExperienceService experienceService;
    private final ProfileService profileService;

    @GetMapping
    public ResponseEntity<List<ExperienceResponse>> getAll() {
        Long profileId = profileService.getProfileEntity().getId();
        return ResponseEntity.ok(experienceService.getExperiencesByProfile(profileId));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ExperienceResponse> create(
            @Valid @RequestBody ExperienceRequest request
    ) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(experienceService.create(request));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ExperienceResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody ExperienceRequest request
    ) {
        return ResponseEntity.ok(experienceService.update(id, request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        experienceService.delete(id);
        return ResponseEntity.noContent().build();
    }
}