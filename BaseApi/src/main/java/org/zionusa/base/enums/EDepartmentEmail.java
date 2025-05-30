package org.zionusa.base.enums;

public enum EDepartmentEmail {
    EVENTS("Events@"),
    GRAPHICS("Graphics@"),
    PHOTOS("Photos@"),
    VIDEO("VideoDepartment@"),
    AV("AV@");

    private final String value;

    EDepartmentEmail(String value) {
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
