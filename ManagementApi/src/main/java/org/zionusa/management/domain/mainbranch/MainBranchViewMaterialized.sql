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

create
unique
clustered index IDX_V1
    on dbo.main_branches_view_materialized (id)
go
