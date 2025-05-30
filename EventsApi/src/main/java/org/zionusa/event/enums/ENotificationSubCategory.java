package org.zionusa.event.enums;

public enum ENotificationSubCategory {
    APPROVAL_DENIED("ApprovalDenied"),
    APPROVAL_GRANTED("ApprovalGranted"),
    APPROVAL_REQUESTED("ApprovalRequested"),
    UNFINISHED_REMINDER("unfinished-reminder");

    private final String value;

    ENotificationSubCategory(String value) {
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
