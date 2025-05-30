package org.zionusa.management.enums;

public enum ETransferRequestStatus {
    APPROVED("A"),
    DENIED("D"),
    PENDING("P");

    private final String value;

    ETransferRequestStatus(String value) {
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
