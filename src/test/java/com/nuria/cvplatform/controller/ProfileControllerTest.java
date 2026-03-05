package com.nuria.cvplatform.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nuria.cvplatform.config.SecurityBeans;
import com.nuria.cvplatform.config.SecurityConfig;
import com.nuria.cvplatform.dto.request.ProfileRequest;
import com.nuria.cvplatform.dto.response.ProfileResponse;
import com.nuria.cvplatform.repository.UserRepository;
import com.nuria.cvplatform.security.CustomUserDetailsService;
import com.nuria.cvplatform.security.JwtAuthenticationFilter;
import com.nuria.cvplatform.security.JwtService;
import com.nuria.cvplatform.service.ProfileService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProfileController.class)
@Import({SecurityConfig.class, SecurityBeans.class, JwtAuthenticationFilter.class, JwtService.class, CustomUserDetailsService.class})
@TestPropertySource(properties = {
        "jwt.secret=test_secret_key_min_32_chars_long_here",
        "jwt.expiration=900000"
})
class ProfileControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ProfileService profileService;
    @MockBean
    private UserRepository userRepository;

    @Test
    @WithMockUser(roles = "VIEWER")
    void getProfile_shouldReturn200_whenViewer() throws Exception {
        ProfileResponse profile = ProfileResponse.builder()
                .fullName("Nuria Olivares")
                .title("Senior Software Engineer")
                .email("nuriaolivaresroyo@gmail.com")
                .build();

        when(profileService.getProfile()).thenReturn(profile);

        mockMvc.perform(get("/api/profile"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.fullName").value("Nuria Olivares"))
                .andExpect(jsonPath("$.title").value("Senior Software Engineer"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void updateProfile_shouldReturn200_whenAdmin() throws Exception {
        ProfileRequest request = new ProfileRequest();
        request.setFullName("Nuria Olivares");
        request.setTitle("Senior Software Engineer | Full Stack");
        request.setSummary("Summary here");
        request.setEmail("nuriaolivaresroyo@gmail.com");

        ProfileResponse updated = ProfileResponse.builder()
                .fullName("Nuria Olivares")
                .title("Senior Software Engineer | Full Stack")
                .build();

        when(profileService.updateProfile(any())).thenReturn(updated);

        mockMvc.perform(put("/api/profile")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Senior Software Engineer | Full Stack"));
    }

    @Test
    @WithMockUser(roles = "VIEWER")
    void updateProfile_shouldReturn403_whenViewer() throws Exception {
        ProfileRequest request = new ProfileRequest();
        request.setFullName("Nuria Olivares");
        request.setTitle("Senior Software Engineer");
        request.setSummary("Summary");
        request.setEmail("nuriaolivaresroyo@gmail.com");

        mockMvc.perform(put("/api/profile")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isForbidden());
    }

    @Test
    void getProfile_shouldReturn401_whenNotAuthenticated() throws Exception {
        mockMvc.perform(get("/api/profile"))
                .andExpect(status().isUnauthorized());
    }
}