CREATE VIEW dbo.event_proposals_table_view WITH SCHEMABINDING as
SELECT e.id                                                                                        AS id,
       e.archived                                                                                  AS archived,
       MAX(e.title)                                                                                AS title,
       MAX(e.branch_id)                                                                            AS branch_id,
       MAX(e.branch_name)                                                                          AS branch_name,
       MAX(c.id)                                                                                   AS category_id,
       MAX(c.title)                                                                                AS category_name,
       e.hidden                                                                                    AS hidden,
       MAX(e.location_id)                                                                          AS location_id,
       MAX(IIF(e.location_name IS NULL OR e.location_name = '', 'Internal', e.location_name))      AS location_name,
       MAX(e.proposed_date)                                                                        AS proposed_date,
       MAX(IIF(e.workflow_status IS NULL OR e.workflow_status = '', 'Pending', e.workflow_status)) AS workflow_status,
       SUM(IIF(v.type = 1,
               v.female_adults + v.female_college_students + v.female_young_adults + v.female_teenagers +
               v.male_adults + v.male_college_students + v.male_young_adults + v.male_teenagers,
               0))                                                                                 AS contacts_total,
       SUM(IIF(v.type = 2,
               v.female_adults + v.female_college_students + v.female_young_adults + v.female_teenagers +
               v.male_adults + v.male_college_students + v.male_young_adults + v.male_teenagers,
               0))                                                                                 AS members_total,
       SUM(IIF(v.type = 0,
               v.female_adults + v.female_college_students + v.female_young_adults + v.female_teenagers +
               v.male_adults + v.male_college_students + v.male_young_adults + v.male_teenagers,
               0))                                                                                 AS vips_total,
       SUM(v.female_adults + v.female_college_students + v.female_young_adults + v.female_teenagers +
           v.male_adults + v.male_college_students + v.male_young_adults +
           v.male_teenagers)                                                                       AS participants_total,
       MAX(t.id)                                                                                   AS type_id,
       MAX(t.title)                                                                                AS type_name
FROM dbo.event_volunteer_management v
       JOIN dbo.event_proposals e ON v.event_proposal_id = e.id
       JOIN dbo.event_categories c ON e.event_category_id = c.id
       JOIN dbo.event_types t ON e.event_type_id = t.id
GROUP BY e.id, e.archived, e.hidden
  go
