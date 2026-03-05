package com.nuria.cvplatform.service;

import com.nuria.cvplatform.dto.request.ProjectRequest;
import com.nuria.cvplatform.dto.response.ProjectResponse;
import com.nuria.cvplatform.exception.ResourceNotFoundException;
import com.nuria.cvplatform.model.Profile;
import com.nuria.cvplatform.model.Project;
import com.nuria.cvplatform.repository.ProfileRepository;
import com.nuria.cvplatform.repository.ProjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final ProfileRepository profileRepository;

    @Transactional(readOnly = true)
    public List<ProjectResponse> getProjectsByProfile(Long profileId) {
        return projectRepository.findByProfileId(profileId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional
    public ProjectResponse create(ProjectRequest request) {
        Profile profile = profileRepository.findAll()
                .stream()
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Profile not found"));

        Project project = Project.builder()
                .name(request.getName())
                .description(request.getDescription())
                .techStack(request.getTechStack())
                .githubUrl(request.getGithubUrl())
                .url(request.getUrl())
                .profile(profile)
                .build();

        return toResponse(projectRepository.save(project));
    }

    @Transactional
    public ProjectResponse update(Long id, ProjectRequest request) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Project", id));

        project.setName(request.getName());
        project.setDescription(request.getDescription());
        project.setTechStack(request.getTechStack());
        project.setGithubUrl(request.getGithubUrl());
        project.setUrl(request.getUrl());

        return toResponse(projectRepository.save(project));
    }

    @Transactional
    public void delete(Long id) {
        if (!projectRepository.existsById(id)) {
            throw new ResourceNotFoundException("Project", id);
        }
        projectRepository.deleteById(id);
    }

    private ProjectResponse toResponse(Project project) {
        return ProjectResponse.builder()
                .id(project.getId())
                .name(project.getName())
                .description(project.getDescription())
                .techStack(project.getTechStack())
                .githubUrl(project.getGithubUrl())
                .url(project.getUrl())
                .build();
    }
}