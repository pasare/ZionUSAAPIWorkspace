package org.zionusa.base.enums;

public enum EBranchType {
    BIBLE_STUDY_CENTER("BIBLE_STUDY_CENTER"),
    DEFAULT("DEFAULT"), // For WeLoveU
    HOUSE("HOUSE"),
    OFFICE("OFFICE"),
    TEMPLE("TEMPLE");

    private final String value;

    EBranchType(String value) {
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
