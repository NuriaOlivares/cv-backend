package com.nuria.cvplatform.enums;

public enum SkillCategory {
    LANGUAGES("Programming Languages"),
    FRAMEWORKS_AND_APIS("Frameworks & APIs"),
    CLOUD_AND_DEVOPS("Cloud & DevOps"),
    DATABASES("Databases"),
    TESTING_AND_QUALITY("Testing & Quality"),
    PRACTICES_AND_METHODOLOGIES("Practices & Methodologies");

    private final String displayName;

    SkillCategory(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}