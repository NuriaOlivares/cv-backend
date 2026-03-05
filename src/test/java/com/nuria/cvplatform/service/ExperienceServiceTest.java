package com.nuria.cvplatform.service;

import com.nuria.cvplatform.dto.request.ExperienceRequest;
import com.nuria.cvplatform.dto.response.ExperienceResponse;
import com.nuria.cvplatform.exception.ResourceNotFoundException;
import com.nuria.cvplatform.model.Experience;
import com.nuria.cvplatform.model.Profile;
import com.nuria.cvplatform.repository.ExperienceRepository;
import com.nuria.cvplatform.repository.ProfileRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ExperienceServiceTest {

    @Mock
    private ExperienceRepository experienceRepository;
    @Mock
    private ProfileRepository profileRepository;

    @InjectMocks
    private ExperienceService experienceService;

    private final Profile mockProfile = Profile.builder().id(1L).fullName("Nuria Olivares").build();

    @Test
    void getExperiencesByProfile_shouldReturnList() {
        Experience experience = Experience.builder()
                .id(1L)
                .role("Senior Software Engineer")
                .company("ADP")
                .startDate(LocalDate.of(2023, 1, 1))
                .build();

        when(experienceRepository.findByProfileIdOrderByEndDateDesc(1L))
                .thenReturn(List.of(experience));

        List<ExperienceResponse> result = experienceService.getExperiencesByProfile(1L);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getRole()).isEqualTo("Senior Software Engineer");
        assertThat(result.get(0).getCompany()).isEqualTo("ADP");
    }

    @Test
    void create_shouldSaveAndReturnExperience() {
        ExperienceRequest request = new ExperienceRequest();
        request.setRole("Senior Software Engineer");
        request.setCompany("ADP");
        request.setStartDate(LocalDate.of(2023, 1, 1));

        Experience saved = Experience.builder()
                .id(1L)
                .role("Senior Software Engineer")
                .company("ADP")
                .startDate(LocalDate.of(2023, 1, 1))
                .profile(mockProfile)
                .build();

        when(profileRepository.findAll()).thenReturn(List.of(mockProfile));
        when(experienceRepository.save(any())).thenReturn(saved);

        ExperienceResponse result = experienceService.create(request);

        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getRole()).isEqualTo("Senior Software Engineer");
        verify(experienceRepository).save(any());
    }

    @Test
    void update_shouldModifyAndReturnExperience() {
        Experience existing = Experience.builder()
                .id(1L)
                .role("Software Engineer")
                .company("ADP")
                .startDate(LocalDate.of(2022, 1, 1))
                .build();

        ExperienceRequest request = new ExperienceRequest();
        request.setRole("Senior Software Engineer");
        request.setCompany("ADP");
        request.setStartDate(LocalDate.of(2022, 1, 1));

        when(experienceRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(experienceRepository.save(any())).thenReturn(existing);

        ExperienceResponse result = experienceService.update(1L, request);

        assertThat(result.getRole()).isEqualTo("Senior Software Engineer");
        verify(experienceRepository).save(existing);
    }

    @Test
    void update_shouldThrowResourceNotFoundException_whenExperienceDoesNotExist() {
        ExperienceRequest request = new ExperienceRequest();
        request.setRole("Senior Software Engineer");
        request.setCompany("ADP");
        request.setStartDate(LocalDate.of(2022, 1, 1));

        when(experienceRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> experienceService.update(99L, request))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Experience");
    }

    @Test
    void delete_shouldCallRepository_whenExperienceExists() {
        when(experienceRepository.existsById(1L)).thenReturn(true);

        experienceService.delete(1L);

        verify(experienceRepository).deleteById(1L);
    }

    @Test
    void delete_shouldThrowResourceNotFoundException_whenExperienceDoesNotExist() {
        when(experienceRepository.existsById(99L)).thenReturn(false);

        assertThatThrownBy(() -> experienceService.delete(99L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Experience");

        verify(experienceRepository, never()).deleteById(any());
    }
}