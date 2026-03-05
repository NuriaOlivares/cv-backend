package com.nuria.cvplatform.dto.response;

import com.nuria.cvplatform.enums.LevelCategory;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class LanguageResponse {
    private Long id;
    private String name;
    private LevelCategory level;
    private String levelDisplayName;
    private Integer displayOrder;
}