package com.nuria.cvplatform.controller;

import com.nuria.cvplatform.dto.request.LanguageRequest;
import com.nuria.cvplatform.dto.response.LanguageResponse;
import com.nuria.cvplatform.service.LanguageService;
import com.nuria.cvplatform.service.ProfileService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/languages")
@RequiredArgsConstructor
public class LanguageController {

    private final LanguageService languageService;
    private final ProfileService profileService;

    @GetMapping
    public ResponseEntity<List<LanguageResponse>> getAll() {
        Long profileId = profileService.getProfileEntity().getId();
        return ResponseEntity.ok(languageService.getLanguagesByProfile(profileId));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<LanguageResponse> create(
            @Valid @RequestBody LanguageRequest request
    ) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(languageService.create(request));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<LanguageResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody LanguageRequest request
    ) {
        return ResponseEntity.ok(languageService.update(id, request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        languageService.delete(id);
        return ResponseEntity.noContent().build();
    }
}