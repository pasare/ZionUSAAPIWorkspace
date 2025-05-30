CREATE VIEW dbo.preaching_log_association_daily_view AS
SELECT NEWID()          AS unique_id,
       u.association_id AS id,
       l.date AS date,
       MAX(a.name)                         AS name,
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
         JOIN zionusa_management_test.dbo.associations a ON a.id = u.association_id
WHERE ISNULL(l.acquaintances, 0) + ISNULL(l.contacts, 0) + ISNULL(l.co_workers, 0) + ISNULL(l.family, 0) +
      ISNULL(l.friends, 0) + ISNULL(l.fruit, 0) + ISNULL(l.meaningful, 0) + ISNULL(l.neighbors, 0) +
      ISNULL(l.simple, 0) > 0
GROUP BY u.association_id, l.date
go
