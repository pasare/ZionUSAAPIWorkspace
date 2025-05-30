package org.zionusa.base.enums;

public enum EApplicationRolePrefix {
    EVENT("EVENT_"),
    EVENT_DEFAULT("EVENT_DEFAULT"),
    EVENT_ASEZ_IUBA("EVENT_ASEZ_IUBA"),
    EVENT_ASEZ_WAO_IWBA("EVENT_ASEZ_WAO_IWBA"),
    STUDY("STUDY_");

    private final String value;

    EApplicationRolePrefix(String value) {
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
