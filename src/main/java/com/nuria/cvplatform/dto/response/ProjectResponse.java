package com.nuria.cvplatform.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ProjectResponse {
    private Long id;
    private String name;
    private String description;
    private String techStack;
    private String githubUrl;
    private String url;
}