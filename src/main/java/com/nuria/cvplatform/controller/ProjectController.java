package com.nuria.cvplatform.controller;

import com.nuria.cvplatform.dto.request.ProjectRequest;
import com.nuria.cvplatform.dto.response.ProjectResponse;
import com.nuria.cvplatform.service.ProfileService;
import com.nuria.cvplatform.service.ProjectService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/projects")
@RequiredArgsConstructor
public class ProjectController {

    private final ProjectService projectService;
    private final ProfileService profileService;

    @GetMapping
    public ResponseEntity<List<ProjectResponse>> getAll() {
        Long profileId = profileService.getProfileEntity().getId();
        return ResponseEntity.ok(projectService.getProjectsByProfile(profileId));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ProjectResponse> create(
            @Valid @RequestBody ProjectRequest request
    ) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(projectService.create(request));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ProjectResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody ProjectRequest request
    ) {
        return ResponseEntity.ok(projectService.update(id, request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        projectService.delete(id);
        return ResponseEntity.noContent().build();
    }
}