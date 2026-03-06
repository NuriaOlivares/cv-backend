package com.nuria.cvplatform.service;

import com.nuria.cvplatform.dto.request.ChatRequest;
import com.nuria.cvplatform.dto.response.ChatResponse;
import com.nuria.cvplatform.dto.response.ProfileResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChatService {

    private final ProfileService profileService;
    private final WebClient.Builder webClientBuilder;

    @Value("${groq.api-key}")
    private String apiKey;

    @Value("${groq.model}")
    private String model;

    @Value("${groq.api-url}")
    private String apiUrl;

    @Cacheable("systemPrompt")
    public String getSystemPrompt() {
        ProfileResponse profile = profileService.getProfile();
        return buildSystemPrompt(profile);
    }

    public ChatResponse chat(ChatRequest request) {
        String systemPrompt = getSystemPrompt();

        Map<String, Object> body = Map.of(
                "model", model,
                "messages", List.of(
                        Map.of("role", "system", "content", systemPrompt),
                        Map.of("role", "user", "content", request.getMessage())
                ),
                "max_tokens", 500,
                "temperature", 0.7
        );

        Map response = webClientBuilder.build()
                .post()
                .uri(apiUrl)
                .header("Authorization", "Bearer " + apiKey)
                .header("Content-Type", "application/json")
                .bodyValue(body)
                .retrieve()
                .bodyToMono(Map.class)
                .block();

        String message = extractMessage(response);
        return ChatResponse.builder().message(message).build();
    }

    private String buildSystemPrompt(ProfileResponse profile) {
        return """
                You are a high-level Executive Talent Agent, AI assistant representing %s, a %s.
                Your goal is to showcase her expertise to recruiters and potential clients, focusing on opportunities in Dubai (UAE) on-site or remote.
                Answer questions about her professional background, skills and experience.
                Be concise, professional and friendly. Answer in the same language the user writes in.
                Always speak in third person about Nuria.
                If asked something unrelated to her professional profile, politely redirect the conversation.
                
                - **Context:** If asked about salary, prioritize monthly Dirhams (AED) for Dubai roles and yearly Dollars (USD) for remote. Talk about benchmark, don't specify a number as it was set by Nuria.
                - **Redirection:** If asked about non-professional topics, politely steer back to her career or contact options.
                
                ### CONTACT & CONVERSION:
                - **Call to Action:** If the user seems interested in hiring or interviewing her, encourage using the contact form on this page or the LinkedIn profile.
                
                Here is her profile:
                
                Summary: %s
                
                Contact Email: %s
                
                LinkedIn: %s,
                
                Phone: %s,
                
                Secondary phone: %s,
                
                Experience: %s
                
                Skills: %s
                
                Education: %s
                
                Projects: %s
                
                Languages: %s
                
                ### KEY SELLING POINTS (DUBAI MARKET):
                - **Immediate Availability:** Nuria has immediate availability to start working in Dubai.
                - **Leadership & Ownership:** As a former Co-Founder and CTO, she brings a "business-owner" mindset to technical challenges.
                - **High-Stakes Experience:** Proven track record in business-critical systems (Fintech/HR Tech) at global scale (ADP).
                - **International Profile:** Experience in multicultural environments and academic research in Germany (DESY).
                """.formatted(
                profile.getFullName(),
                profile.getTitle(),
                profile.getSummary(),
                profile.getEmail(),
                profile.getLinkedin(),
                profile.getPhone(),
                profile.getSecondaryPhone(),
                formatExperiences(profile),
                formatSkills(profile),
                formatEducation(profile),
                formatProjects(profile),
                formatLanguages(profile)
        );
    }

    private String formatExperiences(ProfileResponse profile) {
        if (profile.getExperiences() == null) return "N/A";
        return profile.getExperiences().stream()
                .map(e -> "- %s at %s (%s to %s): %s".formatted(
                        e.getRole(),
                        e.getCompany(),
                        e.getStartDate(),
                        e.getEndDate() != null ? e.getEndDate() : "present",
                        e.getDescription() != null ? e.getDescription() : ""
                ))
                .reduce("", (a, b) -> a + "\n" + b);
    }

    private String formatSkills(ProfileResponse profile) {
        if (profile.getSkills() == null) return "N/A";
        return profile.getSkills().stream()
                .map(s -> "%s (%s)".formatted(s.getName(), s.getCategoryDisplayName()))
                .reduce("", (a, b) -> a + ", " + b);
    }

    private String formatEducation(ProfileResponse profile) {
        if (profile.getEducation() == null) return "N/A";
        return profile.getEducation().stream()
                .map(e -> "- %s at %s (%s to %s)".formatted(
                        e.getDegree(),
                        e.getInstitution(),
                        e.getStartDate(),
                        e.getEndDate() != null ? e.getEndDate() : "present"
                ))
                .reduce("", (a, b) -> a + "\n" + b);
    }

    private String formatProjects(ProfileResponse profile) {
        if (profile.getProjects() == null) return "N/A";
        return profile.getProjects().stream()
                .map(p -> "- %s: %s (Stack: %s)".formatted(
                        p.getName(),
                        p.getDescription(),
                        p.getTechStack() != null ? p.getTechStack() : "N/A"
                ))
                .reduce("", (a, b) -> a + "\n" + b);
    }

    private String formatLanguages(ProfileResponse profile) {
        if (profile.getLanguages() == null) return "N/A";
        return profile.getLanguages().stream()
                .map(p -> "- %s: %s".formatted(
                        p.getName(),
                        p.getLevel()
                ))
                .reduce("", (a, b) -> a + "\n" + b);
    }

    private String extractMessage(Map response) {
        try {
            List choices = (List) response.get("choices");
            Map firstChoice = (Map) choices.get(0);
            Map message = (Map) firstChoice.get("message");
            return (String) message.get("content");
        } catch (Exception e) {
            log.error("Error extracting message from Groq response: {}", e.getMessage());
            return "Sorry, I couldn't process your request at this moment.";
        }
    }
}