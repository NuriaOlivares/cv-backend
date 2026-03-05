package com.nuria.cvplatform.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class CertificationRequest {
    @NotBlank(message = "Name is required")
    private String name;
    private String issuer;
    private LocalDate issueDate;
    private String credentialUrl;
    private Integer displayOrder;
}