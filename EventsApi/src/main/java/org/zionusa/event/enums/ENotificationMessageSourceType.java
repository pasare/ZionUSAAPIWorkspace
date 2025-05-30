package org.zionusa.event.enums;

public enum ENotificationMessageSourceType {
    ADMIN_APPROVAL_TITLE("notification.email.admin.approval.reminder.title"),
    ADMIN_APPROVAL_MESSAGE("notification.email.admin.approval.reminder.message"),
    APPROVAL_TITLE("notification.email.approval.title."),
    APPROVAL_MESSAGE("notification.email.approval.message."),
    BRANCH_MESSAGE("notification.email.branch.message"),
    DENIED_TITLE("notification.email.denied.title"),
    DENIED_MESSAGE("notification.email.denied.message"),
    GA_APPROVAL_REMINDER_TITLE("notification.email.ga.approval.reminder.title"),
    GA_APPROVAL_REMINDER_MESSAGE("notification.email.ga.approval.reminder.message"),
    MULTIMEDIA_APPROVAL_TITLE("notification.email.multimedia.approval.title"),
    MULTIMEDIA_APPROVAL_MESSAGE("notification.email.multimedia.approval.message"),
    REQUEST_TITLE("notification.email.request.title"),
    REQUEST_MESSAGE("notification.email.request.message"),
    RESULTS_SURVEY_REMINDER_TITLE("notification.email.result.survey.reminder.title"),
    RESULTS_SURVEY_REMINDER_MESSAGE("notification.email.result.survey.reminder.message"),
    PHOTO_SUBMISSION_REMINDER_TITLE("notification.email.photo.submission.reminder.title"),
    PHOTO_SUBMISSION_REMINDER_MESSAGE("notification.email.photo.submission.reminder.message"),
    GUESTS_EVENT_REMINDER_TITLE("notification.email.guest.event.reminder.title"),
    GUESTS_EVENT_REMINDER_MESSAGE("notification.email.guest.event.reminder.message"),
    MONTH_POST_EVENT_REMINDER_TITLE("notification.email.month.post.event.update.reminder.title"),
    MONTH_POST_EVENT_REMINDER_MESSAGE("notification.email.month.post.event.update.reminder.message");




    private final String value;

    ENotificationMessageSourceType(String value) {
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
