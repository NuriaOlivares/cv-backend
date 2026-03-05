package com.nuria.cvplatform.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class LoginResponse {
    private String userId;
    private String role;
}