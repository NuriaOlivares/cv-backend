package com.nuria.cvplatform.service;

import com.nuria.cvplatform.dto.request.ExperienceRequest;
import com.nuria.cvplatform.dto.response.ExperienceResponse;
import com.nuria.cvplatform.exception.ResourceNotFoundException;
import com.nuria.cvplatform.model.Experience;
import com.nuria.cvplatform.model.Profile;
import com.nuria.cvplatform.repository.ExperienceRepository;
import com.nuria.cvplatform.repository.ProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ExperienceService {

    private final ExperienceRepository experienceRepository;
    private final ProfileRepository profileRepository;

    @Transactional(readOnly = true)
    public List<ExperienceResponse> getExperiencesByProfile(Long profileId) {
        return experienceRepository.findByProfileIdOrderByStartDateDesc(profileId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional
    public ExperienceResponse create(ExperienceRequest request) {
        Profile profile = profileRepository.findAll()
                .stream()
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Profile not found"));

        Experience experience = Experience.builder()
                .role(request.getRole())
                .company(request.getCompany())
                .description(request.getDescription())
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .profile(profile)
                .build();

        return toResponse(experienceRepository.save(experience));
    }

    @Transactional
    public ExperienceResponse update(Long id, ExperienceRequest request) {
        Experience experience = experienceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Experience", id));

        experience.setRole(request.getRole());
        experience.setCompany(request.getCompany());
        experience.setDescription(request.getDescription());
        experience.setStartDate(request.getStartDate());
        experience.setEndDate(request.getEndDate());

        return toResponse(experienceRepository.save(experience));
    }

    @Transactional
    public void delete(Long id) {
        if (!experienceRepository.existsById(id)) {
            throw new ResourceNotFoundException("Experience", id);
        }
        experienceRepository.deleteById(id);
    }

    private ExperienceResponse toResponse(Experience experience) {
        return ExperienceResponse.builder()
                .id(experience.getId())
                .role(experience.getRole())
                .company(experience.getCompany())
                .description(experience.getDescription())
                .startDate(experience.getStartDate())
                .endDate(experience.getEndDate())
                .build();
    }
}