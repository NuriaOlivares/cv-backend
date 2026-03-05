package com.nuria.cvplatform.service;

import com.nuria.cvplatform.dto.request.SkillRequest;
import com.nuria.cvplatform.dto.response.SkillResponse;
import com.nuria.cvplatform.exception.ResourceNotFoundException;
import com.nuria.cvplatform.model.Profile;
import com.nuria.cvplatform.model.Skill;
import com.nuria.cvplatform.repository.ProfileRepository;
import com.nuria.cvplatform.repository.SkillRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SkillService {

    private final SkillRepository skillRepository;
    private final ProfileRepository profileRepository;

    @Transactional(readOnly = true)
    public List<SkillResponse> getSkillsByProfile(Long profileId) {
        return skillRepository.findByProfileIdOrderByDisplayOrderAsc(profileId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional
    public SkillResponse create(SkillRequest request) {
        Profile profile = profileRepository.findAll()
                .stream()
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Profile not found"));

        Skill skill = Skill.builder()
                .name(request.getName())
                .category(request.getCategory())
                .displayOrder(request.getDisplayOrder())
                .profile(profile)
                .build();

        return toResponse(skillRepository.save(skill));
    }

    @Transactional
    public SkillResponse update(Long id, SkillRequest request) {
        Skill skill = skillRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Skill", id));

        skill.setName(request.getName());
        skill.setCategory(request.getCategory());
        skill.setDisplayOrder(request.getDisplayOrder());

        return toResponse(skillRepository.save(skill));
    }

    @Transactional
    public void delete(Long id) {
        if (!skillRepository.existsById(id)) {
            throw new ResourceNotFoundException("Skill", id);
        }
        skillRepository.deleteById(id);
    }

    private SkillResponse toResponse(Skill skill) {
        return SkillResponse.builder()
                .id(skill.getId())
                .name(skill.getName())
                .category(skill.getCategory())
                .categoryDisplayName(skill.getCategory().getDisplayName())
                .displayOrder(skill.getDisplayOrder())
                .build();
    }
}