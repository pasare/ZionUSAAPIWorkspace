package org.zionusa.event.domain;

public enum ApprovalType {

    ADMIN("admin"),
    GA("ga"),
    MANAGER("manager");

    private final String value;

    ApprovalType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
