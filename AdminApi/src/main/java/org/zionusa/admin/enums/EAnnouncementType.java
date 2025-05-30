package org.zionusa.admin.enums;

public enum EAnnouncementType {
    // This will call enum constructor with one
    // String argument
    GENERAL(1),
    FRAGRANCE(2),
    HELP_VIDEO(3);

    private final Integer value;

    EAnnouncementType(Integer value) {
        this.value = value;
    }

    public Integer getValue() {
        return value;
    }

    public boolean is(Integer otherValue) {
        if (otherValue == null) {
            return false;
        }
        return this.value.equals(otherValue);
    }
}
