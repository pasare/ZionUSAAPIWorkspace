-- Migrate church columns to branch
UPDATE dbo.event_volunteer_management
SET branch_id = church_id,
    branch_name = church_name,
    helping_branch = helping_church
WHERE 1=1;

UPDATE dbo.event_volunteer_management_aud
SET branch_id = church_id,
    branch_name = church_name,
    helping_branch = helping_church
WHERE 1=1;

UPDATE dbo.event_volunteer_management
SET is_for_results_survey = CAST(0 AS BIT)
WHERE 1=1;
