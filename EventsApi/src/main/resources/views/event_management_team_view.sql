CREATE VIEW dbo.event_management_team_view AS
SELECT CONCAT_WS('-', u.id, ua.application_role_id) AS id,
       u.archived                                   AS archived,
       u.hidden                                     AS hidden,
       u.id                                         AS user_id,
       ar.name                                      As application_role_name,
       ua.application_role_id                       AS application_role_id,
       ua.reference_id                              AS application_role_reference_id,
       ar.display_name                              AS application_role_display_name,
       u.association_id                             AS association_id,
       u.church_id                                  AS branch_id,
       u.church_name                                AS branch_name,
       u.display_name                               AS display_name,
       u.enabled                                    AS enabled,
       u.gender                                     AS gender,
       u.main_church_id                             AS main_branch_id,
       u.parent_church_id                           AS parent_branch_id,
       u.picture_id                                 AS picture_id,
       p.thumbnail_url                              AS picture_thumbnail_url,
       u.role_id                                    AS role_id,
       u.role_name                                  AS role_name,
       u.username                                   AS username
FROM zionusa_management_api.dbo.users_display_view u
         JOIN zionusa_management_api.dbo.user_application_roles ua ON ua.user_id = u.id
         JOIN zionusa_events_api.dbo.applications_roles_view ar ON ar.id = ua.application_role_id
         LEFT JOIN zionusa_management_api.dbo.user_pictures p ON p.id = u.picture_id
go
