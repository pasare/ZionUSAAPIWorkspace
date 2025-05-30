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
GO
