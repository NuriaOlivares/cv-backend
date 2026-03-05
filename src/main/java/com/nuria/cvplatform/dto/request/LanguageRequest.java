package com.nuria.cvplatform.dto.request;

import com.nuria.cvplatform.enums.LevelCategory;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class LanguageRequest {
    @NotBlank(message = "Name is required")
    private String name;
    @NotBlank(message = "Level is required")
    private LevelCategory level;
    private Integer displayOrder;
}