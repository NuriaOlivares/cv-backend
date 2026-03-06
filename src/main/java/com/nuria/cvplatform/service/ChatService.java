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

    private final SystemPromptService systemPromptService;
    private final WebClient.Builder webClientBuilder;

    @Value("${groq.api-key}")
    private String apiKey;

    @Value("${groq.model}")
    private String model;

    @Value("${groq.api-url}")
    private String apiUrl;

    public ChatResponse chat(ChatRequest request) {
        String systemPrompt = systemPromptService.getSystemPrompt();

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