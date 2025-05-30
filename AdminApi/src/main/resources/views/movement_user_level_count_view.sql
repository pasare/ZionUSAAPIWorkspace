CREATE view dbo.movement_user_level_count_view WITH SCHEMABINDING as
SELECT CONCAT_WS('-', m.movement_id, m.level) AS id,
       m.level                                AS level,
       m.movement_id                          AS movement_id,
       COUNT(m.level)                         AS total_count
FROM dbo.movement_user_level_view m
GROUP BY m.level, m.movement_id
go

