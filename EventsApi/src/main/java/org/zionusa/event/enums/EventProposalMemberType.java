package org.zionusa.event.enums;

public enum EventProposalMemberType {
    PRESENTER("PRESENTER"),
    SPOKESPERSON("SPOKESPERSON");

    private final String value;

    EventProposalMemberType(String value) {
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
