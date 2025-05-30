package org.zionusa.base.enums;

import java.time.ZoneId;

public enum EZoneId {
    NEW_YORK("America/New_York");

    private final String value;

    EZoneId(String value) {
        this.value = value;
    }

    public ZoneId getValue() {
        return ZoneId.of(this.value);
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
