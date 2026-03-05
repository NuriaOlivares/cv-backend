package com.nuria.cvplatform.dto.response;

import com.nuria.cvplatform.enums.SkillCategory;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SkillResponse {
    private Long id;
    private String name;
    private SkillCategory category;
    private String categoryDisplayName;
    private Integer displayOrder;
}