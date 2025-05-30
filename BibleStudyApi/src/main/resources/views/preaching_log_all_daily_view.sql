CREATE VIEW dbo.preaching_log_all_daily_view WITH SCHEMABINDING as
SELECT p.date                          AS date,
       SUM(ISNULL(p.acquaintances, 0)) AS acquaintances,
       SUM(ISNULL(p.contacts, 0))      AS contacts,
       SUM(ISNULL(p.co_workers, 0))    AS co_workers,
       SUM(ISNULL(p.family, 0))        AS family,
       SUM(ISNULL(p.friends, 0))       AS friends,
       SUM(ISNULL(p.fruit, 0))         AS fruit,
       SUM(ISNULL(p.meaningful, 0))    AS meaningful,
       SUM(ISNULL(p.neighbors, 0))     AS neighbors,
       SUM(ISNULL(p.simple, 0))        AS simple,
       COUNT_BIG(*)                    AS num_logs
FROM dbo.preaching_logs p
GROUP BY p.date
go

create unique clustered index IDX_V1
    on dbo.preaching_log_all_daily_view (date)
go
