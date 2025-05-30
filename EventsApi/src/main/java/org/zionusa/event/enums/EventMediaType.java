package org.zionusa.event.enums;

public enum EventMediaType {
    BANNER_GROUP_PHOTOS("BANNER_GROUP_PHOTOS"),
    FRAGRANCE_VIDEOS("FRAGRANCE_VIDEOS"),
    FRAGRANCE_WRITINGS("FRAGRANCE_WRITINGS"),
    GENERAL("GENERAL"),
    NEWS_ARTICLE("NEWS_ARTICLE"),
    NEWS_MEDIA("NEWS_MEDIA"),
    PHOTOS("PHOTOS"),
    VIDEOS("VIDEOS"),
    VIP_PHOTOS("VIP_PHOTOS"),
    VIP_VIDEOS("VIP_VIDEOS"),
    WRITE_UP("WRITE_UP");

    private final String value;

    EventMediaType(String value) {
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
