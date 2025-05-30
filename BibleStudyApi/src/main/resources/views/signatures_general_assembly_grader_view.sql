CREATE VIEW dbo.signatures_general_assembly_grader_view WITH SCHEMABINDING as
SELECT s.id,
       s.teacher_id,
       s.teacher_name,
       s.study_id,
       t.book_number,
       t.chapter_number,
       t.title,
       'GA'                            AS grade_type,
       s.general_assembly_grader_id    AS grader_id,
       s.general_assembly_grader_name  AS grader_name,
       s.general_assembly_updated_date AS grader_date
FROM dbo.signatures s
         JOIN dbo.studies t on t.id = s.study_id
WHERE s.general_assembly_grader_id IS NOT NULL
go

