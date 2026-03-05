package com.nuria.cvplatform.service;

import com.nuria.cvplatform.dto.request.ProfileRequest;
import com.nuria.cvplatform.dto.response.*;
import com.nuria.cvplatform.exception.ResourceNotFoundException;
import com.nuria.cvplatform.model.Profile;
import com.nuria.cvplatform.repository.ProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProfileService {

    private final ProfileRepository profileRepository;
    private final ExperienceService experienceService;
    private final ProjectService projectService;
    private final SkillService skillService;
    private final EducationService educationService;
    private final CertificationService certificationService;
    private final LanguageService languageService;

    @Transactional(readOnly = true)
    public ProfileResponse getProfile() {
        Profile profile = getProfileEntity();

        return ProfileResponse.builder()
                .id(profile.getId())
                .fullName(profile.getFullName())
                .title(profile.getTitle())
                .summary(profile.getSummary())
                .email(profile.getEmail())
                .phone(profile.getPhone())
                .secondaryPhone(profile.getSecondaryPhone())
                .linkedin(profile.getLinkedin())
                .github(profile.getGithub())
                .experiences(experienceService.getExperiencesByProfile(profile.getId()))
                .projects(projectService.getProjectsByProfile(profile.getId()))
                .skills(skillService.getSkillsByProfile(profile.getId()))
                .education(educationService.getEducationByProfile(profile.getId()))
                .certifications(certificationService.getCertificationsByProfile(profile.getId()))
                .languages(languageService.getLanguagesByProfile(profile.getId()))
                .build();
    }

    @Transactional
    public ProfileResponse updateProfile(ProfileRequest request) {
        Profile profile = getProfileEntity();

        profile.setFullName(request.getFullName());
        profile.setTitle(request.getTitle());
        profile.setSummary(request.getSummary());
        profile.setEmail(request.getEmail());
        profile.setPhone(request.getPhone());
        profile.setSecondaryPhone(request.getPhone());
        profile.setLinkedin(request.getLinkedin());
        profile.setGithub(request.getGithub());

        profileRepository.save(profile);
        return getProfile();
    }

    public Profile getProfileEntity() {
        return profileRepository.findAll()
                .stream()
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Profile not found"));
    }
}