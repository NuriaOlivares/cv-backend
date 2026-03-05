package com.nuria.cvplatform.service;

import com.nuria.cvplatform.dto.request.ChatRequest;
import com.nuria.cvplatform.dto.response.ChatResponse;
import com.nuria.cvplatform.dto.response.ProfileResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
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

    public ChatResponse chat(ChatRequest request) {

        ProfileResponse profile = profileService.getProfile();
        String systemPrompt = buildSystemPrompt(profile);

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
                You are an AI assistant representing %s, a %s.
                Answer questions about her professional background, skills and experience.
                Be concise, professional and friendly. Answer in the same language the user writes in.
                Always speak in third person about Nuria.
                If asked something unrelated to her professional profile, politely redirect the conversation.
                
                Here is her profile:
                
                Summary: %s
                
                Experience: %s
                
                Skills: %s
                
                Education: %s
                
                Projects: %s
                """.formatted(
                profile.getFullName(),
                profile.getTitle(),
                profile.getSummary(),
                formatExperiences(profile),
                formatSkills(profile),
                formatEducation(profile),
                formatProjects(profile)
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