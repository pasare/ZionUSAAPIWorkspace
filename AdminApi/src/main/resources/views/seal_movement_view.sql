-- Seal Movement View
-- * [3234] Zion Seal Test 1
-- * [3235] Zion Seal Test 2
-- * [3236] Zion Seal Test 3
CREATE VIEW dbo.seal_movement_view WITH SCHEMABINDING AS
SELECT cl.user_id,
       ch.id       AS challenge_id,
       ch.name     AS challenge_name,
       cl.end_date AS completed_date,
       cl.completed,
       cl.id       AS challenge_log_id
FROM dbo.challenges ch
         JOIN dbo.goal_logs cl on ch.id = cl.challenge_id
WHERE (ch.id = 3234 OR ch.id = 3235 OR ch.id = 3236)
  AND cl.completed = 1
GO

CREATE UNIQUE CLUSTERED INDEX ucidx_userid_ch_id
    ON dbo.seal_movement_view (user_id, challenge_id)
GO

