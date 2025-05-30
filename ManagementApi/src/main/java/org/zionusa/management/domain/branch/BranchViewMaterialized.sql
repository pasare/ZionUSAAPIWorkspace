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
       a.thumbnail_file_id                                                         AS association_pthumbnail_file_id
FROM dbo.branches b
         JOIN dbo.main_branches mb ON mb.id = b.main_branch_id
         JOIN dbo.associations a ON mb.association_id = a.id
         JOIN dbo.states s ON s.id = b.state_id
         JOIN dbo.branches_types t ON b.type_id = t.id
go

create
    unique
    clustered index IDX_V1
    on dbo.branches_view_materialized (id)
go
