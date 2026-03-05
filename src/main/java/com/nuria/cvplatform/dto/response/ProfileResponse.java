package com.nuria.cvplatform.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class ProfileResponse {
    private Long id;
    private String fullName;
    private String title;
    private String summary;
    private String email;
    private String phone;
    private String secondaryPhone;
    private String linkedin;
    private String github;
    private List<ExperienceResponse> experiences;
    private List<ProjectResponse> projects;
    private List<SkillResponse> skills;
    private List<EducationResponse> education;
    private List<CertificationResponse> certifications;
    private List<LanguageResponse> languages;
}