CREATE VIEW dbo.signatures_r_grader_view WITH SCHEMABINDING as
SELECT CONCAT_WS('-', s.id, 'R') AS id,
       s.id AS signature_id,
       s.teacher_id,
       s.teacher_name,
       s.teacher_church_name,
       s.study_id,
       t.book_number,
       t.chapter_number,
       t.title,
       'R' AS grade_type,
       s.ready_grader_id AS grader_id,
       s.ready_grader_name AS grader_name,
       s.ready_updated_date AS grader_date
FROM dbo.signatures s
    JOIN dbo.studies t on t.id = s.study_id
    WHERE s.ready_grader_id IS NOT NULL
go

create unique clustered index IDX_V1
    on signatures_r_grader_view (study_id, teacher_id)
go

