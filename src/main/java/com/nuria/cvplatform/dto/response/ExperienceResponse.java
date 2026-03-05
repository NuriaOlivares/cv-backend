package com.nuria.cvplatform.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@Builder
public class ExperienceResponse {
    private Long id;
    private String role;
    private String company;
    private String description;
    private LocalDate startDate;
    private LocalDate endDate;
}