package org.zionusa.event.domain;

public enum WorkflowStatus {
    PENDING("Pending"),
    SUBMITTED("Submitted"),
    MANAGER_REVIEW("Manager Review"),
    MANAGER_APPROVED("Manager Approved"),
    MANAGER_REJECTED("Manager Rejected"),
    ADMIN_APPROVED("Admin Approved"),
    ADMIN_REJECTED("Admin Rejected"),
    GA_APPROVED("GA Approved"),
    GA_REJECTED("GA Rejected"),
    ARCHIVED("Archived");

    private final String status;

    WorkflowStatus(String status) {
        this.status = status;
    }

    public String status() {
        return status;
    }

    public static WorkflowStatus getByName(String status) {

        for (WorkflowStatus workflowStatus : WorkflowStatus.values()) {
            if (workflowStatus.status.equals(status)) {
                return workflowStatus;
            }
        }
        return null;
    }
}
