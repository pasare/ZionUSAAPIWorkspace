------------------------------------------------------------------------------------------------------------------------
----- SELECT DATABASE (if necessary) -----------------------------------------------------------------------------------
------------------------------------------------------------------------------------------------------------------------

use zionusa_events_test

------------------------------------------------------------------------------------------------------------------------
----- DROP VIEWS -------------------------------------------------------------------------------------------------------
------------------------------------------------------------------------------------------------------------------------
-- (1)
BEGIN TRY
  DROP VIEW IF EXISTS users_display_view
END TRY
BEGIN CATCH
  DROP TABLE IF EXISTS users_display_view
END CATCH
GO

-- (2)
BEGIN TRY
  DROP VIEW IF EXISTS event_branch_view
END TRY
BEGIN CATCH
  DROP TABLE IF EXISTS event_branch_view
END CATCH
GO

-- (3)
BEGIN TRY
  DROP VIEW IF EXISTS event_church_view
END TRY
BEGIN CATCH
  DROP TABLE IF EXISTS event_church_view
END CATCH
GO

-- (4)
BEGIN TRY
  DROP VIEW IF EXISTS applications_roles_view
END TRY
BEGIN CATCH
  DROP TABLE IF EXISTS applications_roles_view
END CATCH
GO

-- (5)
BEGIN TRY
  DROP VIEW IF EXISTS event_management_team_view
END TRY
BEGIN CATCH
  DROP TABLE IF EXISTS event_management_team_view
END CATCH
GO

-- (6)
BEGIN TRY
  DROP VIEW IF EXISTS users_application_role_user_permission_view
END TRY
BEGIN CATCH
  DROP TABLE IF EXISTS users_application_role_user_permission_view
END CATCH
GO

-- (7)
BEGIN TRY
  DROP VIEW IF EXISTS event_team_members_view
END TRY
BEGIN CATCH
  DROP TABLE IF EXISTS event_team_members_view
END CATCH
GO

-- (8)
BEGIN TRY
  DROP VIEW IF EXISTS event_proposals_table_view
END TRY
BEGIN CATCH
  DROP TABLE IF EXISTS event_proposals_table_view
END CATCH
GO

-- (9)
BEGIN TRY
  DROP VIEW IF EXISTS event_proposals_vips_view
END TRY
BEGIN CATCH
  DROP TABLE IF EXISTS event_proposals_vips_view
END CATCH
GO

-- (10)
BEGIN TRY
  DROP VIEW IF EXISTS reminder_event_result_survey_table_view
END TRY
BEGIN CATCH
  DROP TABLE IF EXISTS reminder_event_result_survey_table_view
END CATCH
GO

-- (11)
BEGIN TRY
  DROP VIEW IF EXISTS results_survey_view
END TRY
BEGIN CATCH
  DROP TABLE IF EXISTS results_survey_view
END CATCH
GO

-- (12)
BEGIN TRY
  DROP VIEW IF EXISTS applications
END TRY
BEGIN CATCH
  DROP TABLE IF EXISTS applications
END CATCH
GO


------------------------------------------------------------------------------------------------------------------------
----- (1) USERS DISPLAY VIEW (for query performance) -------------------------------------------------------------------
------------------------------------------------------------------------------------------------------------------------
CREATE VIEW dbo.users_display_view AS
SELECT u.id,
       u.archived,
       u.hidden,
       u.association_id,
       u.church_id        AS branch_id,
       u.church_name      AS branch_name,
       u.display_name,
       u.enabled,
       u.gender,
       u.main_church_id   AS main_branch_id,
       u.parent_church_id AS parent_branch_id,
       u.picture_id,
       u.role_id,
       u.role_name,
       u.username
FROM zionusa_management_test.dbo.users_view_materialized AS u
go


------------------------------------------------------------------------------------------------------------------------
----- (2) EVENT BRANCH VIEW (for query performance) --------------------------------------------------------------------
------------------------------------------------------------------------------------------------------------------------
CREATE view dbo.event_branch_view as
select b.id,
       b.archived,
       b.hidden,
       b.name,
       b.leader_id,
       b.state_id
from zionusa_management_test.dbo.churches b
where b.archived = 0
go

------------------------------------------------------------------------------------------------------------------------
----- (3) EVENT CHURCH VIEW (for query performance) --------------------------------------------------------------------
------------------------------------------------------------------------------------------------------------------------
CREATE view dbo.event_church_view as
select c.id,
       c.archived,
       c.hidden,
       c.name,
       c.leader_id,
       c.state_id
from zionusa_management_test.dbo.churches c
where c.archived = 0
go
------------------------------------------------------------------------------------------------------------------------
----- (4) APPLICATIONS ROLES VIEW (for query performance) --------------------------------------------------------------------
---- REQUIRES MANAGEMENT VIEWS -----------------------------------------------------------------------------------------
------------------------------------------------------------------------------------------------------------------------

CREATE VIEW applications_roles_view AS
SELECT a.id,
       a.name,
       a.display_name,
       a.description,
       a.sort_order,
       a.application_guid,
       a.created_by,
       a.created_date,
       a.last_modified_by,
       a.last_modified_date
FROM zionusa_management_test.dbo.application_roles a
WHERE a.name LIKE '%EVENT%'
   OR a.name like '%BRANCH_ACCESS%'
  AND a.enabled = 1
go

------------------------------------------------------------------------------------------------------------------------
----- (5) EVENT MANAGEMENT TEAM VIEW (for query performance) -----------------------------------------------------------
---- REQUIRES MANAGEMENT VIEWS -----------------------------------------------------------------------------------------
------------------------------------------------------------------------------------------------------------------------
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
FROM zionusa_management_test.dbo.users_display_view u
       JOIN zionusa_management_test.dbo.user_application_roles ua ON ua.user_id = u.id
       JOIN dbo.applications_roles_view ar ON ar.id = ua.application_role_id
       LEFT JOIN zionusa_management_test.dbo.user_pictures p ON p.id = u.picture_id
go

------------------------------------------------------------------------------------------------------------------------
----- (6) USERS APPLICATION ROLE USER PERMISSION VIEW (for query performance) ------------------------------------------
---- REQUIRES MANAGEMENT VIEWS -----------------------------------------------------------------------------------------
------------------------------------------------------------------------------------------------------------------------
CREATE VIEW dbo.users_application_role_user_permission_view as
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
FROM zionusa_management_test.dbo.users_application_role_user_permission_view arp
go

------------------------------------------------------------------------------------------------------------------------
----- (7) EVENT TEAM MEMBERS VIEW (for query performance) --------------------------------------------------------------
---- REQUIRES MANAGEMENT VIEWS -----------------------------------------------------------------------------------------
------------------------------------------------------------------------------------------------------------------------
CREATE VIEW dbo.event_team_members_view AS
SELECT CONCAT_WS('-', u.id, ua.application_role_id) AS id,
       u.id                                         AS user_id,
       ar.name                                      As application_role_name,
       ua.application_role_id                       AS application_role_id,
       ar.display_name                              AS application_role_display_name,
       u.association_id                             AS association_id,
       u.church_id                                  AS branch_id,
       u.church_name                                AS branch_name,
       u.display_name                               AS display_name,
       u.gender                                     AS gender,
       u.main_church_id                             AS main_branch_id,
       u.parent_church_id                           AS parent_branch_id,
       u.picture_id                                 AS picture_id,
       p.thumbnail_url                              AS picture_thumbnail_url,
       u.role_id                                    AS role_id,
       u.role_name                                  AS role_name,
       u.username                                   AS username
FROM zionusa_management_test.dbo.users_display_view u
       JOIN zionusa_management_test.dbo.user_application_roles ua ON ua.user_id = u.id
       JOIN zionusa_management_test.dbo.application_roles ar ON ar.id = ua.application_role_id
       LEFT JOIN zionusa_management_test.dbo.user_pictures p ON p.id = u.picture_id
WHERE u.enabled = 1
  AND (
      ar.name like '%EVENT%'
    OR (u.role_name like '%Church Leader%' and ar.name like '%BRANCH_ACCESS%')
    OR (u.role_name like '%Overseer%' and ar.name like '%BRANCH_ACCESS%')
  )
go


------------------------------------------------------------------------------------------------------------------------
----- (8) EVENT PROPOSAL TABLE VIEW MATERIALIZED (for query performance) -----------------------------------------------
------------------------------------------------------------------------------------------------------------------------
CREATE VIEW dbo.event_proposals_table_view WITH SCHEMABINDING as
SELECT e.id                                                                                        AS id,
       e.archived                                                                                  AS archived,
       MAX(e.title)                                                                                AS title,
       MAX(e.branch_id)                                                                            AS branch_id,
       MAX(e.branch_name)                                                                          AS branch_name,
       MAX(c.id)                                                                                   AS category_id,
       MAX(c.title)                                                                                AS category_name,
       e.hidden                                                                                    AS hidden,
       MAX(e.location_id)                                                                          AS location_id,
       MAX(IIF(e.location_name IS NULL OR e.location_name = '', 'Internal', e.location_name))      AS location_name,
       MAX(e.proposed_date)                                                                        AS proposed_date,
       MAX(IIF(e.workflow_status IS NULL OR e.workflow_status = '', 'Pending', e.workflow_status)) AS workflow_status,
       SUM(IIF(v.type = 1,
               v.female_adults + v.female_college_students + v.female_young_adults + v.female_teenagers +
               v.male_adults + v.male_college_students + v.male_young_adults + v.male_teenagers,
               0))                                                                                 AS contacts_total,
       SUM(IIF(v.type = 2,
               v.female_adults + v.female_college_students + v.female_young_adults + v.female_teenagers +
               v.male_adults + v.male_college_students + v.male_young_adults + v.male_teenagers,
               0))                                                                                 AS members_total,
       SUM(IIF(v.type = 0,
               v.female_adults + v.female_college_students + v.female_young_adults + v.female_teenagers +
               v.male_adults + v.male_college_students + v.male_young_adults + v.male_teenagers,
               0))                                                                                 AS vips_total,
       SUM(v.female_adults + v.female_college_students + v.female_young_adults + v.female_teenagers +
           v.male_adults + v.male_college_students + v.male_young_adults +
           v.male_teenagers)                                                                       AS participants_total,
       MAX(t.id)                                                                                   AS type_id,
       MAX(t.title)                                                                                AS type_name
FROM dbo.event_volunteer_management v
       JOIN dbo.event_proposals e ON v.event_proposal_id = e.id
       JOIN dbo.event_categories c ON e.event_category_id = c.id
       JOIN dbo.event_types t ON e.event_type_id = t.id
GROUP BY e.id, e.archived, e.hidden
go


------------------------------------------------------------------------------------------------------------------------
----- (9) EVENT PROPOSAL VIP VIEW MATERIALIZED (for query performance) -------------------------------------------------
------------------------------------------------------------------------------------------------------------------------
CREATE VIEW dbo.event_proposals_vips_view WITH SCHEMABINDING as
SELECT ev.id                   AS id,
       ev.archived             AS archived,
       ev.hidden               AS hidden,
       c.id                    AS vip_id,
       c.business_office_phone AS business_office_phone,
       c.cell_phone            AS cell_phone,
       c.city                  AS city,
       c.company_name          AS company_name,
       c.country_region        AS country_region,
       c.email_address         AS email_address,
       c.facebook_url          AS facebook_url,
       e.event_category_id     AS event_category_id,
       ec.title                AS event_category_title,
       e.id                    AS event_proposal_id,
       e.branch_id             AS event_proposal_branch_id,
       e.branch_name           AS event_proposal_branch_name,
       e.proposed_date         AS event_proposal_proposed_date,
       e.title                 AS event_proposal_title,
       e.event_type_id         AS event_type_id,
       et.title                AS event_type_title,
       c.first_name            AS first_name,
       c.instagram_url         AS instagram_url,
       c.last_name             AS last_name,
       c.linked_in_url         AS linked_in_url,
       c.picture_url           AS picture_url,
       c.state_province        AS state_province,
       c.title                 AS title,
       c.twitter_url           AS twitter_url,
       COUNT_BIG(*)            as instances
FROM dbo.contacts c
       JOIN dbo.event_proposals_vips ev ON ev.contact_id = c.id
       JOIN dbo.event_proposals e ON e.id = ev.event_proposal_id
       JOIN dbo.event_categories ec ON ec.id = e.event_category_id
       JOIN dbo.event_types et ON et.id = e.event_type_id
GROUP BY ev.id, ev.archived, ev.hidden, c.id, c.business_office_phone, et.title, e.title, ec.title, c.cell_phone,
         c.city, c.company_name,
         c.country_region, c.email_address, c.facebook_url, e.event_category_id, e.id, e.branch_id, e.branch_name,
         e.proposed_date, e.event_type_id, c.first_name, c.instagram_url, c.last_name, c.linked_in_url,
         c.picture_url, c.state_province, c.title, c.twitter_url
go

create unique clustered index IDX_V1
  on dbo.event_proposals_vips_view (id)
go

------------------------------------------------------------------------------------------------------------------------
----- (10) REMINDER EVENT RESULT SURVEY TABLE VIEW (for query performance) ----------------------------------------------
------------------------------------------------------------------------------------------------------------------------
CREATE VIEW dbo.reminder_event_result_survey_table_view WITH SCHEMABINDING AS

SELECT ep.id       AS event_id,
       ep.archived AS archived,
       ep.hidden   AS hidden,
       ep.title    AS event_title,
       ep.proposed_end_date,
       ep.requester_id,
       ep.requester_name,
       ep.requester_email,
       ep.branch_id,
       ep.branch_name,
       rs.event_proposal_id,
       rs.finalized
FROM dbo.event_proposals ep
       LEFT JOIN dbo.results_survey rs
                 ON ep.id = rs.event_proposal_id
WHERE CAST(ep.proposed_end_date AS DATE) < DATEADD(DAY, DATEDIFF(DAY, 0, GETDATE()), 0)
  AND rs.event_proposal_id IS NULL
  AND ep.archived = 0
   OR (rs.finalized = 0)
go

------------------------------------------------------------------------------------------------------------------------
----- (11) RESULT SURVEY VIEW (for query performance) -------------------------------------------------------------------
------------------------------------------------------------------------------------------------------------------------
CREATE VIEW dbo.results_survey_view AS
SELECT r.id                                              AS id,
       CAST(IIF(r.archive IS NULL, 0, r.archive) AS BIT) AS archive,
       r.archived                                        AS archived,
       r.banner_group_photo                              AS banner_group_photo,
       r.beneficiaries_age_range                         AS beneficiaries_age_range,
       r.blood_donors                                    AS blood_donors,
       r.branch_id                                       AS branch_id,
       r.branch_items                                    AS branch_items,
       b.name                                            AS branch_name,
       r.branch_services                                 AS branch_services,
       r.distance_cleaned_in_miles                       AS distance_cleaned_in_miles,
       r.distributed_materials                           AS distributed_materials,
       r.donation1amount                                 AS donation1amount,
       r.donation1item                                   AS donation1item,
       r.donation2amount                                 AS donation2amount,
       r.donation2item                                   AS donation2item,
       r.donation3amount                                 AS donation3amount,
       r.donation3item                                   AS donation3item,
       r.donation4amount                                 AS donation4amount,
       r.donation4item                                   AS donation4item,
       r.donation5amount                                 AS donation5amount,
       r.donation5item                                   AS donation5item,
       r.donation6amount                                 AS donation6amount,
       r.donation6item                                   AS donation6item,
       r.environment_population                          AS environment_population,
       r.event_category_id                               AS event_category_id,
       c.title                                           AS event_category_title,
       e.branch_id                                       AS event_proposal_branch_id,
       e.branch_name                                     AS event_proposal_branch_name,
       e.event_category_id                               AS event_proposal_category_id,
       r.event_proposal_id                               AS event_proposal_id,
       e.is_internal                                     AS event_proposal_is_internal,
       e.location_id                                     AS event_proposal_location_id,
       e.is_private                                      AS event_proposal_is_private,
       e.partner_organization_id                         AS event_proposal_partner_organization_id,
       e.proposed_date                                   AS event_proposal_proposed_date,
       e.proposed_time                                   AS event_proposal_proposed_time,
       e.proposed_end_date                               AS event_proposal_proposed_end_date,
       e.proposed_end_time                               AS event_proposal_proposed_end_time,
       e.title                                           AS event_proposal_title,
       e.event_type_id                                   AS event_proposal_type_id,
       r.event_type_id                                   AS event_type_id,
       t.title                                           AS event_type_title,
       r.event_video                                     AS event_video,
       r.finalized                                       AS finalized,
       r.fragrances                                      AS fragrances,
       r.fragrances_recorded                             AS fragrances_recorded,
       r.femaleabblood                                   AS femaleabblood,
       r.femaleabpositive_blood                          AS femaleabpositive_blood,
       r.femaleanegative_blood                           AS femaleanegative_blood,
       r.femaleapositive_blood                           AS femaleapositive_blood,
       r.femalebnegative_blood                           AS femalebnegative_blood,
       r.femalebpositive_blood                           AS femalebpositive_blood,
       r.femaleonegative_blood                           AS femaleonegative_blood,
       r.femaleopositive_blood                           AS femaleopositive_blood,
       r.guest_blood_donors                              AS guest_blood_donors,
       r.helped_addresses                                AS helped_addresses,
       r.hidden                                          AS hidden,
       r.is_internal                                     AS is_internal,
       r.is_private                                      AS is_private,
       l.address                                         AS location_address,
       l.city                                            AS location_city,
       l.country_region                                  AS location_country_region,
       r.location_id                                     AS location_id,
       l.location_name                                   AS location_name,
       l.state_province                                  AS location_state_province,
       l.zip_postal_code                                 AS location_zip_postal_code,
       r.maleabblood                                     AS maleabblood,
       r.maleabpositive_blood                            AS maleabpositive_blood,
       r.maleanegative_blood                             AS maleanegative_blood,
       r.maleapositive_blood                             AS maleapositive_blood,
       r.malebnegative_blood                             AS malebnegative_blood,
       r.malebpositive_blood                             AS malebpositive_blood,
       r.maleonegative_blood                             AS maleonegative_blood,
       r.maleopositive_blood                             AS maleopositive_blood,
       r.man_hours_per_volunteer                         AS man_hours_per_volunteer,
       r.max_beneficiaries_age_range                     AS max_beneficiaries_age_range,
       r.media_attended                                  AS media_attended,
       r.media_name                                      AS media_name,
       r.min_beneficiaries_age_range                     AS min_beneficiaries_age_range,
       r.new_donations                                   AS new_donations,
       r.new_members_added                               AS new_members_added,
       r.no_event_video_reason                           AS no_event_video_reason,
       r.no_vip_photo_reason                             AS no_vip_photo_reason,
       r.non_members                                     AS non_members,
       r.number_of_lives_saved                           AS number_of_lives_saved,
       r.ounce_blood_collected                           AS ounce_blood_collected,
       o.address                                         AS partner_organization_address,
       o.city                                            AS partner_organization_city,
       o.country_region                                  AS partner_organization_country_region,
       r.partner_organization_id                         AS partner_organization_id,
       r.partner_organization_items                      AS partner_organization_items,
       r.partner_organization_services                   AS partner_organization_services,
       o.state_province                                  AS partner_organization_state_province,
       o.title                                           AS partner_organization_title,
       o.zip_postal_code                                 AS partner_organization_zip_postal_code,
       r.post_survey_completed                           AS post_survey_completed,
       r.pre_survey_completed                            AS pre_survey_completed,
       r.proposed_date                                   AS proposed_date,
       r.proposed_time                                   AS proposed_time,
       r.proposed_end_date                               AS proposed_end_date,
       r.proposed_end_time                               AS proposed_end_time,
       r.same_event_day                                  AS same_event_day,
       r.same_event_location                             AS same_event_location,
       r.same_event_partner_organization                 AS same_event_partner_organization,
       r.same_number_of_volunteers                       AS same_number_of_volunteers,
       r.survey_participants                             AS survey_participants,
       r.survey_respondents                              AS survey_respondents,
       r.title                                           AS title,
       r.total_donation_amount                           AS total_donation_amount,
       r.type_of_trees                                   AS type_of_trees,
       r.vip_photos                                      AS vip_photos,
       r.volunteer_blood_donors                          AS volunteer_blood_donors,
       r.volunteer_female_adults                         AS volunteer_female_adults,
       r.volunteer_male_adults                           AS volunteer_male_adults,
       r.volunteer_female_college_students               AS volunteer_female_college_students,
       r.volunteer_male_college_students                 AS volunteer_male_college_students,
       r.volunteer_female_teenagers                      AS volunteer_female_teenagers,
       r.volunteer_male_teenagers                        AS volunteer_male_teenagers,
       r.volunteer_female_young_adults                   AS volunteer_female_young_adults,
       r.volunteer_male_young_adults                     AS volunteer_male_young_adults,
       r.workflow_status                                 AS workflow_status,
       r.write_up                                        AS write_up,
       r.created_by                                      AS created_by,
       r.created_date                                    AS created_date,
       r.last_modified_by                                AS last_modified_by,
       r.last_modified_date                              AS last_modified_date,
       r.visitors                                        AS visitors,
       r.homes_helped                                    AS homes_helped,
       r.people_helped                                   AS people_helped,
       r.bags_of_trash                                   AS bags_of_trash,
       r.distance_cleaned_in_km                          AS distance_cleaned_in_km,
       r.weight_of_trash_cleaned                         As weight_of_trash_cleaned,
       r.guest_still_studying                            AS guest_still_studying,
       r.guest_baptized                                  AS guest_baptized,
       r.engagement_rating                               AS engagement_rating,
       r.present_again                                   AS present_again,
       r.presentation_rating                             AS presentation_rating,
       r.well_spoken_rating                              AS well_spoken_rating,
       r.trees_planted                                   AS trees_planted,
       r.pints_blood_collected                           AS pints_blood_collected
FROM dbo.results_survey r
       LEFT JOIN dbo.event_proposals e ON e.id = r.event_proposal_id
       LEFT JOIN dbo.locations l ON l.id = r.location_id
       LEFT JOIN dbo.organizations o ON o.id = r.partner_organization_id
       LEFT JOIN dbo.event_church_view b ON b.id = r.branch_id
       LEFT JOIN dbo.event_categories c ON c.id = r.event_category_id
       LEFT JOIN dbo.event_types t ON t.id = r.event_type_id
go

------------------------------------------------------------------------------------------------------------------------
----- (12) APPLICATIONS VIEW/TABLE (for query performance) -------------------------------------------------------------------
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
FROM zionusa_management_test.dbo.applications a
go



------------------------------------------------------------------------------------------------------------------------
----- Last 6 Months Event Report  --------------------------------------------------------------------------------------
------------------------------------------------------------------------------------------------------------------------
SELECT e.id                                                                                        AS id,
       MAX(e.title)                                                                                AS title,
       MAX(e.branch_name)                                                                          AS branch_name,
       MAX(c.title)                                                                                AS category_name,
       MAX(IIF(e.location_name IS NULL OR e.location_name = '', 'Internal', e.location_name))      AS location_name,
       MAX(e.proposed_date)                                                                        AS proposed_date,
       MAX(IIF(e.workflow_status IS NULL OR e.workflow_status = '', 'Pending', e.workflow_status)) AS workflow_status,
       SUM(IIF(v.type = 1,
               v.female_adults + v.female_college_students + v.female_young_adults + v.female_teenagers +
               v.male_adults + v.male_college_students + v.male_young_adults + v.male_teenagers,
               0))                                                                                 AS contacts_total,
       SUM(IIF(v.type = 2,
               v.female_adults + v.female_college_students + v.female_young_adults + v.female_teenagers +
               v.male_adults + v.male_college_students + v.male_young_adults + v.male_teenagers,
               0))                                                                                 AS members_total,
       SUM(IIF(v.type = 0,
               v.female_adults + v.female_college_students + v.female_young_adults + v.female_teenagers +
               v.male_adults + v.male_college_students + v.male_young_adults + v.male_teenagers,
               0))                                                                                 AS vips_total,
       SUM(v.female_adults + v.female_college_students + v.female_young_adults + v.female_teenagers +
           v.male_adults + v.male_college_students + v.male_young_adults +
           v.male_teenagers)                                                                       AS participants_total,
       MAX(t.title)                                                                                AS type_name
FROM dbo.event_volunteer_management v
       JOIN dbo.event_proposals e ON v.event_proposal_id = e.id
       JOIN dbo.event_categories c ON e.event_category_id = c.id
       JOIN dbo.event_types t ON e.event_type_id = t.id
WHERE DATEDIFF(MM, e.proposed_end_date, GETDATE()) < 6 and e.archived = 0
GROUP BY e.branch_name, e.id

SELECT
  c.first_name            AS first_name,
  c.last_name             AS last_name,
  c.title                 AS title,
  c.company_name          AS company_name,
  c.email_address         AS email_address,
  c.business_office_phone AS business_office_phone,
  c.cell_phone            AS cell_phone,
  c.city                  AS city,
  c.state_province        AS state_province,
  c.country_region        AS country_region,
  c.instagram_url         AS instagram_url,
  c.linked_in_url         AS linked_in_url,
  c.twitter_url           AS twitter_url,
  c.facebook_url          AS facebook_url,

  ec.title                AS event_category_title,
  e.title                 AS event_proposal_title,
  et.title                AS event_type_title,
  e.proposed_date         AS event_proposal_proposed_date,
  e.branch_name,

  COUNT_BIG(*)            as instances
FROM dbo.contacts c
       JOIN dbo.event_proposals_vips ev ON ev.contact_id = c.id
       JOIN dbo.event_proposals e ON e.id = ev.event_proposal_id
       JOIN dbo.event_categories ec ON ec.id = e.event_category_id
       JOIN dbo.event_types et ON et.id = e.event_type_id
WHERE DATEDIFF(MM, e.proposed_end_date, GETDATE()) < 6
GROUP BY c.first_name,
         c.last_name,
         c.title,
         c.company_name,
         c.email_address,
         c.business_office_phone,
         c.cell_phone,
         c.city,
         c.state_province,
         c.country_region,
         c.instagram_url,
         c.linked_in_url,
         c.twitter_url,
         c.facebook_url,

         ec.title,
         et.title,
         e.title,
         e.branch_name,
         e.proposed_date
