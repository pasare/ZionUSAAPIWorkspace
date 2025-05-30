package org.zionusa.base.enums;

public enum EUserGender {
    FEMALE("F"),
    MALE("M");

    private final String value;

    EUserGender(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public String getDisplayValue() {
        return value.equals("F") ? "Female" : "Male";
    }

    public boolean contains(String otherValue) {
        if (otherValue == null) {
            return false;
        }
        return this.value.toUpperCase().contains(otherValue.toUpperCase()) ||
            getDisplayValue().toUpperCase().contains(otherValue.toUpperCase());
    }

    public boolean is(String otherValue) {
        if (otherValue == null) {
            return false;
        }
        return this.value.equalsIgnoreCase(otherValue) || getDisplayValue().equalsIgnoreCase(otherValue);
    }
}
