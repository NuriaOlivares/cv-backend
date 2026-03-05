package com.nuria.cvplatform.controller;

import com.nuria.cvplatform.dto.request.ChatRequest;
import com.nuria.cvplatform.dto.response.ChatResponse;
import com.nuria.cvplatform.service.ChatService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/chat")
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;

    @PostMapping
    public ResponseEntity<ChatResponse> chat(
            @Valid @RequestBody ChatRequest request
    ) {
        return ResponseEntity.ok(chatService.chat(request));
    }
}