package com.nuria.cvplatform.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class EducationRequest {
    @NotBlank(message = "Institution is required")
    private String institution;
    @NotBlank(message = "Degree is required")
    private String degree;
    private String location;
    @NotNull(message = "Start date is required")
    private LocalDate startDate;
    private LocalDate endDate;
    private String description;
    private Integer displayOrder;
}
