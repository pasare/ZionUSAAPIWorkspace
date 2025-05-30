package org.zionusa.base.enums;

public enum EUserRole {
    ADMIN("Admin"),
    ASSOCIATION_LEADER("Association Leader"),
    CHURCH_LEADER("Church Leader"),
    GROUP_LEADER("Group Leader"),
    MEMBER("Member"),
    PASTOR("Pastor"),
    OVERSEER("Overseer"),
    TEAM_LEADER("Team Leader");

    private final String value;

    EUserRole(String value) {
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
