package com.nuria.cvplatform.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class ChatRequest {
    @NotBlank(message = "Message is required")
    private String message;
}