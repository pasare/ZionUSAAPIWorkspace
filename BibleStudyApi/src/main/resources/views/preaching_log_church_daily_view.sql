CREATE VIEW dbo.preaching_log_church_daily_view AS
SELECT NEWID()                         AS unique_id,
       l.date                          AS date,
       u.church_id                     AS id,
       MAX(cp.thumbnail_url)           AS thumbnail_url,
       MAX(cp.picture_url_medium)      AS picture_url_medium,
       MAX(c.name)                     AS name,
       MAX(C.display_name)             AS display_name,
       MAX(c.city)                     AS city,
       MAX(c.state_abbrv)              AS state_abbrv,
       MAX(c.state_name)               AS state_name,
       MAX(u.main_church_id)           AS main_church_id,
       MAX(u.association_id)           AS association_id,
       MAX(cr.region_name)             AS region_name,
       MAX(cr.region_id)               AS region_id,
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
         JOIN zionusa_management_test.dbo.users_display_view u ON l.user_id1 = u.id
         JOIN zionusa_management_test.dbo.church_display_view c ON c.id = u.church_id
         LEFT JOIN zionusa_management_test.dbo.church_pictures cp ON c.id = cp.church_id
         LEFT JOIN zionusa_management_test.dbo.churches_region_materialized_view cr ON cr.id = u.church_id
WHERE (ISNULL(l.acquaintances, 0) + ISNULL(l.contacts, 0) + ISNULL(l.co_workers, 0) + ISNULL(l.family, 0) +
       ISNULL(l.friends, 0) + ISNULL(l.fruit, 0) + ISNULL(l.meaningful, 0) + ISNULL(l.neighbors, 0) +
       ISNULL(l.simple, 0) > 0)
GROUP BY l.date, u.church_id
go
