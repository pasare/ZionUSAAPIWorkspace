package org.zionusa.base.enums;

public enum EUserTitle {
    BROTHER("Brother"),
    DEACON("Deacon"),
    DEACONESS("Deaconess"),
    ELDER("Elder"),
    MEMBER("Member"),
    NONE(""),
    MISSIONARY("Missionary"),
    PASTOR("Pastor"),
    SISTER("Sister");

    private final String value;

    EUserTitle(String value) {
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
