package org.zionusa.event.domain;

public enum VolunteerType {
    VIP("Vip"),
    CONTACT("Contact"),
    MEMBER("Member");

    private final String type;

    VolunteerType(String type) {
        this.type = type;
    }

    public String type() {
        return type;
    }

    public static VolunteerType getByName(String name) {
        return VolunteerType.valueOf(name);
    }
}
