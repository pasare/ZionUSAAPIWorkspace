package org.zionusa.event.enums;

public enum ENotificationCategory {
    EVENT_NOTIFICATION("EventNotification"),
    EVENT_PROPOSAL("event-proposal"),
    RESULTS_SURVEY("RESULTS_SURVEY"),
    ADMIN_APPROVAL_REMINDER("Admin-approval-reminder"),
    RESULTS_SURVEY_REMINDER("Result-Survey-reminder"),
    PHOTO_SUBMISSION_REMINDER("Photo-Submission-reminder"),
    GUEST_EVENT_REMINDER("Guest-Event-reminder"),
    POST_EVENT_UPDATE_REMINDER("1-Month-Post-Event-Reminder");

    private final String value;

    ENotificationCategory(String value) {
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
