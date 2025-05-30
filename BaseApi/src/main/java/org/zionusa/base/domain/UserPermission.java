package org.zionusa.base.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode()
public class UserPermission implements BasePermissionDomain<Integer> {
    // Application
    public static final String VIEW_GRAPHICS_SHAREPOINT = "VIEW_GRAPHICS_SHAREPOINT";

    // Event Proposal
    public static final String CREATE_EVENT_PROPOSAL = "CREATE_EVENT_PROPOSAL";
    public static final String EDIT_EVENT_PROPOSAL = "EDIT_EVENT_PROPOSAL";
    public static final String EDIT_EVENT_TYPES = "EDIT_EVENT_TYPES";
    public static final String DELETE_EVENT_PROPOSAL = "DELETE_EVENT_PROPOSAL";
    public static final String PUBLISH_EVENT_PROPOSAL = "PUBLISH_EVENT_PROPOSAL";
    public static final String VIEW_UNPUBLISHED_EVENT_PROPOSAL = "VIEW_UNPUBLISHED_EVENT_PROPOSAL";
    public static final String VIEW_EVENT_CATEGORIES = "VIEW_EVENT_CATEGORIES";
    public static final String VIEW_EVENT_TYPES = "VIEW_EVENT_TYPES";

    // Event Media
    public static final String EDIT_EVENT_MEDIA = "EDIT_EVENT_MEDIA";

    // Results Survey
    public static final String CREATE_RESULTS_SURVEY = "CREATE_RESULTS_SURVEY";
    public static final String EDIT_RESULTS_SURVEY = "EDIT_RESULTS_SURVEY";
    public static final String DELETE_RESULTS_SURVEY = "DELETE_RESULTS_SURVEY";
    public static final String VIEW_RESULTS_SURVEY = "VIEW_RESULTS_SURVEY";

    // Plans
    public static final String ADMIN_PLANS = "ADMIN_PLANS";
    public static final String CHURCH_PLANS = "CHURCH_PLANS";
    public static final String GROUP_PLANS = "GROUP_PLANS";

    // User
    public static final String CREATE_USER = "CREATE_USER";
    public static final String DELETE_USER = "DELETE_USER";
    public static final String DISABLE_USER = "DISABLE_USER";
    public static final String EDIT_EVENTS_USER_APPLICATION_ROLES = "EDIT_EVENTS_USER_APPLICATION_ROLES";
    public static final String EDIT_USER_APPLICATION_ROLES = "EDIT_USER_APPLICATION_ROLES";
    public static final String UPDATE_USER = "UPDATE_USER";
    public static final String UPDATE_GOSPEL_WORKER_STATUS = "UPDATE_GOSPEL_WORKER_STATUS";

    private Integer id;
    private String name;
    private String displayName;
    private String description;
}
