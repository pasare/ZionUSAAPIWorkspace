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
       u.parent_church_id AS parent_church_id,
       u.picture_id,
       u.role_id,
       u.role_name,
       u.username
FROM zionusa_management_api.dbo.users_display_view AS u
go
