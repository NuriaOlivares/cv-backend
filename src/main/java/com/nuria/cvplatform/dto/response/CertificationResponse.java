package com.nuria.cvplatform.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@Builder
public class CertificationResponse {
    private Long id;
    private String name;
    private String issuer;
    private LocalDate issueDate;
    private String credentialUrl;
    private Integer displayOrder;
}