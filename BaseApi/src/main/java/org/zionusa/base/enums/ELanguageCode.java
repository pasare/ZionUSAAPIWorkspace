package org.zionusa.base.enums;

public enum ELanguageCode {
    CHINESE("zh"),
    ENGLISH("en"),
    FRENCH("fr"),
    GERMAN("de"),
    HAITIAN_CREOLE("ht"),
    ITALIAN("it"),
    JAPANESE("ja"),
    KOREAN("ko"),
    PORTUGUESE("pt"),
    ROMANIAN("ro"),
    RUSSIAN("ru"),
    SPANISH("es"),
    UKRAINIAN("uk");

    private final String value;

    ELanguageCode(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public boolean contains(String otherValue) {
        if (otherValue == null) {
            return false;
        }
        return this.value.toUpperCase().contains(otherValue.toUpperCase());
    }

    public boolean is(String otherValue) {
        if (otherValue == null) {
            return false;
        }
        return this.value.equalsIgnoreCase(otherValue);
    }
}
