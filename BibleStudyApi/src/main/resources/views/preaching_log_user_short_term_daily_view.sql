CREATE VIEW dbo.preaching_log_user_short_term_daily_view AS
SELECT NEWID() AS unique_id,
       l.date AS date,
       u.id                            AS user_id,
       u.short_term_id                 AS short_term_id,
       u.display_name                  AS user_display_name,
       p.thumbnail_url                 AS user_picture_url,
       u.team_id                       AS team_id,
       u.team_name                     AS team_name,
       u.group_id                      AS group_id,
       u.group_name                    AS group_name,
       u.church_id                     AS church_id,
       u.church_name                   AS church_name,
       u.main_church_id                AS main_church_id,
       SUM(ISNULL(l.acquaintances, 0)) AS acquaintances,
       SUM(ISNULL(l.contacts, 0))      AS contacts,
       SUM(ISNULL(l.co_workers, 0))    AS co_workers,
       SUM(ISNULL(l.family, 0))        AS family,
       SUM(ISNULL(l.friends, 0))       AS friends,
       SUM(ISNULL(l.fruit, 0))         AS fruit,
       SUM(ISNULL(l.meaningful, 0))    AS meaningful,
       SUM(ISNULL(l.neighbors, 0))     AS neighbors,
       SUM(ISNULL(l.simple, 0))        AS simple,
       COUNT(*)                        AS num_logs
FROM zionusa_biblestudy_test.dbo.preaching_logs l
         JOIN zionusa_management_test.dbo.users_short_term_view u
              ON l.user_id1 = u.id
         LEFT JOIN zionusa_management_test.dbo.user_pictures p ON p.id = u.picture_id
WHERE ISNULL(l.acquaintances, 0) + ISNULL(l.contacts, 0) + ISNULL(l.co_workers, 0) + ISNULL(l.family, 0) +
      ISNULL(l.friends, 0) + ISNULL(l.fruit, 0) + ISNULL(l.meaningful, 0) + ISNULL(l.neighbors, 0) +
      ISNULL(l.simple, 0) > 0
GROUP BY u.id, u.short_term_id, u.display_name, l.date, p.thumbnail_url, u.team_id, u.team_name, u.group_id,
         u.group_name, u.church_id,
         u.church_name, u.main_church_id
go
