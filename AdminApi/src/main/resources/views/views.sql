------------------------------------------------------------------------------------------------------------------------
----- SELECT DATABASE (if necessary) -----------------------------------------------------------------------------------
------------------------------------------------------------------------------------------------------------------------

use admin_api

------------------------------------------------------------------------------------------------------------------------
----- DROP VIEWS -------------------------------------------------------------------------------------------------------
------------------------------------------------------------------------------------------------------------------------

-- (27)
BEGIN TRY
  DROP VIEW IF EXISTS dbo.movements_main_branch_baptisms_leader_input_view
END TRY
BEGIN CATCH
  DROP TABLE IF EXISTS dbo.movements_main_branch_baptisms_leader_input_view
END CATCH
GO

-- (26)
BEGIN TRY
  DROP VIEW IF EXISTS dbo.movements_branch_baptisms_leader_input_view
END TRY
BEGIN CATCH
  DROP TABLE IF EXISTS dbo.movements_branch_baptisms_leader_input_view
END CATCH
GO

-- (25)
BEGIN TRY
  DROP VIEW IF EXISTS dbo.movements_goals_results_church_view
END TRY
BEGIN CATCH
  DROP TABLE IF EXISTS dbo.movements_goals_results_church_view
END CATCH
GO

-- (24)
BEGIN TRY
  DROP VIEW IF EXISTS dbo.movements_goals_results_overseer_view
END TRY
BEGIN CATCH
  DROP TABLE IF EXISTS dbo.movements_goals_results_overseer_view
END CATCH
GO

-- (23)
BEGIN TRY
  DROP VIEW IF EXISTS dbo.movements_goals_results_association_view
END TRY
BEGIN CATCH
  DROP TABLE IF EXISTS dbo.movements_goals_results_association_view
END CATCH
GO

-- (22)
BEGIN TRY
  DROP VIEW IF EXISTS dbo.movements_goals_results_view
END TRY
BEGIN CATCH
  DROP TABLE IF EXISTS dbo.movements_goals_results_view
END CATCH
GO

-- (21)
BEGIN TRY
  DROP VIEW IF EXISTS dbo.movements_goals_short_term_view
END TRY
BEGIN CATCH
  DROP TABLE IF EXISTS dbo.movements_goals_short_term_view
END CATCH
GO

-- (20)
BEGIN TRY
  DROP VIEW IF EXISTS dbo.movements_goals_church_view
END TRY
BEGIN CATCH
  DROP TABLE IF EXISTS dbo.movements_goals_church_view
END CATCH
GO

-- (19)
BEGIN TRY
  DROP VIEW IF EXISTS dbo.movements_goals_overseer_view
END TRY
BEGIN CATCH
  DROP TABLE IF EXISTS dbo.movements_goals_overseer_view
END CATCH
GO

-- (18)
BEGIN TRY
  DROP VIEW IF EXISTS dbo.movements_goals_association_view
END TRY
BEGIN CATCH
  DROP TABLE IF EXISTS dbo.movements_goals_association_view
END CATCH
GO

-- (17)
BEGIN TRY
  DROP VIEW IF EXISTS dbo.movements_goals_view
END TRY
BEGIN CATCH
  DROP TABLE IF EXISTS dbo.movements_goals_view
END CATCH
GO

-- (16)
BEGIN TRY
  DROP VIEW IF EXISTS dbo.user_monthly_challenge_points
END TRY
BEGIN CATCH
  DROP TABLE IF EXISTS dbo.user_monthly_challenge_points
END CATCH
GO

-- (15)
BEGIN TRY
  DROP VIEW IF EXISTS dbo.group_monthly_challenge_points
END TRY
BEGIN CATCH
  DROP TABLE IF EXISTS dbo.group_monthly_challenge_points
END CATCH
GO

-- (14)
BEGIN TRY
  DROP VIEW IF EXISTS dbo.activities_special_view
END TRY
BEGIN CATCH
  DROP TABLE IF EXISTS dbo.activities_special_view
END CATCH
GO

-- (13)
BEGIN TRY
  DROP VIEW IF EXISTS dbo.activities_special_view_materialized
END TRY
BEGIN CATCH
  DROP TABLE IF EXISTS dbo.activities_special_view_materialized
END CATCH
GO

-- (12)
BEGIN TRY
  DROP VIEW IF EXISTS dbo.activities_group_report_view
END TRY
BEGIN CATCH
  DROP TABLE IF EXISTS dbo.activities_group_report_view
END CATCH
GO

-- (11)
BEGIN TRY
  DROP VIEW IF EXISTS dbo.activities_group_report_view_materialized
END TRY
BEGIN CATCH
  DROP TABLE IF EXISTS dbo.activities_group_report_view_materialized
END CATCH
GO

-- (10)
BEGIN TRY
  DROP VIEW IF EXISTS dbo.activities_church_report_view
END TRY
BEGIN CATCH
  DROP TABLE IF EXISTS dbo.activities_church_report_view
END CATCH
GO

-- (9)
BEGIN TRY
  DROP VIEW IF EXISTS dbo.activities_church_report_view_materialized
END TRY
BEGIN CATCH
  DROP TABLE IF EXISTS dbo.activities_church_report_view_materialized
END CATCH
GO

-- (8)
BEGIN TRY
  DROP VIEW IF EXISTS dbo.activities_group_log_view
END TRY
BEGIN CATCH
  DROP TABLE IF EXISTS dbo.activities_group_log_view
END CATCH
GO

-- (7)
BEGIN TRY
  DROP VIEW IF EXISTS dbo.activities_church_log_view
END TRY
BEGIN CATCH
  DROP TABLE IF EXISTS dbo.activities_church_log_view
END CATCH
GO

-- (6)
BEGIN TRY
  DROP VIEW IF EXISTS dbo.activities_categories_view
END TRY
BEGIN CATCH
  DROP TABLE IF EXISTS dbo.activities_categories_view
END CATCH
GO

-- (5)
BEGIN TRY
  DROP VIEW IF EXISTS dbo.challenges_activity
END TRY
BEGIN CATCH
  DROP TABLE IF EXISTS dbo.challenges_activity
END CATCH
GO

-- (4)
BEGIN TRY
  DROP VIEW IF EXISTS dbo.branch_view
END TRY
BEGIN CATCH
  DROP TABLE IF EXISTS dbo.branch_view
END CATCH
GO

-- (3)
BEGIN TRY
  DROP VIEW IF EXISTS dbo.users_application_role_user_permission_view
END TRY
BEGIN CATCH
  DROP TABLE IF EXISTS dbo.users_application_role_user_permission_view
END CATCH
GO

-- (2)
BEGIN TRY
  DROP VIEW IF EXISTS dbo.special_days_view
END TRY
BEGIN CATCH
  DROP TABLE IF EXISTS dbo.special_days_view
END CATCH
GO

-- (1)
BEGIN TRY
  DROP VIEW IF EXISTS dbo.applications
END TRY
BEGIN CATCH
  DROP TABLE IF EXISTS dbo.applications
END CATCH
GO

------------------------------------------------------------------------------------------------------------------------
----- (1) APPLICATIONS (FROM management_api) ---------------------------------------------------------------------------
------------------------------------------------------------------------------------------------------------------------

CREATE VIEW dbo.applications AS
SELECT id,
       apple_app_store_url,
       archived,
       description,
       enabled,
       google_play_store_url,
       hidden,
       icon_title,
       icon_url,
       launch_url,
       name,
       unique_id
FROM zionusa_management_api.dbo.applications a
GO

------------------------------------------------------------------------------------------------------------------------
----- (2) SPECIAL DAYS (FROM management_api) ---------------------------------------------------------------------------
------------------------------------------------------------------------------------------------------------------------

CREATE VIEW special_days_view AS
SELECT s.id          AS id,
       s.date        AS date,
       s.name        AS name
FROM zionusa_management_api.dbo.special_days s
GO

------------------------------------------------------------------------------------------------------------------------
----- (3) USERS APPLICATION ROLE PERMISSION VIEW (FROM management_api) -------------------------------------------------
------------------------------------------------------------------------------------------------------------------------

CREATE VIEW dbo.users_application_role_user_permission_view AS
SELECT arp.id,
       arp.archived,
       arp.hidden,
       arp.user_application_role_id,
       arp.user_application_role_archived,
       arp.user_application_role_hidden,
       arp.user_application_role_display_name,
       arp.user_application_role_description,
       arp.user_application_role_name,
       arp.user_permission_id,
       arp.user_permission_archived,
       arp.user_permission_hidden,
       arp.user_permission_display_name,
       arp.user_permission_description,
       arp.user_permission_name
FROM zionusa_management_api.dbo.users_application_role_user_permission_view arp
GO

------------------------------------------------------------------------------------------------------------------------
----- (4) BRANCH VIEW --------------------------------------------------------------------------------------------------
------------------------------------------------------------------------------------------------------------------------

CREATE VIEW dbo.branch_view AS
SELECT c.id,
       c.archived,
       c.association_id,
       c.association_name,
       c.hidden,
       c.main_church_id    AS main_branch_id,
       c.name,
       c.display_name,
       c.leader_id,
       c.leader_two_id,
       c.state_id
FROM zionusa_management_api.dbo.church_display_view c
WHERE c.archived = 0
GO

------------------------------------------------------------------------------------------------------------------------
----- (5) CHALLENGES ACTIVITY ------------------------------------------------------------------------------------------
------------------------------------------------------------------------------------------------------------------------

CREATE VIEW challenges_activity AS
SELECT c.id   AS challenge_id,
       c.name AS challenge_name,
       a.id   AS activity_id,
       a.name AS activity_name
FROM dbo.challenges c
    JOIN dbo.challenges_activities ca ON c.id = ca.challenge_id
    JOIN dbo.activities a ON ca.activities_id = a.id
GO

------------------------------------------------------------------------------------------------------------------------
----- (6) ACTIVITIES CHURCH LOG (materialized for performance) ---------------------------------------------------------
------------------------------------------------------------------------------------------------------------------------

CREATE VIEW dbo.activities_church_log_view WITH SCHEMABINDING AS
SELECT l.id                         AS id,
       a.id                         AS activity_id,
       a.abbreviation               AS activity_abbreviation,
       a.approved                   AS activity_approved,
       a.approver_id                AS activity_approver_id,
       -- activity_approver_name (linked)
       a.archived                   AS activity_archived,
       a.attachment_url             AS activity_attachment_url,
       a.background_color           AS activity_background_color,
       a.background_url             AS activity_background_url,
       a.church_id                  AS activity_church_id,
       -- activity_church_name (linked)
       a.date                       AS activity_date,
       a.editor_id                  AS activity_editor_id,
       -- activity_editor_name (linked)
       a.description                AS activity_description,
       a.feedback                   AS activity_feedback,
       a.group_id                   AS activity_group_id,
       a.icon_url                   AS activity_icon_url,
       a.link                       AS activity_link,
       a.logo_url                   AS activity_logo_url,
       a.name                       AS activity_name,
       a.notification_date_and_time AS activity_notification_date_and_time,
       a.object_id                  AS activity_object_id,
       a.published                  AS activity_published,
       a.published_date             AS activity_published_date,
       a.notes                      AS activity_notes,
       a.points                     AS activity_points,
       a.requester_id               AS activity_requester_id,
       -- activity_requester_image_url (linked)
       -- activity_requester_name (linked)
       a.tag                        AS activity_tag,
       a.team_id                    AS activity_team_id,
       a.text_color                 AS activity_text_color,
       a.use_for_notifications      AS activity_use_for_notifications,
       l.archived                   AS archived,
       c.abbreviation               AS category_abbreviation,
       c.background_color           AS category_background_color,
       c.description                AS category_description,
       c.id                         AS category_id,
       c.icon_url                   AS category_icon_url,
       c.name                       AS category_name,
       c.text_color                 AS category_text_color,
       l.notes                      AS notes,
       l.hidden                     AS hidden,
       l.order_id                   AS order_id,
       l.participant_count          AS participant_count,
       p.church_id                  AS plan_church_id,
       p.close_time                 AS plan_close_time,
       p.date                       AS plan_date,
       p.id                         AS plan_id,
       p.notes                      AS plan_notes,
       p.open_time                  AS plan_open_time,
       p.submitted                  AS plan_submitted,
       l.time_of_day                AS time_of_day,
       l.weight                     AS weight

FROM dbo.activities_church_logs l
         JOIN dbo.activities_church_plans p ON l.church_plan_id = p.id
         JOIN dbo.activities a ON a.id = l.activity_id
         JOIN dbo.activities_categories c on a.category_id = c.id
WHERE a.approved = 1
  AND a.archived = 0
GO

CREATE unique clustered index IDX_PLAN_ID
    on dbo.activities_church_log_view (id)
GO

------------------------------------------------------------------------------------------------------------------------
----- (7) ACTIVITIES GROUP LOG (materialized for performance) ----------------------------------------------------------
------------------------------------------------------------------------------------------------------------------------

CREATE VIEW dbo.activities_group_log_view WITH SCHEMABINDING AS
SELECT l.id                         AS id,
       a.id                         AS activity_id,
       a.abbreviation               AS activity_abbreviation,
       a.approved                   AS activity_approved,
       a.approver_id                AS activity_approver_id,
       -- activity_approver_name linked
       a.archived                   AS activity_archived,
       a.attachment_url             AS activity_attachment_url,
       a.background_color           AS activity_background_color,
       a.background_url             AS activity_background_url,
       a.church_id                  AS activity_church_id,
       -- activity_church_name linked
       a.date                       AS activity_date,
       a.editor_id                  AS activity_editor_id,
       -- activity_editor_name
       a.description                AS activity_description,
       a.feedback                   AS activity_feedback,
       a.group_id                   AS activity_group_id,
       a.icon_url                   AS activity_icon_url,
       a.link                       AS activity_link,
       a.logo_url                   AS activity_logo_url,
       a.name                       AS activity_name,
       a.notification_date_and_time AS activity_notification_date_and_time,
       a.object_id                  AS activity_object_id,
       a.published                  AS activity_published,
       a.published_date             AS activity_published_date,
       a.notes                      AS activity_notes,
       a.points                     AS activity_points,
       a.requester_id               AS activity_requester_id,
       -- activity_requester_image_url
       -- activity_requester_name
       a.tag                        AS activity_tag,
       a.team_id                    AS activity_team_id,
       a.use_for_notifications      AS activity_use_for_notifications,
       l.archived                   AS archived,
       c.abbreviation               AS category_abbreviation,
       c.background_color           AS category_background_color,
       c.description                AS category_description,
       c.id                         AS category_id,
       c.icon_url                   AS category_icon_url,
       c.name                       AS category_name,
       c.text_color                 AS category_text_color,
       l.hidden                     AS hidden,
       l.notes                      AS notes,
       l.order_id                   AS order_id,
       l.participant_count          AS participant_count,
       p.church_id                  AS plan_church_id,
       p.group_id                   AS plan_group_id,
       p.date                       AS plan_date,
       p.id                         AS plan_id,
       p.notes                      AS plan_notes,
       p.submitted                  AS plan_submitted,
       l.time_of_day                AS time_of_day,
       l.weight                     AS weight
FROM dbo.activities_group_logs l
         JOIN dbo.activities_group_plans p ON l.group_plan_id = p.id
         JOIN dbo.activities a ON a.id = l.activity_id
         JOIN dbo.activities_categories c on a.category_id = c.id
WHERE a.approved = 1
  AND a.archived = 0
GO

CREATE unique clustered index IDX_PLAN_ID
    on dbo.activities_group_log_view (id)
GO

------------------------------------------------------------------------------------------------------------------------
----- (8) ACTIVITIES CATEGORIES (materialized for performance) ---------------------------------------------------------
------------------------------------------------------------------------------------------------------------------------

CREATE VIEW dbo.activities_categories_view WITH SCHEMABINDING AS
SELECT a.id                         AS id,
       a.abbreviation               AS abbreviation,
       a.approved                   AS approved,
       a.approver_id                AS approver_id,
       -- a.approver_name (linked)
       a.archived                   AS archived,
       a.attachment_url             AS attachment_url,
       a.background_color           AS background_color,
       a.background_url             AS background_url,
       c.abbreviation               AS category_abbreviation,
       c.background_color           AS category_background_color,
       a.church_id                  AS church_id,
       -- a.church_name (linked)
       a.date                       AS date,
       c.description                AS category_description,
       a.editor_id                  AS editor_id,
       -- editor_name (linked)
       c.hidden                     AS hidden,
       c.id                         AS category_id,
       c.icon_url                   AS category_icon_url,
       c.name                       AS category_name,
       c.text_color                 AS category_text_color,
       a.description                AS description,
       a.feedback                   AS feedback,
       a.group_id                   AS group_id,
       a.icon_url                   AS icon_url,
       a.link                       AS link,
       a.logo_url                   AS logo_url,
       a.name                       AS name,
       a.notification_date_and_time AS notification_date_and_time,
       a.object_id                  AS object_id,
       a.published                  AS published,
       a.published_date             AS published_date,
       a.notes                      AS notes,
       a.points                     AS points,
       a.requester_id               AS requester_id,
       -- requester_image_url (linked)
       -- requester_name (linked)
       a.tag                        AS tag,
       a.team_id                    AS team_id,
       a.text_color                 AS text_color,
       a.use_for_notifications      AS use_for_notifications
FROM dbo.activities a
         JOIN dbo.activities_categories c on a.category_id = c.id
WHERE a.approved = 1
  AND a.archived = 0
  AND c.hidden != 1
GO

CREATE unique clustered index IDX_ACTIVITY_ID
	on dbo.activities_categories_view (id)
GO

------------------------------------------------------------------------------------------------------------------------
----- (9) ACTIVITIES CHURCH REPORT VIEW MATERIALIZED (for performance) -------------------------------------------------
------------------------------------------------------------------------------------------------------------------------

CREATE VIEW dbo.activities_church_report_view_materialized WITH SCHEMABINDING AS
SELECT a.id                                AS activity_id,
       COUNT_BIG(*)                        AS activity_count,
       SUM(ISNULL(l.participant_count, 0)) AS participant_count,
       l.time_of_day                       AS activity_time_of_day,
       p.church_id                         AS plan_church_id,
       p.date                              AS plan_date
FROM dbo.activities_church_logs l
         JOIN dbo.activities_church_plans p ON l.church_plan_id = p.id
         JOIN dbo.activities a ON a.id = l.activity_id
WHERE a.approved = 1
  AND a.archived = 0
GROUP BY a.id, p.church_id, p.date, l.time_of_day
GO

CREATE unique clustered index IDX_ACTIVITY_ID_CHURCH_ID_DATE
	on dbo.activities_church_report_view_materialized (activity_id, activity_time_of_day, plan_church_id, plan_date)
GO

------------------------------------------------------------------------------------------------------------------------
----- (10) ACTIVITIES CHURCH REPORT VIEW --------------------------------------------------------------------------------
------------------------------------------------------------------------------------------------------------------------

CREATE VIEW dbo.activities_church_report_view AS
SELECT NEWID()                     AS id,
       a.abbreviation              AS activity_abbreviation,
       a.background_color          AS activity_background_color,
       v.activity_count            AS activity_count,
       v.activity_id               AS activity_id,
       a.icon_url                  AS activity_icon_url,
       a.name                      AS activity_name,
       v.participant_count         AS activity_participant_count,
       a.text_color                AS activity_text_color,
       v.activity_time_of_day      AS activity_time_of_day,
       CAST(0 AS BIT)              AS archived,
       a.category_abbreviation     AS category_abbreviation,
       a.category_background_color AS category_background_color,
       a.category_id               AS category_id,
       a.category_icon_url         AS category_icon_url,
       a.category_name             AS category_name,
       a.category_text_color       AS category_text_color,
       v.plan_church_id            AS church_id,
       c.name                      AS church_name,
       c.city                      AS church_city,
       c.state_abbrv               AS church_state_abbrv,
       c.type_id                   AS church_type_id,
       c.type                      AS church_type,
       CAST(0 AS BIT)              AS hidden,
       v.plan_date                 AS date
FROM dbo.activities_church_report_view_materialized v
         JOIN dbo.activities_categories_view a ON a.id = v.activity_id
         JOIN zionusa_management_api.dbo.church_display_view c ON c.id = v.plan_church_id
GO

------------------------------------------------------------------------------------------------------------------------
----- (11) ACTIVITIES GROUP REPORT VIEW MATERIALIZED (for performance) -------------------------------------------------
------------------------------------------------------------------------------------------------------------------------

CREATE VIEW dbo.activities_group_report_view_materialized WITH SCHEMABINDING AS
SELECT a.id                                AS activity_id,
       l.time_of_day                       AS activity_time_of_day,
       COUNT_BIG(*)                        AS activity_count,
       SUM(ISNULL(l.participant_count, 0)) AS participant_count,
       p.group_id                          AS plan_group_id,
       p.date                              AS plan_date
FROM dbo.activities_group_logs l
         JOIN dbo.activities_group_plans p ON l.group_plan_id = p.id
         JOIN dbo.activities a ON a.id = l.activity_id
WHERE a.approved = 1
  AND a.archived = 0
GROUP BY a.id, p.group_id, p.date, l.time_of_day
GO

CREATE unique clustered index IDX_ACTIVITY_ID_GROUP_ID_DATE
    on dbo.activities_group_report_view_materialized (activity_id, activity_time_of_day, plan_group_id, plan_date)
GO

------------------------------------------------------------------------------------------------------------------------
----- (12) ACTIVITIES GROUP REPORT VIEW --------------------------------------------------------------------------------
------------------------------------------------------------------------------------------------------------------------

CREATE VIEW dbo.activities_group_report_view AS
SELECT NEWID()                     AS id,
       a.abbreviation              AS activity_abbreviation,
       a.background_color          AS activity_background_color,
       v.activity_count            AS activity_count,
       v.activity_id               AS activity_id,
       v.activity_time_of_day      AS activity_time_of_day,
       a.icon_url                  AS activity_icon_url,
       a.name                      AS activity_name,
       v.participant_count         AS activity_participant_count,
       a.text_color                AS activity_text_color,
       CAST(0 AS BIT)              AS archived,
       a.category_abbreviation     AS category_abbreviation,
       a.category_background_color AS category_background_color,
       a.category_id               AS category_id,
       a.category_icon_url         AS category_icon_url,
       a.category_name             AS category_name,
       a.category_text_color       AS category_text_color,
       g.church_id                 AS church_id,
       v.plan_date                 AS date,
       v.plan_group_id             AS group_id,
       g.name                      AS group_name,
       CAST(0 AS BIT)              AS hidden
FROM dbo.activities_group_report_view_materialized v
         JOIN dbo.activities_categories_view a ON a.id = v.activity_id
         JOIN zionusa_management_api.dbo.group_display_view g ON g.id = v.plan_group_id
GO

------------------------------------------------------------------------------------------------------------------------
----- (13) ACTIVITIES SPECIAL VIEW MATERIALIZED (for performance) ------------------------------------------------------
------------------------------------------------------------------------------------------------------------------------

CREATE VIEW dbo.activities_special_view_materialized WITH SCHEMABINDING AS
SELECT a.id               AS id,
       a.archived         AS archived,
       c.abbreviation     AS category_abbreviation,
       c.background_color AS category_background_color,
       c.description      AS category_description,
       c.hidden           AS category_hidden,
       c.icon_url         AS category_icon_url,
       c.id               AS category_id,
       c.name             AS category_name,
       c.text_color       AS category_text_color,
       a.hidden           AS hidden,
       a.order_id         AS order_id,
       a.special_day_id   AS special_day_id,
       a.time_of_day      AS time_of_day,
       a.weight           AS weight
FROM dbo.activities_special a
         JOIN dbo.activities_categories c ON c.id = a.category_id
GO

CREATE unique clustered index IDX_ID
    on dbo.activities_special_view_materialized (id)
GO

------------------------------------------------------------------------------------------------------------------------
----- (14) ACTIVITIES SPECIAL VIEW  ------------------------------------------------------------------------------------
------------------------------------------------------------------------------------------------------------------------

CREATE VIEW dbo.activities_special_view AS
SELECT a.id                        AS id,
       a.archived                  AS archived,
       a.category_abbreviation     AS category_abbreviation,
       a.category_background_color AS category_background_color,
       a.category_description      AS category_description,
       a.category_hidden           AS category_hidden,
       a.category_id               AS category_id,
       a.category_icon_url         AS category_icon_url,
       a.category_name             AS category_name,
       a.category_text_color       AS category_text_color,
       s.date                      AS date,
       a.hidden                    AS hidden,
       s.name                      AS name,
       a.order_id                  AS order_id,
       a.special_day_id            AS special_day_id,
       a.time_of_day               AS time_of_day,
       a.weight                    AS weight
FROM dbo.activities_special_view_materialized a
         JOIN dbo.special_days_view s ON s.id = a.special_day_id
GO

------------------------------------------------------------------------------------------------------------------------
----- (15) GROUP MONTHLY CHALLENGE POINTS  -----------------------------------------------------------------------------
------------------------------------------------------------------------------------------------------------------------

CREATE VIEW dbo.group_monthly_challenge_points WITH SCHEMABINDING AS
SELECT al.group_id,
       CAST(0 AS BIT)                      AS archived,
       CAST(0 AS BIT)                      AS hidden,
       SUM(c.points * c.points_multiplier) AS points,
       MONTH(convert(date, cl.date, 120))  AS month,
       YEAR(convert(date, cl.date, 120))   AS year,
       COUNT_BIG(*)                        AS tmp
FROM dbo.challenge_logs cl join dbo.challenges c on cl.challenge_id = c.id
  join (SELECT top 1 group_id,user_id,challenge_log_id FROM dbo.activity_logs) al on cl.id = al.challenge_log_id
where cl.completed = 1
group by al.group_id, MONTH(convert(date, cl.date, 120)), YEAR(convert(date, cl.date, 120))
GO

------------------------------------------------------------------------------------------------------------------------
----- (16) USER MONTHLY CHALLENGE POINTS (materialized for performance)  -----------------------------------------------
------------------------------------------------------------------------------------------------------------------------

CREATE VIEW dbo.user_monthly_challenge_points WITH SCHEMABINDING AS
SELECT al.user_id,
       SUM(a.points)                      AS points,
       MONTH(convert(date, al.date, 120)) AS month,
       YEAR(convert(date, al.date, 120))  AS year,
       COUNT_BIG(*)                       AS tmp
FROM dbo.activity_logs al join dbo.activities a on al.activity_id = a.id
group by al.user_id, MONTH(convert(date, al.date, 120)), YEAR(convert(date, al.date, 120))
GO

CREATE unique clustered index IDX_V1
    on dbo.user_monthly_challenge_points (user_id, month, year)
GO

------------------------------------------------------------------------------------------------------------------------
----- (17) MOVEMENTS GOALS VIEW (materialized for performance) ---------------------------------------------------------
------------------------------------------------------------------------------------------------------------------------

CREATE VIEW dbo.movements_goals_view WITH SCHEMABINDING AS
SELECT g.id                                                               AS id,
       g.archived                                                         AS archived,
       g.hidden                                                           AS hidden,
       g.reference_id                                                     AS reference_id,
       g.reference_type_id                                                AS reference_type_id,
       t.archived                                                         AS movement_goal_type_archived,
       t.hidden                                                           AS movement_goal_type_hidden,
       t.id                                                               AS movement_goal_type_id,
       t.name                                                             AS movement_goal_type_name,
       m.archived                                                         AS movement_archived,
       m.hidden                                                           AS movement_hidden,
       m.id                                                               AS movement_id,
       m.name                                                             AS movement_name,
       m.start_date                                                       AS movement_start_date,
       m.end_date                                                         AS movement_end_date,
       IIF(g.participant_count IS NOT NULL, g.participant_count, 0)       AS participant_count,
       IIF(g.acquaintances IS NOT NULL, g.acquaintances, 0)               AS acquaintances,
       IIF(g.activity_boards IS NOT NULL, g.activity_boards, 0)           AS activity_boards,
       IIF(g.activity_count IS NOT NULL, g.activity_count, 0)             AS activity_count,
       IIF(g.activity_points IS NOT NULL, g.activity_points, 0)           AS activity_points,
       IIF(g.contacts IS NOT NULL, g.contacts, 0)                         AS contacts,
       IIF(g.co_workers IS NOT NULL, g.co_workers, 0)                     AS co_workers,
       IIF(g.family IS NOT NULL, g.family, 0)                             AS family,
       IIF(g.friends IS NOT NULL, g.friends, 0)                           AS friends,
       IIF(g.meaningful IS NOT NULL, g.meaningful, 0)                     AS meaningful,
       IIF(g.fruit IS NOT NULL, g.fruit, 0)                               AS fruit,
       IIF(g.evangelists IS NOT NULL, g.evangelists, 0)                   AS evangelists,
       IIF(g.bible_studies IS NOT NULL, g.bible_studies, 0)               AS bible_studies,
       IIF(g.one_time_attendance IS NOT NULL, g.one_time_attendance, 0)   AS one_time_attendance,
       IIF(g.four_time_attendance IS NOT NULL, g.four_time_attendance, 0) AS four_time_attendance,
       IIF(g.practice_signatures IS NOT NULL, g.practice_signatures, 0)   AS practice_signatures,
       IIF(g.ga_signatures IS NOT NULL, g.ga_signatures, 0)               AS ga_signatures,
       IIF(g.neighbors IS NOT NULL, g.neighbors, 0)                       AS neighbors,
       IIF(g.preaching_total IS NOT NULL, g.preaching_total, 0)           AS preaching_total,
       IIF(g.ready_signatures IS NOT NULL, g.ready_signatures, 0)         AS ready_signatures,
       IIF(g.simple IS NOT NULL, g.simple, 0)                             AS simple,
       IIF(g.talents IS NOT NULL, g.talents, 0)                           AS talents,
       IIF(g.teaching_signatures IS NOT NULL, g.teaching_signatures, 0)   AS teaching_signatures,
       CAST(IIF(g.use_preaching_total_only IS NOT NULL, g.use_preaching_total_only,
                0) AS BIT)                                                AS use_preaching_total_only
FROM dbo.movements_goals g
         JOIN dbo.movements m ON g.movement_id = m.id
         JOIN dbo.movements_goals_types t on g.movement_goal_type_id = t.id
GO

CREATE unique clustered index UC_IDX_ID
    on dbo.movements_goals_view (id)
GO

------------------------------------------------------------------------------------------------------------------------
----- (18) MOVEMENTS GOALS ASSOCIATION VIEW ----------------------------------------------------------------------------
------------------------------------------------------------------------------------------------------------------------

CREATE VIEW dbo.movements_goals_association_view AS
SELECT g.id                       AS id,
       g.archived                 AS archived,
       g.hidden                   AS hidden,
       g.reference_id             AS reference_id,
       NULL                       AS reference_type_id,
       a.name                     AS name,
       g.movement_goal_type_id    AS movement_goal_type_id,
       g.movement_goal_type_name  AS movement_goal_type_name,
       g.movement_id              AS movement_id,
       g.movement_name            AS movement_name,
       g.movement_start_date      AS movement_start_date,
       g.movement_end_date        AS movement_end_date,
       g.participant_count        AS participant_count,
       g.acquaintances            AS acquaintances,
       g.activity_boards          AS activity_boards,
       g.activity_count           AS activity_count,
       g.activity_points          AS activity_points,
       g.bible_studies            AS bible_studies,
       g.contacts                 AS contacts,
       g.co_workers               AS co_workers,
       g.family                   AS family,
       g.friends                  AS friends,
       g.fruit                    AS fruit,
       g.one_time_attendance      AS one_time_attendance,
       g.four_time_attendance     AS four_time_attendance,
       g.practice_signatures      AS practice_signatures,
       g.ga_signatures            AS ga_signatures,
       g.meaningful               AS meaningful,
       g.neighbors                AS neighbors,
       g.preaching_total          AS preaching_total,
       g.ready_signatures         AS ready_signatures,
       g.simple                   AS simple,
       g.talents                  AS talents,
       g.teaching_signatures      AS teaching_signatures,
       g.use_preaching_total_only AS use_preaching_total_only
FROM dbo.movements_goals_view g
         JOIN zionusa_management_api.dbo.associations a ON g.reference_id = a.id
WHERE g.movement_goal_type_id = 6
GO

------------------------------------------------------------------------------------------------------------------------
----- (19) MOVEMENTS GOALS OVERSEER VIEW -------------------------------------------------------------------------------
------------------------------------------------------------------------------------------------------------------------

CREATE VIEW dbo.movements_goals_overseer_view AS
SELECT g.id                       AS id,
       g.archived                 AS archived,
       g.hidden                   AS hidden,
       g.reference_id             AS reference_id,
       NULL                       AS reference_type_id,
       c.name                     AS name,
       g.movement_goal_type_id    AS movement_goal_type_id,
       g.movement_goal_type_name  AS movement_goal_type_name,
       g.movement_id              AS movement_id,
       g.movement_name            AS movement_name,
       g.movement_start_date      AS movement_start_date,
       g.movement_end_date        AS movement_end_date,
       g.participant_count        AS participant_count,
       g.acquaintances            AS acquaintances,
       g.activity_boards          AS activity_boards,
       g.activity_count           AS activity_count,
       g.activity_points          AS activity_points,
       g.bible_studies            AS bible_studies,
       g.contacts                 AS contacts,
       g.co_workers               AS co_workers,
       g.family                   AS family,
       g.friends                  AS friends,
       g.fruit                    AS fruit,
       g.evangelists              AS evangelists,
       g.one_time_attendance      AS one_time_attendance,
       g.four_time_attendance     AS four_time_attendance,
       g.practice_signatures      AS practice_signatures,
       g.ga_signatures            AS ga_signatures,
       g.meaningful               AS meaningful,
       g.neighbors                AS neighbors,
       g.preaching_total          AS preaching_total,
       g.ready_signatures         AS ready_signatures,
       g.simple                   AS simple,
       g.talents                  AS talents,
       g.teaching_signatures      AS teaching_signatures,
       g.use_preaching_total_only AS use_preaching_total_only
FROM dbo.movements_goals_view g
         JOIN zionusa_management_api.dbo.church_display_view c ON g.reference_id = c.id
WHERE g.movement_goal_type_id = 5
GO

------------------------------------------------------------------------------------------------------------------------
----- (20) MOVEMENTS GOALS CHURCH VIEW ---------------------------------------------------------------------------------
------------------------------------------------------------------------------------------------------------------------

CREATE VIEW dbo.movements_goals_church_view AS
SELECT g.id                       AS id,
       g.archived                 AS archived,
       g.hidden                   AS hidden,
       g.reference_id             AS reference_id,
       NULL                       AS reference_type_id,
       c.name                     AS name,
       g.movement_goal_type_id    AS movement_goal_type_id,
       g.movement_goal_type_name  AS movement_goal_type_name,
       g.movement_id              AS movement_id,
       g.movement_name            AS movement_name,
       g.movement_start_date      AS movement_start_date,
       g.movement_end_date        AS movement_end_date,
       g.participant_count        AS participant_count,
       g.acquaintances            AS acquaintances,
       g.activity_boards          AS activity_boards,
       g.activity_count           AS activity_count,
       g.activity_points          AS activity_points,
       g.bible_studies            AS bible_studies,
       g.contacts                 AS contacts,
       g.co_workers               AS co_workers,
       g.evangelists              AS evangelists,
       g.family                   AS family,
       g.friends                  AS friends,
       g.fruit                    AS fruit,
       g.one_time_attendance      AS one_time_attendance,
       g.four_time_attendance     AS four_time_attendance,
       g.practice_signatures      AS practice_signatures,
       g.ga_signatures            AS ga_signatures,
       g.meaningful               AS meaningful,
       g.neighbors                AS neighbors,
       g.preaching_total          AS preaching_total,
       g.ready_signatures         AS ready_signatures,
       g.simple                   AS simple,
       g.talents                  AS talents,
       g.teaching_signatures      AS teaching_signatures,
       g.use_preaching_total_only AS use_preaching_total_only
FROM dbo.movements_goals_view g
         JOIN zionusa_management_api.dbo.church_display_view c ON g.reference_id = c.id
WHERE g.movement_goal_type_id = 4
GO

------------------------------------------------------------------------------------------------------------------------
----- (21) MOVEMENTS GOALS SHORT-TERM VIEW -----------------------------------------------------------------------------
------------------------------------------------------------------------------------------------------------------------

CREATE VIEW dbo.movements_goals_short_term_view AS
SELECT s.id                       AS id,
       g.archived                 AS archived,
       g.hidden                   AS hidden,
       g.reference_id             AS reference_id,
       NULL                       AS reference_type_id,
       s.name                     AS name,
       g.movement_goal_type_id    AS movement_goal_type_id,
       g.movement_goal_type_name  AS movement_goal_type_name,
       g.movement_id              AS movement_id,
       g.movement_name            AS movement_name,
       s.start_date               AS movement_start_date,
       s.end_date                 AS movement_end_date,
       g.participant_count        AS participant_count,
       g.acquaintances            AS acquaintances,
       g.activity_boards          AS activity_boards,
       g.activity_count           AS activity_count,
       g.activity_points          AS activity_points,
       g.bible_studies            AS bible_studies,
       g.contacts                 AS contacts,
       g.co_workers               AS co_workers,
       g.evangelists              AS evangelists,
       g.family                   AS family,
       g.friends                  AS friends,
       g.fruit                    AS fruit,
       g.one_time_attendance      AS one_time_attendance,
       g.four_time_attendance     AS four_time_attendance,
       g.practice_signatures      AS practice_signatures,
       g.ga_signatures            AS ga_signatures,
       g.meaningful               AS meaningful,
       g.neighbors                AS neighbors,
       g.preaching_total          AS preaching_total,
       g.ready_signatures         AS ready_signatures,
       g.simple                   AS simple,
       g.talents                  AS talents,
       g.teaching_signatures      AS teaching_signatures,
       g.use_preaching_total_only AS use_preaching_total_only
FROM dbo.movements_goals_view g
         JOIN zionusa_management_api.dbo.short_term_preaching s ON g.reference_id = s.id
WHERE g.movement_goal_type_id = 8
GO

------------------------------------------------------------------------------------------------------------------------
----- (22) MOVEMENTS GOALS RESULTS VIEW (materialized for performance) -------------------------------------------------
------------------------------------------------------------------------------------------------------------------------

CREATE VIEW dbo.movements_goals_results_view WITH SCHEMABINDING AS
SELECT g.id                                                               AS id,
       g.archived                                                         AS archived,
       g.hidden                                                           AS hidden,
       g.reference_id                                                     AS reference_id,
       t.archived                                                         AS movement_goal_type_archived,
       t.hidden                                                           AS movement_goal_type_hidden,
       t.id                                                               AS movement_goal_type_id,
       t.name                                                             AS movement_goal_type_name,
       m.archived                                                         AS movement_archived,
       m.hidden                                                           AS movement_hidden,
       m.id                                                               AS movement_id,
       m.name                                                             AS movement_name,
       m.start_date                                                       AS movement_start_date,
       m.end_date                                                         AS movement_end_date,
       IIF(g.participant_count IS NOT NULL, g.participant_count, 0)       AS participant_count,
       IIF(g.acquaintances IS NOT NULL, g.acquaintances, 0)               AS acquaintances,
       IIF(g.activity_boards IS NOT NULL, g.activity_boards, 0)           AS activity_boards,
       IIF(g.activity_count IS NOT NULL, g.activity_count, 0)             AS activity_count,
       IIF(g.activity_points IS NOT NULL, g.activity_points, 0)           AS activity_points,
       IIF(g.contacts IS NOT NULL, g.contacts, 0)                         AS contacts,
       IIF(g.co_workers IS NOT NULL, g.co_workers, 0)                     AS co_workers,
       IIF(g.family IS NOT NULL, g.family, 0)                             AS family,
       IIF(g.friends IS NOT NULL, g.friends, 0)                           AS friends,
       IIF(g.meaningful IS NOT NULL, g.meaningful, 0)                     AS meaningful,
       IIF(g.fruit IS NOT NULL, g.fruit, 0)                               AS fruit,
       IIF(g.evangelists IS NOT NULL, g.evangelists , 0)                  AS evangelists,
       IIF(g.bible_studies IS NOT NULL, g.bible_studies, 0)               AS bible_studies,
       IIF(g.one_time_attendance IS NOT NULL, g.one_time_attendance, 0)   AS one_time_attendance,
       IIF(g.four_time_attendance IS NOT NULL, g.four_time_attendance, 0) AS four_time_attendance,
       IIF(g.practice_signatures IS NOT NULL, g.practice_signatures, 0)   AS practice_signatures,
       IIF(g.ga_signatures IS NOT NULL, g.ga_signatures, 0)               AS ga_signatures,
       IIF(g.neighbors IS NOT NULL, g.neighbors, 0)                       AS neighbors,
       IIF(g.preaching_total IS NOT NULL, g.preaching_total, 0)           AS preaching_total,
       IIF(g.ready_signatures IS NOT NULL, g.ready_signatures, 0)         AS ready_signatures,
       IIF(g.simple IS NOT NULL, g.simple, 0)                             AS simple,
       IIF(g.talents IS NOT NULL, g.talents, 0)                           AS talents,
       IIF(g.teaching_signatures IS NOT NULL, g.teaching_signatures, 0)   AS teaching_signatures,
       CAST(IIF(g.use_preaching_total_only IS NOT NULL, g.use_preaching_total_only,
                0) AS BIT)                                                AS use_preaching_total_only
FROM dbo.movements_goals_results g
         JOIN dbo.movements m ON g.movement_id = m.id
         JOIN dbo.movements_goals_types t on g.movement_goal_type_id = t.id
GO

CREATE unique clustered index UC_IDX_ID
    on dbo.movements_goals_results_view (id)
GO

------------------------------------------------------------------------------------------------------------------------
----- (23) MOVEMENTS GOALS RESULTS ASSOCIATION VIEW --------------------------------------------------------------------
------------------------------------------------------------------------------------------------------------------------

CREATE VIEW dbo.movements_goals_results_association_view AS
SELECT g.id                       AS id,
       g.archived                 AS archived,
       g.hidden                   AS hidden,
       g.reference_id             AS reference_id,
       NULL                       AS reference_type_id,
       a.name                     AS name,
       g.movement_goal_type_id    AS movement_goal_type_id,
       g.movement_goal_type_name  AS movement_goal_type_name,
       g.movement_id              AS movement_id,
       g.movement_name            AS movement_name,
       g.movement_start_date      AS movement_start_date,
       g.movement_end_date        AS movement_end_date,
       g.participant_count        AS participant_count,
       g.acquaintances            AS acquaintances,
       g.activity_boards          AS activity_boards,
       g.activity_count           AS activity_count,
       g.activity_points          AS activity_points,
       g.bible_studies            AS bible_studies,
       g.contacts                 AS contacts,
       g.co_workers               AS co_workers,
       g.family                   AS family,
       g.evangelists              AS evangelists,
       g.friends                  AS friends,
       g.fruit                    AS fruit,
       g.one_time_attendance      AS one_time_attendance,
       g.four_time_attendance     AS four_time_attendance,
       g.practice_signatures      AS practice_signatures,
       g.ga_signatures            AS ga_signatures,
       g.meaningful               AS meaningful,
       g.neighbors                AS neighbors,
       g.preaching_total          AS preaching_total,
       g.ready_signatures         AS ready_signatures,
       g.simple                   AS simple,
       g.talents                  AS talents,
       g.teaching_signatures      AS teaching_signatures,
       g.use_preaching_total_only AS use_preaching_total_only
FROM dbo.movements_goals_results_view g
         JOIN zionusa_management_api.dbo.associations a ON g.reference_id = a.id
WHERE g.movement_goal_type_id = 6
GO

------------------------------------------------------------------------------------------------------------------------
----- (24) MOVEMENTS GOALS RESULTS OVERSEER VIEW -----------------------------------------------------------------------
------------------------------------------------------------------------------------------------------------------------

CREATE VIEW dbo.movements_goals_results_overseer_view AS
SELECT g.id                       AS id,
       g.archived                 AS archived,
       g.hidden                   AS hidden,
       g.reference_id             AS reference_id,
       NULL                       AS reference_type_id,
       c.name                     AS name,
       g.movement_goal_type_id    AS movement_goal_type_id,
       g.movement_goal_type_name  AS movement_goal_type_name,
       g.movement_id              AS movement_id,
       g.movement_name            AS movement_name,
       g.movement_start_date      AS movement_start_date,
       g.movement_end_date        AS movement_end_date,
       g.participant_count        AS participant_count,
       g.acquaintances            AS acquaintances,
       g.activity_boards          AS activity_boards,
       g.activity_count           AS activity_count,
       g.activity_points          AS activity_points,
       g.bible_studies            AS bible_studies,
       g.contacts                 AS contacts,
       g.co_workers               AS co_workers,
       g.family                   AS family,
       g.evangelists              AS evangelists,
       g.friends                  AS friends,
       g.fruit                    AS fruit,
       g.one_time_attendance      AS one_time_attendance,
       g.four_time_attendance     AS four_time_attendance,
       g.practice_signatures      AS practice_signatures,
       g.ga_signatures            AS ga_signatures,
       g.meaningful               AS meaningful,
       g.neighbors                AS neighbors,
       g.preaching_total          AS preaching_total,
       g.ready_signatures         AS ready_signatures,
       g.simple                   AS simple,
       g.talents                  AS talents,
       g.teaching_signatures      AS teaching_signatures,
       g.use_preaching_total_only AS use_preaching_total_only
FROM dbo.movements_goals_results_view g
         JOIN zionusa_management_api.dbo.church_display_view c ON g.reference_id = c.id
WHERE g.movement_goal_type_id = 5
GO

------------------------------------------------------------------------------------------------------------------------
----- (25) MOVEMENTS GOALS RESULTS CHURCH VIEW -------------------------------------------------------------------------
------------------------------------------------------------------------------------------------------------------------

CREATE VIEW dbo.movements_goals_results_church_view AS
SELECT g.id                       AS id,
       g.archived                 AS archived,
       g.hidden                   AS hidden,
       g.reference_id             AS reference_id,
       NULL                       AS reference_type_id,
       c.name                     AS name,
       g.movement_goal_type_id    AS movement_goal_type_id,
       g.movement_goal_type_name  AS movement_goal_type_name,
       g.movement_id              AS movement_id,
       g.movement_name            AS movement_name,
       g.movement_start_date      AS movement_start_date,
       g.movement_end_date        AS movement_end_date,
       g.participant_count        AS participant_count,
       g.acquaintances            AS acquaintances,
       g.activity_boards          AS activity_boards,
       g.activity_count           AS activity_count,
       g.activity_points          AS activity_points,
       g.bible_studies            AS bible_studies,
       g.contacts                 AS contacts,
       g.co_workers               AS co_workers,
       g.family                   AS family,
       g.evangelists              AS evangelists,
       g.friends                  AS friends,
       g.fruit                    AS fruit,
       g.one_time_attendance      AS one_time_attendance,
       g.four_time_attendance     AS four_time_attendance,
       g.practice_signatures      AS practice_signatures,
       g.ga_signatures            AS ga_signatures,
       g.meaningful               AS meaningful,
       g.neighbors                AS neighbors,
       g.preaching_total          AS preaching_total,
       g.ready_signatures         AS ready_signatures,
       g.simple                   AS simple,
       g.talents                  AS talents,
       g.teaching_signatures      AS teaching_signatures,
       g.use_preaching_total_only AS use_preaching_total_only
FROM dbo.movements_goals_results_view g
         JOIN zionusa_management_api.dbo.church_display_view c ON g.reference_id = c.id
WHERE g.movement_goal_type_id = 4
GO

------------------------------------------------------------------------------------------------------------------------
----- (26) MOVEMENTS BRANCH BAPTISMS LEADER INPUT VIEW -----------------------------------------------------------------
------------------------------------------------------------------------------------------------------------------------

CREATE VIEW dbo.movements_branch_baptisms_leader_input_view AS
SELECT c.id                                             AS branch_id,
       CAST(0 AS BIT)                                   AS archived,
       CONCAT_WS(', ', MAX(c.name), MAX(c.state_abbrv)) AS branch_name,
       SUM(m.baptisms)                                  AS baptisms,
       CAST(0 AS BIT)                                   AS hidden,
       m.movement_id                                    AS movement_id,
       MAX(c.association_id)                            AS association_id
FROM dbo.movements_branch_baptisms_leader_input m
         JOIN zionusa_management_api.dbo.church_display_view c ON m.branch_id = c.id
GROUP BY c.id, movement_id
GO

------------------------------------------------------------------------------------------------------------------------
----- (27) MOVEMENTS MAIN BRANCH BAPTISMS LEADER INPUT VIEW ------------------------------------------------------------
------------------------------------------------------------------------------------------------------------------------

CREATE VIEW dbo.movements_main_branch_baptisms_leader_input_view AS
SELECT c.id                                             AS branch_id,
       CAST(0 AS BIT)                                   AS archived,
       CONCAT_WS(', ', MAX(c.name), MAX(c.state_abbrv)) AS branch_name,
       SUM(m.baptisms)                                  AS baptisms,
       CAST(0 AS BIT)                                   AS hidden,
       m.movement_id                                    AS movement_id,
       MAX(c.association_id)                            AS association_id
FROM dbo.movements_main_branch_baptisms_leader_input m
         JOIN zionusa_management_api.dbo.church_display_view c ON m.branch_id = c.id
WHERE c.parent_church_id IS NULL
GROUP BY c.id, movement_id
GO
