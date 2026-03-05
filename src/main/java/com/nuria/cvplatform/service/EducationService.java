package com.nuria.cvplatform.service;

import com.nuria.cvplatform.dto.request.EducationRequest;
import com.nuria.cvplatform.dto.response.EducationResponse;
import com.nuria.cvplatform.exception.ResourceNotFoundException;
import com.nuria.cvplatform.model.Education;
import com.nuria.cvplatform.model.Profile;
import com.nuria.cvplatform.repository.EducationRepository;
import com.nuria.cvplatform.repository.ProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EducationService {

    private final EducationRepository educationRepository;
    private final ProfileRepository profileRepository;

    @Transactional(readOnly = true)
    public List<EducationResponse> getEducationByProfile(Long profileId) {
        return educationRepository.findByProfileIdOrderByStartDateDesc(profileId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional
    public EducationResponse create(EducationRequest request) {
        Profile profile = profileRepository.findAll()
                .stream()
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Profile not found"));

        Education education = Education.builder()
                .institution(request.getInstitution())
                .degree(request.getDegree())
                .location(request.getLocation())
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .description(request.getDescription())
                .displayOrder(request.getDisplayOrder())
                .profile(profile)
                .build();

        return toResponse(educationRepository.save(education));
    }

    @Transactional
    public EducationResponse update(Long id, EducationRequest request) {
        Education education = educationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Education", id));

        education.setInstitution(request.getInstitution());
        education.setDegree(request.getDegree());
        education.setLocation(request.getLocation());
        education.setStartDate(request.getStartDate());
        education.setEndDate(request.getEndDate());
        education.setDescription(request.getDescription());
        education.setDisplayOrder(request.getDisplayOrder());

        return toResponse(educationRepository.save(education));
    }

    @Transactional
    public void delete(Long id) {
        if (!educationRepository.existsById(id)) {
            throw new ResourceNotFoundException("Education", id);
        }
        educationRepository.deleteById(id);
    }

    private EducationResponse toResponse(Education education) {
        return EducationResponse.builder()
                .id(education.getId())
                .institution(education.getInstitution())
                .degree(education.getDegree())
                .location(education.getLocation())
                .startDate(education.getStartDate())
                .endDate(education.getEndDate())
                .description(education.getDescription())
                .displayOrder(education.getDisplayOrder())
                .build();
    }
}