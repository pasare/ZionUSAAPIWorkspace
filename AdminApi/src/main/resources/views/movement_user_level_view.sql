CREATE view dbo.movement_user_level_view WITH SCHEMABINDING as
SELECT CONCAT_WS('-', m.movement_id, m.user_id) AS id,
      CASE
        WHEN m.preaching_count >= 48 AND m.signature_count >= 48 AND m.fruit_count >= 4 AND (m.attendance_count >= 1 or m.t_signature_count >= 1) THEN 13
        WHEN m.preaching_count >= 44 AND m.signature_count >= 44 AND m.fruit_count >= 4 THEN 12
        WHEN m.preaching_count >= 40 AND m.signature_count >= 40 AND m.fruit_count >= 3 THEN 11
        WHEN m.preaching_count >= 36 AND m.signature_count >= 36 AND m.fruit_count >= 3 THEN 10
        WHEN m.preaching_count >= 32 AND m.signature_count >= 32 AND m.fruit_count >= 2 THEN 9
        WHEN m.preaching_count >= 28 AND m.signature_count >= 28 AND m.fruit_count >= 2 THEN 8
        WHEN m.preaching_count >= 24 AND m.signature_count >= 24 AND m.fruit_count >= 1 THEN 7
        WHEN m.preaching_count >= 20 AND m.signature_count >= 20 AND m.fruit_count >= 1 THEN 6
        WHEN m.preaching_count >= 16 AND m.signature_count >= 16 THEN 5
        WHEN m.preaching_count >= 12 AND m.signature_count >= 12 THEN 4
        WHEN m.preaching_count >= 8 AND m.signature_count >= 8 THEN 3
        WHEN m.preaching_count >= 4 AND m.signature_count >= 4 THEN 2
      ELSE 1
      END AS level,
       m.movement_id                   AS movement_id,
       m.total_count - m.error_count   AS total_count,
       m.user_id                       AS user_id,
       ''                              AS user_name
FROM dbo.movement_user_category_view m
GROUP BY m.attendance_count, m.error_count, m.fruit_count, m.movement_id, m.preaching_count, m.signature_count, m.t_signature_count, m.total_count, m.user_id
go

