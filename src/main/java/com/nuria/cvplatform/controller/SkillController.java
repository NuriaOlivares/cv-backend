package com.nuria.cvplatform.controller;

import com.nuria.cvplatform.dto.request.SkillRequest;
import com.nuria.cvplatform.dto.response.SkillResponse;
import com.nuria.cvplatform.service.ProfileService;
import com.nuria.cvplatform.service.SkillService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/skills")
@RequiredArgsConstructor
public class SkillController {

    private final SkillService skillService;
    private final ProfileService profileService;

    @GetMapping
    public ResponseEntity<List<SkillResponse>> getAll() {
        Long profileId = profileService.getProfileEntity().getId();
        return ResponseEntity.ok(skillService.getSkillsByProfile(profileId));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<SkillResponse> create(
            @Valid @RequestBody SkillRequest request
    ) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(skillService.create(request));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<SkillResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody SkillRequest request
    ) {
        return ResponseEntity.ok(skillService.update(id, request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        skillService.delete(id);
        return ResponseEntity.noContent().build();
    }
}