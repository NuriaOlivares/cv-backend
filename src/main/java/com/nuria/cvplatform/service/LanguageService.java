package com.nuria.cvplatform.service;

import com.nuria.cvplatform.dto.request.LanguageRequest;
import com.nuria.cvplatform.dto.response.LanguageResponse;
import com.nuria.cvplatform.exception.ResourceNotFoundException;
import com.nuria.cvplatform.model.Language;
import com.nuria.cvplatform.model.Profile;
import com.nuria.cvplatform.repository.LanguageRepository;
import com.nuria.cvplatform.repository.ProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LanguageService {

    private final LanguageRepository languageRepository;
    private final ProfileRepository profileRepository;

    @Transactional(readOnly = true)
    public List<LanguageResponse> getLanguagesByProfile(Long profileId) {
        return languageRepository.findByProfileIdOrderByDisplayOrderAsc(profileId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional
    public LanguageResponse create(LanguageRequest request) {
        Profile profile = profileRepository.findAll()
                .stream()
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Profile not found"));

        Language language = Language.builder()
                .name(request.getName())
                .level(request.getLevel())
                .displayOrder(request.getDisplayOrder())
                .profile(profile)
                .build();

        return toResponse(languageRepository.save(language));
    }

    @Transactional
    public LanguageResponse update(Long id, LanguageRequest request) {
        Language language = languageRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Language", id));

        language.setName(request.getName());
        language.setLevel(request.getLevel());
        language.setDisplayOrder(request.getDisplayOrder());

        return toResponse(languageRepository.save(language));
    }

    @Transactional
    public void delete(Long id) {
        if (!languageRepository.existsById(id)) {
            throw new ResourceNotFoundException("Language", id);
        }
        languageRepository.deleteById(id);
    }

    private LanguageResponse toResponse(Language language) {
        return LanguageResponse.builder()
                .id(language.getId())
                .name(language.getName())
                .level(language.getLevel())
                .levelDisplayName(language.getLevel().getDisplayName())
                .displayOrder(language.getDisplayOrder())
                .build();
    }
}