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
