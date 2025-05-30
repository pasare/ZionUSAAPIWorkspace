CREATE VIEW dbo.movement_user_category_count_view WITH SCHEMABINDING as
SELECT m.id                                           AS id,
       COUNT(CASE WHEN a.category_id = 5 THEN 1 END)  AS signature_count,
       COUNT(CASE WHEN a.category_id = 12 THEN 1 END) AS preaching_count,
       COUNT(CASE WHEN a.category_id = 15 THEN 1 END) AS fruit_count,
       COUNT(CASE WHEN a.category_id = 16 THEN 1 END) AS attendance_count,
       COUNT(CASE WHEN a.id = 431 THEN 1 END)         AS t_signature_count,
       COUNT(CASE WHEN a.category_id != 5 AND a.category_id != 12 AND a.category_id != 15 AND a.category_id != 16 THEN 1 END) AS error_count,
       COUNT(a.category_id)                           AS total_count,
       SUM(a.points)                                  AS total_points
FROM dbo.activity_logs al
JOIN dbo.movements m ON al.movement_id = m.id
JOIN dbo.activities a ON al.activity_id = a.id
GROUP BY m.id
go

