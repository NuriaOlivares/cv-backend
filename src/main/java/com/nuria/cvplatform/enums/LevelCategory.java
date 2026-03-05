package com.nuria.cvplatform.enums;

public enum LevelCategory {
    NATIVE("Native"),
    PROFESSIONAL("Professional"),
    CONVERSATIONAL("Conversational"),
    BEGINNER("Beginner"),
    LEARNING("Learning");

    private final String displayName;

    LevelCategory(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}