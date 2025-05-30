------------------------------------------------------------------------------------------------------------------------
----- SELECT DATABASE (if necessary) -----------------------------------------------------------------------------------
------------------------------------------------------------------------------------------------------------------------

-- use management_api

------------------------------------------------------------------------------------------------------------------------
----- DROP VIEWS -------------------------------------------------------------------------------------------------------
------------------------------------------------------------------------------------------------------------------------
-- (14)
BEGIN TRY
  DROP VIEW IF EXISTS dbo.teams_church_team_view
END TRY
BEGIN CATCH
  DROP TABLE IF EXISTS dbo.teams_church_team_view
END CATCH
GO

-- (13)
BEGIN TRY
  DROP VIEW IF EXISTS dbo.team_display_view
END TRY
BEGIN CATCH
  DROP TABLE IF EXISTS dbo.team_display_view
END CATCH
GO

-- (12)
BEGIN TRY
  DROP VIEW IF EXISTS dbo.group_display_view
END TRY
BEGIN CATCH
  DROP TABLE IF EXISTS dbo.group_display_view
END CATCH
GO

-- (11a)
BEGIN TRY
  DROP VIEW IF EXISTS dbo.branches_view
END TRY
BEGIN CATCH
  DROP TABLE IF EXISTS dbo.branches_view
END CATCH
GO

-- (11b)
BEGIN TRY
  DROP VIEW IF EXISTS dbo.church_display_view
END TRY
BEGIN CATCH
  DROP TABLE IF EXISTS dbo.church_display_view
END CATCH
GO

-- (10a)
BEGIN TRY
  DROP VIEW IF EXISTS dbo.branches_view_materialized
END TRY
BEGIN CATCH
  DROP TABLE IF EXISTS dbo.branches_view_materialized
END CATCH
GO

-- (10b)
BEGIN TRY
  DROP VIEW IF EXISTS dbo.churches_materialized_view
END TRY
BEGIN CATCH
  DROP TABLE IF EXISTS dbo.churches_materialized_view
END CATCH
GO

-- (E2)
BEGIN TRY
  DROP VIEW IF EXISTS dbo.main_branches_view
END TRY
BEGIN CATCH
  DROP TABLE IF EXISTS dbo.main_branches_view
END CATCH
GO

-- (E1)
BEGIN TRY
  DROP VIEW IF EXISTS dbo.main_branches_view_materialized
END TRY
BEGIN CATCH
  DROP TABLE IF EXISTS dbo.main_branches_view_materialized
END CATCH
GO

-- (D1)
BEGIN TRY
  DROP VIEW IF EXISTS dbo.associations_view
END TRY
BEGIN CATCH
  DROP TABLE IF EXISTS dbo.associations_view
END CATCH
GO

-- (C2)
BEGIN TRY
  DROP VIEW IF EXISTS dbo.users_application_roles_materialized_view_2020_12
END TRY
BEGIN CATCH
  DROP TABLE IF EXISTS dbo.users_application_roles_materialized_view_2020_12
END CATCH
GO

-- (C1)
BEGIN TRY
  DROP VIEW IF EXISTS dbo.users_application_role_user_permission_view
END TRY
BEGIN CATCH
  DROP TABLE IF EXISTS dbo.users_application_role_user_permission_view
END CATCH
GO

-- (B8)
BEGIN TRY
  DROP VIEW IF EXISTS dbo.users_display_view
END TRY
BEGIN CATCH
  DROP TABLE IF EXISTS dbo.users_display_view
END CATCH
GO

-- (B7)
BEGIN TRY
  DROP VIEW IF EXISTS dbo.users_profile_search_view
END TRY
BEGIN CATCH
  DROP TABLE IF EXISTS dbo.users_profile_search_view
END CATCH
GO

-- (B6)
BEGIN TRY
  DROP VIEW IF EXISTS dbo.users_profile_search_software_view
END TRY
BEGIN CATCH
  DROP TABLE IF EXISTS dbo.users_profile_search_software_view
END CATCH
GO

-- (B5)
BEGIN TRY
  DROP VIEW IF EXISTS dbo.users_profile_search_professional_skills_view
END TRY
BEGIN CATCH
  DROP TABLE IF EXISTS dbo.users_profile_search_professional_skills_view
END CATCH
GO

-- (B4)
BEGIN TRY
  DROP VIEW IF EXISTS dbo.users_profile_search_languages_view
END TRY
BEGIN CATCH
  DROP TABLE IF EXISTS dbo.users_profile_search_languages_view
END CATCH
GO

-- (B3)
BEGIN TRY
  DROP VIEW IF EXISTS dbo.users_profile_search_branch_positions_view
END TRY
BEGIN CATCH
  DROP TABLE IF EXISTS dbo.users_profile_search_branch_positions_view
END CATCH
GO

-- (B2)
BEGIN TRY
  DROP VIEW IF EXISTS dbo.users_profile_view
END TRY
BEGIN CATCH
  DROP TABLE IF EXISTS dbo.users_profile_view
END CATCH
GO

-- (B1)
BEGIN TRY
  DROP VIEW IF EXISTS dbo.users_view_materialized
END TRY
BEGIN CATCH
  DROP TABLE IF EXISTS dbo.users_view_materialized
END CATCH
GO

-- (A1)
BEGIN TRY
  DROP VIEW IF EXISTS dbo.states_view
END TRY
BEGIN CATCH
  DROP TABLE IF EXISTS dbo.states_view
END CATCH
GO


------------------------------------------------------------------------------------------------------------------------
----- (A1) STATES_VIEW (materialized for query performance) -------------------------------------------------------------
------------------------------------------------------------------------------------------------------------------------

CREATE VIEW dbo.states_view WITH SCHEMABINDING as
SELECT s.id         AS id,
       s.archived   AS archived,
       c.archived   AS country_archived,
       c.full_name  AS country_full_name,
       c.hidden     AS country_hidden,
       s.country_id AS country_id,
       c.short_name AS country_short_name,
       s.hidden     AS hidden,
       s.full_name  AS full_name,
       s.short_name AS short_name
FROM dbo.states s
       JOIN dbo.countries c ON s.country_id = c.id
GO

CREATE UNIQUE CLUSTERED INDEX IDX_V1
  on dbo.states_view (id)
GO
------------------------------------------------------------------------------------------------------------------------
----- (B1) USERS VIEW MATERIALIZED (for query performance) -------------------------------------------------------------
------------------------------------------------------------------------------------------------------------------------

CREATE VIEW dbo.users_view_materialized WITH SCHEMABINDING as
SELECT u.id                                                                       AS id,
       u.access_id                                                                AS access_id,
       u.archived                                                                 AS archived,
       c.association_id                                                           AS association_id,
       c.archived                                                                 AS church_archived,
       c.hidden                                                                   AS church_hidden,
       c.id                                                                       AS church_id,
       c.leader_id                                                                AS church_leader_id,
       c.leader_two_id                                                            AS church_leader_two_id,
       c.name                                                                     AS church_name,
       c.state_id                                                                 AS church_state_id,
       s.full_name                                                                AS church_state_full_name,
       s.short_name                                                               AS church_state_short_name,
       CONCAT_WS(' ', ti.display_name, CONCAT_WS(' ', u.first_name, u.last_name)) AS display_name,
       CAST(IIF(
             u.archived = 0 AND
             u.enabled = 1 AND
             u.account_not_expired = 1 AND
             u.account_not_locked = 1 AND
             u.credentials_not_expired = 1
         , 1, 0) AS BIT)                                                          AS enabled,
       IIF(u.gender = 'M', 'Male', 'Female')                                      AS gender,
       g.archived                                                                 AS group_archived,
       g.hidden                                                                   AS group_hidden,
       t.group_id                                                                 AS group_id,
       g.leader_id                                                                AS group_leader_id,
--        g.leader_two_id                                                            AS group_leader_two_id,
       g.name                                                                     AS group_name,
       u.hidden                                                                   AS hidden,
       CAST(IIF(u.id = c.leader_id OR u.id = c.leader_two_id, 1, 0) AS BIT)       AS is_branch_leader,
--        CAST(IIF(u.id = g.leader_id OR u.id = g.leader_two_id, 1, 0) AS BIT)       AS is_group_leader,
--        CAST(IIF(u.id = t.leader_id OR u.id = t.leader_two_id, 1, 0) AS BIT)       AS is_team_leader,
       c.main_church_id                                                           AS main_church_id,
       c.parent_church_id                                                         AS parent_church_id,
       u.picture_id                                                               AS picture_id,
       r.archived                                                                 AS role_archived,
       r.hidden                                                                   AS role_hidden,
       u.role_id                                                                  AS role_id,
       r.name                                                                     AS role_name,
       t.archived                                                                 AS team_archived,
       t.hidden                                                                   AS team_hidden,
       u.team_id                                                                  AS team_id,
       t.leader_id                                                                AS team_leader_id,
--        t.leader_two_id                                                            AS leader_two_id,
       t.name                                                                     AS team_name,
       u.user_name                                                                AS username,
       u.last_login_date                                                          AS last_login_date,
       u.type_id                                                                  AS type_id,
       ty.name                                                                    AS type_name
FROM dbo.users u
       JOIN dbo.teams t ON u.team_id = t.id
       JOIN dbo.groups g ON t.group_id = g.id
       JOIN dbo.churches c ON g.church_id = c.id
       JOIN dbo.roles r ON u.role_id = r.id
       JOIN dbo.states s ON s.id = c.state_id
       JOIN dbo.titles ti ON u.title_id = ti.id
       JOIN dbo.users_types ty ON u.type_id = ty.id
GO

create unique clustered index IDX_V1
  on dbo.users_view_materialized (id)
GO

------------------------------------------------------------------------------------------------------------------------
----- (B2) USERS PROFILE VIEW (materialized view for query performance) ------------------------------------------------
------------------------------------------------------------------------------------------------------------------------

CREATE VIEW dbo.users_profile_view WITH SCHEMABINDING as
SELECT u.id                                                                       AS id,
       u.access_id                                                                AS access_id,
       u.archived                                                                 AS archived,
       c.association_id                                                           AS association_id,
       u.baptism_year                                                             AS baptism_year,
       u.birth_year                                                               AS birth_year,
       c.id                                                                       AS church_id,
       c.name                                                                     AS church_name,
       CONCAT_WS(' ', ti.display_name, CONCAT_WS(' ', u.first_name, u.last_name)) AS display_name,
       CAST(IIF(
             u.enabled = 1 AND
             u.account_not_expired = 1 AND
             u.account_not_locked = 1 AND
             u.credentials_not_expired = 1
         , 1, 0) AS BIT)                                                          AS enabled,
       u.first_name                                                               AS first_name,
       u.ga_grader                                                                AS ga_grader,
       IIF(u.gender = 'M', 'Male', 'Female')                                      AS gender,
       u.gospel_worker                                                            AS gospel_worker,
       t.group_id                                                                 AS group_id,
       g.name                                                                     AS group_name,
       u.hidden                                                                   AS hidden,
       u.last_login_date                                                          AS last_login_date,
       COALESCE(u.language_preference, 'en')                                      AS language_preference,
       u.last_name                                                                AS last_name,
       c.main_church_id                                                           AS main_church_id,
       u.married                                                                  AS married,
       u.middle_name                                                              AS middle_name,
       c.parent_church_id                                                         AS parent_church_id,
       u.picture_id                                                               AS picture_id,
       u.ready_grader                                                             AS ready_grader,
       u.role_id                                                                  AS role_id,
       u.spouse_id                                                                AS spouse_id,
       u.teacher                                                                  AS teacher,
       u.team_id                                                                  AS team_id,
       t.name                                                                     AS team_name,
       u.theological_student                                                      AS theological_student,
       u.title_id                                                                 AS title_id,
       u.user_name                                                                AS username,
       u.type_id                                                                  AS type_id,
       ty.name                                                                    AS type_name
FROM dbo.users u
       JOIN dbo.teams t ON u.team_id = t.id
       JOIN dbo.accesses a ON u.access_id = a.id
       JOIN dbo.groups g ON t.group_id = g.id
       JOIN dbo.churches c ON g.church_id = c.id
       JOIN dbo.roles r ON u.role_id = r.id
       JOIN dbo.titles ti ON u.title_id = ti.id
       JOIN dbo.users_types ty ON u.type_id = ty.id
go

create unique clustered index IDX_V1
  on dbo.users_profile_view (id)
go

------------------------------------------------------------------------------------------------------------------------
----- (B3) USERS PROFILE CHURCH POSITIONS ------------------------------------------------------------------------------
------------------------------------------------------------------------------------------------------------------------

CREATE VIEW dbo.users_profile_search_branch_positions_view WITH SCHEMABINDING as
SELECT i.id          AS id,
       i.information as information,
       i.profile_id  as profile_id,
       p.user_id     AS user_id
FROM dbo.user_profiles p
       JOIN dbo.user_profile_information i ON p.id = i.profile_id
       JOIN dbo.user_profile_categories c ON c.id = i.category_id
WHERE c.id = 2
go

create unique clustered index IDX_V1
  on dbo.users_profile_search_branch_positions_view (id)
go

------------------------------------------------------------------------------------------------------------------------
----- (B4) USERS PROFILE LANGUAGES -------------------------------------------------------------------------------------
------------------------------------------------------------------------------------------------------------------------

CREATE VIEW dbo.users_profile_search_languages_view WITH SCHEMABINDING as
SELECT i.id          AS id,
       i.information as information,
       i.profile_id  as profile_id,
       p.user_id     AS user_id
FROM dbo.user_profiles p
       JOIN dbo.user_profile_information i ON p.id = i.profile_id
       JOIN dbo.user_profile_categories c ON c.id = i.category_id
WHERE c.id = 11
go

create unique clustered index IDX_V1
  on dbo.users_profile_search_languages_view (id)
go

------------------------------------------------------------------------------------------------------------------------
----- (B5) USERS PROFILE PROFESSIONAL SKILLS ---------------------------------------------------------------------------
------------------------------------------------------------------------------------------------------------------------

CREATE VIEW dbo.users_profile_search_professional_skills_view WITH SCHEMABINDING as
SELECT i.id          AS id,
       i.information as information,
       i.profile_id  as profile_id,
       p.user_id     AS user_id
FROM dbo.user_profiles p
       JOIN dbo.user_profile_information i ON p.id = i.profile_id
       JOIN dbo.user_profile_categories c ON c.id = i.category_id
WHERE c.id = 4
go

create unique clustered index IDX_V1
  on dbo.users_profile_search_professional_skills_view (id)
go

------------------------------------------------------------------------------------------------------------------------
----- (B6) USERS PROFILE PROFESSIONAL SKILLS ---------------------------------------------------------------------------
------------------------------------------------------------------------------------------------------------------------

CREATE VIEW dbo.users_profile_search_software_view WITH SCHEMABINDING as
SELECT i.id          AS id,
       i.information as information,
       i.profile_id  as profile_id,
       p.user_id     AS user_id
FROM dbo.user_profiles p
       JOIN dbo.user_profile_information i ON p.id = i.profile_id
       JOIN dbo.user_profile_categories c ON c.id = i.category_id
WHERE c.id = 5
go

create unique clustered index IDX_V1
  on dbo.users_profile_search_software_view (id)
go

------------------------------------------------------------------------------------------------------------------------
----- (B7) USERS PROFILE SEARCH VIEW -----------------------------------------------------------------------------------
------------------------------------------------------------------------------------------------------------------------

CREATE VIEW dbo.users_profile_search_view as
SELECT u.id                                                              AS id,
       u.archived                                                        AS archived,
       IIF(u.birth_year IS NULL, -1, YEAR(GETDATE()) - u.birth_year)     AS age,
       u.church_id                                                       AS branch_id,        -- query branch_id, main_branch_id, parent_branch_id
       u.church_name                                                     AS branch_name,
       CAST(p.children AS BIT)                                           AS children,
       STRING_AGG(CAST(bp.information AS NVARCHAR(MAX)), ',')            AS branch_positions,
       IIF(p.city IS NULL, '', p.city)                                   AS city,
       IIF(p.company_city IS NULL, '', p.company_city)                   AS company_city,
       p.company_state_id                                                AS company_state_id,
       u.display_name                                                    AS display_name,
       u.enabled                                                         AS enabled,          -- query enabled is true
       p.ethnicity                                                       AS ethnicity,
       IIF(p.faith_level IS NULL, '', p.faith_level)                     AS faith_level,
       u.first_name                                                      AS first_name,
       IIF(u.gender = 'M', 'Male', 'Female')                             AS gender,
       u.gospel_worker                                                   AS gospel_worker,
       u.group_id                                                        AS group_id,
       u.hidden                                                          AS hidden,
       STRING_AGG(CAST(l.information AS NVARCHAR(MAX)), ',')             AS languages,
       u.last_name                                                       AS last_name,
       u.main_church_id                                                  AS main_branch_id,   -- query branch_id, main_branch_id, parent_branch_id
       u.married                                                         AS married,
       u.middle_name                                                     AS middle_name,
       u.parent_church_id                                                AS parent_branch_id, -- query branch_id, main_branch_id, parent_branch_id
       pic.thumbnail_url                                                 AS picture_url,
       STRING_AGG(CAST(ps.information AS NVARCHAR(MAX)), ',')            AS professional_skills,
       u.role_id                                                         AS role_id,
       p.short_term_participant                                          AS short_term_participant,
       STRING_AGG(CAST(s.information AS NVARCHAR(MAX)), ',')             AS software,
       IIF(p.baptism_year IS NULL, -1, YEAR(GETDATE()) - p.baptism_year) AS spiritual_age,
       u.spouse_id                                                       AS spouse_id,
       p.state_id                                                        AS state_id,
       u.team_id                                                         AS team_id,
       u.title_id                                                        AS title_id,
       u.username                                                        AS username,
       p.works_remotely                                                  AS works_remotely
FROM dbo.users_profile_view u
       LEFT JOIN dbo.user_profiles p ON u.id = p.user_id
       LEFT JOIN dbo.user_pictures pic ON u.id = pic.user_id
       LEFT JOIN dbo.users_profile_search_branch_positions_view bp on bp.user_id = u.id
       LEFT JOIN dbo.users_profile_search_languages_view l on l.user_id = u.id
       LEFT JOIN dbo.users_profile_search_professional_skills_view ps on ps.user_id = u.id
       LEFT JOIN dbo.users_profile_search_software_view s on s.user_id = u.id
group by u.id, u.archived, u.birth_year, u.church_id, u.church_name, p.children, p.city, p.company_city, p.company_state_id, u.display_name, u.enabled, p.ethnicity, p.faith_level, u.first_name, u.gender, u.gospel_worker, u.group_id, u.hidden, u.last_name, u.main_church_id, u.married, u.middle_name, p.baptism_year, u.parent_church_id, pic.thumbnail_url, u.role_id, p.short_term_participant, u.spouse_id, p.state_id, u.teacher, u.theological_student, u.team_id, u.title_id, u.type_id, u.type_name, p.works_remotely, u.username
go

------------------------------------------------------------------------------------------------------------------------
----- (B8) USERS DISPLAY VIEW (materialized view for query performance) -------------------------------------------------
------------------------------------------------------------------------------------------------------------------------

CREATE VIEW dbo.users_display_view WITH SCHEMABINDING as
SELECT u.id                                                                       AS id,
       u.access_id                                                                AS access_id,
       u.archived                                                                 AS archived,
       c.association_id                                                           AS association_id,
       a.archived                                                                 AS access_archived,
       a.hidden                                                                   AS access_hidden,
       a.name                                                                     AS access_name,
       c.archived                                                                 AS church_archived,
       c.hidden                                                                   AS church_hidden,
       c.id                                                                       AS church_id,
       c.leader_id                                                                AS church_leader_id,
       c.leader_two_id                                                            AS church_leader_two_id,
       c.name                                                                     AS church_name,
       CONCAT_WS(' ', ti.display_name, CONCAT_WS(' ', u.first_name, u.last_name)) AS display_name,
       CAST(IIF(
             u.archived = 0 AND
             u.enabled = 1 AND
             u.account_not_expired = 1 AND
             u.account_not_locked = 1 AND
             u.credentials_not_expired = 1
         , 1, 0) AS BIT)                                                          AS enabled,
       u.ga_grader                                                                AS ga_grader,
       IIF(u.gender = 'M', 'Male', 'Female')                                      AS gender,
       g.archived                                                                 AS group_archived,
       g.hidden                                                                   AS group_hidden,
       t.group_id                                                                 AS group_id,
       g.leader_id                                                                AS group_leader_id,
--        g.leader_two_id                                                            AS group_leader_two_id,
       g.name                                                                     AS group_name,
       u.hidden                                                                   AS hidden,
       c.main_church_id                                                           AS main_church_id,
       c.parent_church_id                                                         AS parent_church_id,
       u.picture_id,
       u.ready_grader                                                             AS ready_grader,
       r.archived                                                                 AS role_archived,
       r.hidden                                                                   AS role_hidden,
       u.role_id                                                                  AS role_id,
       r.name                                                                     AS role_name,
       u.teacher                                                                  AS teacher,
       t.archived                                                                 AS team_archived,
       t.hidden                                                                   AS team_hidden,
       u.team_id                                                                  AS team_id,
       t.leader_id                                                                AS team_leader_id,
--        t.leader_two_id                                                            AS leader_two_id,
       t.name                                                                     AS team_name,
       u.user_name                                                                AS username,
       u.last_login_date                                                          AS last_login_date,
       u.type_id                                                                  AS type_id,
       ty.name                                                                    AS type_name
FROM dbo.users u
       JOIN dbo.teams t ON u.team_id = t.id
       JOIN dbo.accesses a ON u.access_id = a.id
       JOIN dbo.groups g ON t.group_id = g.id
       JOIN dbo.churches c ON g.church_id = c.id
       JOIN dbo.roles r ON u.role_id = r.id
       JOIN dbo.titles ti ON u.title_id = ti.id
       JOIN dbo.users_types ty ON u.type_id = ty.id
go

create unique clustered index IDX_V1
  on dbo.users_display_view (id)
go

------------------------------------------------------------------------------------------------------------------------
----- (C1) USERS APPLICATION ROLE USER PERMISSION VIEW (materialized view for query performance) ------------------------
------------------------------------------------------------------------------------------------------------------------

CREATE VIEW dbo.users_application_role_user_permission_view WITH SCHEMABINDING as
SELECT arp.id          AS id,
       ar.archived     AS archived,
       ar.hidden       AS hidden,
       ar.id           AS user_application_role_id,
       ar.archived     AS user_application_role_archived,
       ar.hidden       AS user_application_role_hidden,
       ar.display_name AS user_application_role_display_name,
       ar.description  AS user_application_role_description,
       ar.name         AS user_application_role_name,
       up.id           AS user_permission_id,
       up.archived     AS user_permission_archived,
       up.hidden       AS user_permission_hidden,
       up.display_name AS user_permission_display_name,
       up.description  AS user_permission_description,
       up.name         AS user_permission_name
FROM dbo.application_roles ar
       JOIN dbo.application_role_permissions arp on ar.id = arp.application_role_id
       JOIN dbo.user_permissions up on arp.permission_id = up.id
go

create unique clustered index IDX_V1
  on dbo.users_application_role_user_permission_view (id)
go

------------------------------------------------------------------------------------------------------------------------
----- (C2) USERS APPLICATION ROLES MATERIALIZED (for query performance) -------------------------------------------------
------------------------------------------------------------------------------------------------------------------------

CREATE VIEW dbo.users_application_roles_materialized_view_2020_12 WITH SCHEMABINDING as
SELECT CONCAT_WS('-', u.id, ua.application_role_id)                               AS user_application_role_id,
       u.id                                                                       AS id,
       CONCAT_WS(' ', ti.display_name, CONCAT_WS(' ', u.first_name, u.last_name)) AS display_name,
       u.user_name                                                                AS user_name,
       u.picture_id                                                               AS picture_id,
       ar.display_name                                                            AS application_role_display_name,
       ua.application_role_id                                                     AS application_role_id,
       ar.name                                                                    AS application_role_name,
       ua.reference_id                                                            AS reference_id,
       c.id                                                                       AS church_id,
       c.name                                                                     AS church_name,
       c.city                                                                     AS church_city,
       s.full_name                                                                AS church_state,
       s.short_name                                                               AS church_short_state,
       c.main_church_id                                                           AS main_church_id,
       c.association_id                                                           AS association_id
FROM dbo.users u
       JOIN dbo.teams t ON u.team_id = t.id
       JOIN dbo.groups g ON t.group_id = g.id
       JOIN dbo.churches c ON g.church_id = c.id
       JOIN dbo.states s ON s.id = c.state_id
       JOIN dbo.associations a ON c.association_id = a.id
       JOIN dbo.titles ti ON u.title_id = ti.id
       JOIN dbo.user_application_roles ua ON u.id = ua.user_id
       JOIN dbo.application_roles ar ON ar.id = ua.application_role_id
go

create unique clustered index ucidx_userid_ar_id
  on dbo.users_application_roles_materialized_view_2020_12 (id, application_role_id)
go

------------------------------------------------------------------------------------------------------------------------
----- (D1) ASSOCIATIONS VIEW --------------------------------------------------------------------------------------------
------------------------------------------------------------------------------------------------------------------------

CREATE VIEW dbo.associations_view WITH SCHEMABINDING AS
SELECT a.id,
       a.archived,
       a.hidden,
       a.name,
       a.leader_id,
       COALESCE(l.display_name, '') AS leader_display_name,
       a.leader_two_id,
       COALESCE(t.display_name, '') AS leader_two_display_name,
       a.picture_file_id,
       a.thumbnail_file_id
FROM dbo.associations a
       LEFT JOIN dbo.users_display_view l ON a.leader_id = l.id
       LEFT JOIN dbo.users_display_view t ON a.leader_two_id = t.id
go

------------------------------------------------------------------------------------------------------------------------
----- (E1) MAIN BRANCHES VIEW MATERIALIZED (for query performance) -----------------------------------------------------
------------------------------------------------------------------------------------------------------------------------

CREATE VIEW dbo.main_branches_view_materialized WITH SCHEMABINDING AS
SELECT mb.id,
       mb.archived,
       mb.hidden,
       mb.name,
       mb.association_id,
       a.archived          AS association_archived,
       a.hidden            AS association_hidden,
       a.name              AS association_name,
       a.picture_file_id   AS association_picture_file_id,
       a.leader_id         AS association_leader_id,
       a.leader_two_id     AS association_leader_two_id,
       a.thumbnail_file_id AS association_thumbnail_file_id,
       mb.leader_id,
       mb.leader_two_id,
       mb.picture_file_id,
       mb.thumbnail_file_id
FROM dbo.main_branches mb
       JOIN dbo.associations a ON mb.association_id = a.id
go

create unique clustered index IDX_V1
  on dbo.main_branches_view_materialized (id)
go

------------------------------------------------------------------------------------------------------------------------
----- (E2) MAIN BRANCHES VIEW ------------------------------------------------------------------------------------------
------------------------------------------------------------------------------------------------------------------------

CREATE VIEW dbo.main_branches_view WITH SCHEMABINDING AS
SELECT mb.id,
       mb.archived,
       mb.hidden,
       mb.name,
       mb.picture_file_id,
       mb.thumbnail_file_id,
       mb.association_id,
       mb.association_archived,
       mb.association_hidden,
       mb.association_name,
       mb.association_picture_file_id,
       mb.association_leader_id,
       mb.association_leader_two_id,
       mb.association_thumbnail_file_id,
       mb.leader_id,
       COALESCE(l.display_name, '') AS leader_display_name,
       mb.leader_two_id,
       COALESCE(t.display_name, '') AS leader_two_display_name
FROM dbo.main_branches_view_materialized mb
       LEFT JOIN dbo.users_display_view l ON mb.leader_id = l.id
       LEFT JOIN dbo.users_display_view t ON mb.leader_two_id = t.id
go

------------------------------------------------------------------------------------------------------------------------
----- (10a) BRANCHES VIEW MATERIALIZED (for query performance) ----------------------------------------------------------
------------------------------------------------------------------------------------------------------------------------

CREATE VIEW dbo.branches_view_materialized WITH SCHEMABINDING AS
SELECT b.id,
       b.archived,
       b.hidden,
       b.address,
       b.city,
       CONCAT(CONCAT_WS(' (', CONCAT_WS(', ', b.name, s.short_name), t.name), ')') AS display_name,
       b.distant,
       b.latitude,
       b.longitude,
       b.leader_id,
       b.leader_two_id,
       b.name,
       b.parent_branch_id,
       b.picture_file_id,
       b.state_id,
       s.full_name                                                                 AS state_full_name,
       s.short_name                                                                AS state_short_name,
       b.type_id,
       t.name                                                                      AS type_name,
       b.thumbnail_file_id,
       mb.id                                                                       AS main_branch_id,
       mb.archived                                                                 AS main_branch_archived,
       mb.hidden                                                                   AS main_branch_hidden,
       mb.name                                                                     AS main_branch_name,
       mb.picture_file_id                                                          AS main_branch_picture_file_id,
       mb.leader_id                                                                AS main_branch_leader_id,
       mb.leader_two_id                                                            AS main_branch_leader_two_id,
       mb.thumbnail_file_id                                                        AS main_branch_thumbnail_file_id,
       mb.association_id                                                           AS association_id,
       a.archived                                                                  AS association_archived,
       a.hidden                                                                    AS association_hidden,
       a.name                                                                      AS association_name,
       a.picture_file_id                                                           AS association_picture_file_id,
       a.leader_id                                                                 AS association_leader_id,
       a.leader_two_id                                                             AS association_leader_two_id,
       a.thumbnail_file_id                                                         AS association_thumbnail_file_id
FROM dbo.branches b
       JOIN dbo.main_branches mb ON mb.id = b.main_branch_id
       JOIN dbo.associations a ON mb.association_id = a.id
       JOIN dbo.states s ON s.id = b.state_id
       JOIN dbo.branches_types t ON b.type_id = t.id
go

create unique clustered index IDX_V1
  on dbo.branches_view_materialized (id)
go

------------------------------------------------------------------------------------------------------------------------
----- (10b) CHURCHES VIEW MATERIALIZED (soon to be removed) -------------------------------------------------------------
------------------------------------------------------------------------------------------------------------------------

CREATE VIEW dbo.churches_materialized_view WITH SCHEMABINDING as
SELECT c.id,
       c.archived,
       c.hidden,
       c.main_church_id,
       c.parent_church_id,
       c.leader_id,
       c.leader_two_id,
       c.type_id,
       ct.name      AS type,
       c.state_id,
       s.full_name  AS state_name,
       s.short_name AS state_abbrv,
       c.name,
       c.address,
       c.phone,
       c.email,
       c.city,
       c.latitude,
       c.longitude,
       c.postal_code,
       c.hidden_location,
       c.association_id,
       a.name       AS association_name
FROM dbo.churches c
       JOIN dbo.church_types ct on c.type_id = ct.id
       JOIN dbo.associations a on a.id = c.association_id
       JOIN dbo.states s on c.state_id = s.id
go

create unique clustered index IDX_V1
  on dbo.churches_materialized_view (id)
go

------------------------------------------------------------------------------------------------------------------------
----- (11a) BRANCHES VIEW -----------------------------------------------------------------------------------------------
------------------------------------------------------------------------------------------------------------------------

CREATE VIEW dbo.branches_view WITH SCHEMABINDING AS
SELECT b.id,
       b.archived,
       b.hidden,
       b.address,
       b.city,
       b.display_name,
       b.distant,
       b.latitude,
       b.longitude,
       b.leader_id,
       COALESCE(l.display_name, '') AS leader_display_name,
       b.leader_two_id,
       COALESCE(t.display_name, '') AS leader_two_display_name,
       b.name,
       b.parent_branch_id,
       b.picture_file_id,
       b.state_id,
       b.state_full_name,
       b.state_short_name,
       b.type_id,
       b.type_name,
       b.thumbnail_file_id,
       b.main_branch_id,
       b.main_branch_archived,
       b.main_branch_hidden,
       b.main_branch_name,
       b.main_branch_picture_file_id,
       b.main_branch_leader_id,
       b.main_branch_leader_two_id,
       b.main_branch_thumbnail_file_id,
       b.association_id,
       b.association_archived,
       b.association_hidden,
       b.association_name,
       b.association_picture_file_id,
       b.association_leader_id,
       b.association_leader_two_id,
       b.association_thumbnail_file_id
FROM dbo.branches_view_materialized b
       LEFT JOIN dbo.users_display_view l ON b.leader_id = l.id
       LEFT JOIN dbo.users_display_view t ON b.leader_two_id = t.id
go

------------------------------------------------------------------------------------------------------------------------
----- (11b) CHURCHES DISPLAY VIEW (soon to be removed) ------------------------------------------------------------------
------------------------------------------------------------------------------------------------------------------------

CREATE VIEW dbo.church_display_view WITH SCHEMABINDING as
SELECT c.id,
       c.archived,
       c.hidden,
       c.main_church_id,
       c.parent_church_id,
       c.leader_id,
       l_one.display_name                                  AS leader_name,
       c.leader_two_id,
       l_two.display_name                                  AS leader_two_name,
       c.type,
       c.type_id,
       c.state_id,
       c.state_name,
       c.state_abbrv,
       CONCAT(CONCAT_WS(' (', c.name, c.state_abbrv), ')') as display_name,
       c.name,
       c.address,
       c.phone,
       c.email,
       c.city,
       c.latitude,
       c.longitude,
       c.postal_code,
       c.hidden_location,
       c.association_id,
       c.association_name
FROM dbo.churches_materialized_view c
       LEFT JOIN dbo.users_display_view l_one on l_one.id = c.leader_id
       LEFT JOIN dbo.users_display_view l_two on l_two.id = c.leader_two_id
go

------------------------------------------------------------------------------------------------------------------------
----- (12) GROUP DISPLAY VIEW (soon to be updated) ---------------------------------------------------------------------
------------------------------------------------------------------------------------------------------------------------

CREATE VIEW dbo.group_display_view WITH SCHEMABINDING as
SELECT g.id,
       g.church_id,
       g.leader_id,
       CONCAT_WS(' ', t.display_name, CONCAT_WS(' ', u.first_name, u.last_name)) AS leader_name,
       g.name,
       g.church_group,
       g.archived
FROM dbo.groups g
       LEFT JOIN dbo.users u on g.leader_id = u.id
       LEFT JOIN dbo.titles t on u.title_id = t.id
go

------------------------------------------------------------------------------------------------------------------------
----- (13) TEAM DISPLAY VIEW (soon to be updated) ----------------------------------------------------------------------
------------------------------------------------------------------------------------------------------------------------

CREATE VIEW dbo.team_display_view WITH SCHEMABINDING as
SELECT t.id,
       t.group_id,
       t.leader_id,
       CONCAT_WS(' ', ti.display_name, CONCAT_WS(' ', u.first_name, u.last_name)) AS leader_name,
       t.name,
       t.church_team,
       t.archived
FROM dbo.teams t
       LEFT JOIN dbo.users u on t.leader_id = u.id
       LEFT JOIN dbo.titles ti on u.title_id = t.id
go

------------------------------------------------------------------------------------------------------------------------
----- (14) TEAM CHURCH TEAM VIEW (soon to be updated) ------------------------------------------------------------------
------------------------------------------------------------------------------------------------------------------------

CREATE VIEW dbo.teams_church_team_view WITH SCHEMABINDING as
SELECT t.id               AS id,
       a.id               AS association_id,
       a.name             AS association_name,
       c.city             AS church_city,
       c.id               AS church_id,
       c.name             AS church_name,
       s.full_name        AS church_state_full_name,
       s.id               AS church_state_id,
       s.short_name       AS church_state_short_name,
       g.id               AS group_id,
       g.name             AS group_name,
       c.main_church_id   AS main_church_id,
       t.name             AS name,
       c.parent_church_id AS parent_church_id
FROM dbo.teams t
       JOIN dbo.groups g ON t.group_id = g.id
       JOIN dbo.churches c ON g.church_id = c.id
       JOIN dbo.associations a ON c.association_id = a.id
       JOIN dbo.states s ON c.state_id = s.id
WHERE t.church_team = 1
  AND t.archived = 0
go
