CREATE VIEW dbo.preaching_log_overseer_daily_view AS
SELECT NEWID()                    AS unique_id,
       u.main_church_id           AS id,
       MAX(cp.thumbnail_url)      AS thumbnail_url,
       MAX(cp.picture_url_medium) AS picture_url_medium,
       p.date                          AS date,
       MAX(c.name)                     AS name,
       MAX(c.city)                     AS city,
       MAX(c.state_abbrv)              AS state_abbrv,
       MAX(c.state_name)               AS state_name,
       MAX(u.association_id)           AS association_id,
       SUM(ISNULL(p.acquaintances, 0)) AS acquaintances,
       SUM(ISNULL(p.contacts, 0))      AS contacts,
       SUM(ISNULL(p.co_workers, 0))    AS co_workers,
       SUM(ISNULL(p.family, 0))        AS family,
       SUM(ISNULL(p.friends, 0))       AS friends,
       SUM(ISNULL(p.fruit, 0))         AS fruit,
       SUM(ISNULL(p.meaningful, 0))    AS meaningful,
       SUM(ISNULL(p.neighbors, 0))     AS neighbors,
       SUM(ISNULL(p.simple, 0))        AS simple,
       COUNT(*)                        AS num_logs
FROM zionusa_biblestudy_test.dbo.preaching_logs p
         JOIN zionusa_management_test.dbo.users_display_view u ON p.user_id1 = u.id
         JOIN zionusa_management_test.dbo.church_display_view c ON c.id = u.main_church_id
         LEFT JOIN zionusa_management_test.dbo.church_pictures cp ON c.id = cp.church_id
WHERE c.hidden_location = 0
GROUP BY p.date, u.main_church_id
go
