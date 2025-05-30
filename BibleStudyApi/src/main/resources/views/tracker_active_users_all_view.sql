CREATE VIEW dbo.tracker_active_users_all_view AS
SELECT CONCAT_WS('-', s.year,  a.month) AS id,  a.year, a.month, COUNT(*) as num_of_active_users FROM (SELECT DATEPART(YEAR, l.date)  AS year,
       DATEPART(MONTH, l.date)  AS month,
       u.id                            AS user_id,
       u.display_name                  AS user_display_name,
       COUNT(*)                        AS num_logs
FROM zionusa_biblestudy_test.dbo.preaching_logs l
         JOIN zionusa_management_test.dbo.users_display_view u ON l.user_id1 = u.id
WHERE l.date >= '2021-04-01'
AND l.date <= '2021-06-30'
GROUP BY DATEPART(YEAR, l.date), DATEPART(MONTH, l.date), u.id, u.display_name
HAVING  COUNT(*) > 4
) AS a GROUP BY a.year, a.month
go

