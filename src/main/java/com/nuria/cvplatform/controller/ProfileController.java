package com.nuria.cvplatform.controller;

import com.nuria.cvplatform.dto.request.ProfileRequest;
import com.nuria.cvplatform.dto.response.ProfileResponse;
import com.nuria.cvplatform.service.ProfileService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/profile")
@RequiredArgsConstructor
public class ProfileController {

    private final ProfileService profileService;

    @GetMapping
    public ResponseEntity<ProfileResponse> getProfile() {
        return ResponseEntity.ok(profileService.getProfile());
    }

    @PutMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ProfileResponse> updateProfile(
            @Valid @RequestBody ProfileRequest request
    ) {
        return ResponseEntity.ok(profileService.updateProfile(request));
    }
}