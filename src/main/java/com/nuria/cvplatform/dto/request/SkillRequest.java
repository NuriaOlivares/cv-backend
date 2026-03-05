package com.nuria.cvplatform.dto.request;

import com.nuria.cvplatform.enums.SkillCategory;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class SkillRequest {
    @NotBlank(message = "Name is required")
    private String name;
    @NotNull(message = "Category is required")
    private SkillCategory category;
    private Integer displayOrder;
}
