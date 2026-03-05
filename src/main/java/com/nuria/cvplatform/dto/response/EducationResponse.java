package com.nuria.cvplatform.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@Builder
public class EducationResponse {
    private Long id;
    private String institution;
    private String degree;
    private String location;
    private LocalDate startDate;
    private LocalDate endDate;
    private String description;
    private Integer displayOrder;
}