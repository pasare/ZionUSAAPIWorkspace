CREATE VIEW dbo.reminder_event_result_survey_table_view WITH SCHEMABINDING AS

SELECT ep.id       AS event_id,
       ep.archived AS archived,
       ep.hidden   AS hidden,
       ep.title    AS event_title,
       ep.proposed_end_date,
       ep.requester_id,
       ep.requester_name,
       ep.requester_email,
       ep.branch_id,
       ep.branch_name,
       rs.event_proposal_id,
       rs.finalized
FROM dbo.event_proposals ep
         LEFT JOIN dbo.results_survey rs
                   ON ep.id = rs.event_proposal_id
WHERE CAST(ep.proposed_end_date AS DATE) < DATEADD(DAY, DATEDIFF(DAY, 0, GETDATE()), 0)
    AND rs.event_proposal_id IS NULL
    AND ep.archived = 0
   OR (rs.finalized = 0)
go
