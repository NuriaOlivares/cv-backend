package com.nuria.cvplatform.service;

import com.nuria.cvplatform.dto.request.CertificationRequest;
import com.nuria.cvplatform.dto.response.CertificationResponse;
import com.nuria.cvplatform.exception.ResourceNotFoundException;
import com.nuria.cvplatform.model.Certification;
import com.nuria.cvplatform.model.Profile;
import com.nuria.cvplatform.repository.CertificationRepository;
import com.nuria.cvplatform.repository.ProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CertificationService {

    private final CertificationRepository certificationRepository;
    private final ProfileRepository profileRepository;

    @Transactional(readOnly = true)
    public List<CertificationResponse> getCertificationsByProfile(Long profileId) {
        return certificationRepository.findByProfileIdOrderByDisplayOrderAsc(profileId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional
    public CertificationResponse create(CertificationRequest request) {
        Profile profile = profileRepository.findAll()
                .stream()
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Profile not found"));

        Certification certification = Certification.builder()
                .name(request.getName())
                .issuer(request.getIssuer())
                .issueDate(request.getIssueDate())
                .credentialUrl(request.getCredentialUrl())
                .displayOrder(request.getDisplayOrder())
                .profile(profile)
                .build();

        return toResponse(certificationRepository.save(certification));
    }

    @Transactional
    public CertificationResponse update(Long id, CertificationRequest request) {
        Certification certification = certificationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Certification", id));

        certification.setName(request.getName());
        certification.setIssuer(request.getIssuer());
        certification.setIssueDate(request.getIssueDate());
        certification.setCredentialUrl(request.getCredentialUrl());
        certification.setDisplayOrder(request.getDisplayOrder());

        return toResponse(certificationRepository.save(certification));
    }

    @Transactional
    public void delete(Long id) {
        if (!certificationRepository.existsById(id)) {
            throw new ResourceNotFoundException("Certification", id);
        }
        certificationRepository.deleteById(id);
    }

    private CertificationResponse toResponse(Certification certification) {
        return CertificationResponse.builder()
                .id(certification.getId())
                .name(certification.getName())
                .issuer(certification.getIssuer())
                .issueDate(certification.getIssueDate())
                .credentialUrl(certification.getCredentialUrl())
                .displayOrder(certification.getDisplayOrder())
                .build();
    }
}