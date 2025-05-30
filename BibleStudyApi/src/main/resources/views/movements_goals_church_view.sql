CREATE VIEW dbo.movements_goals_church_view AS
SELECT g.id                      AS id,
       g.reference_id            AS reference_id,
       c.name                    AS name,
       c.type_id                 AS reference_type_id,
       g.movement_goal_type_id   AS movement_goal_type_id,
       g.movement_goal_type_name AS movement_goal_type_name,
       g.movement_id             AS movement_id,
       g.movement_name           AS movement_name,
       g.movement_start_date     AS movement_start_date,
       g.movement_end_date       AS movement_end_date,
       g.participant_count       AS participant_count,
       g.fruit                   AS fruit,
       g.one_time_attendance     AS one_time_attendance,
       g.meaningful              AS meaningful,
       g.simple                  AS simple,
       g.talents                 AS talents
FROM zionusa_admin_test.dbo.movements_goals_view g
         JOIN zionusa_management_test.dbo.church_display_view c ON g.reference_id = c.id
WHERE g.movement_goal_type_id = 4
  AND g.movement_id = 4
    go
