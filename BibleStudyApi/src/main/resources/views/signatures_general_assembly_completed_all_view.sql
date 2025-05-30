CREATE VIEW dbo.signatures_general_assembly_completed_all_view WITH SCHEMABINDING as
SELECT signatures.teacher_id                                    AS teacher_id,
       MAX(signatures.general_assembly_updated_date)            AS last_signature_date,
       IIF(COUNT(*) >= 70, MIN(signatures.general_assembly), 0) AS times_completed_all
FROM dbo.signatures signatures
         JOIN dbo.studies studies ON signatures.study_id = studies.id
WHERE signatures.general_assembly > 0
  AND (studies.study_category_id = 3 or studies.study_category_id = 11)
GROUP BY signatures.teacher_id
go

