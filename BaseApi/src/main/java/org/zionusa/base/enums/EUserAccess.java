package org.zionusa.base.enums;

public enum EUserAccess {
    ADMIN("Admin"),
    ASSOCIATION("Association"),
    BRANCH("Branch"),
    CHURCH("Church"),
    GROUP("Group"),
    MAIN_BRANCH("Main Branch"),
    MEMBER("Member"),
    OVERSEER("Overseer"),
    TEAM("Team");

    private final String value;

    EUserAccess(String value) {
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
