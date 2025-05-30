CREATE VIEW dbo.signatures_grader_view WITH SCHEMABINDING as
SELECT ga.id,
       ga.signature_id,
       ga.teacher_id,
       ga.teacher_name,
       ga.teacher_church_name,
       ga.study_id,
       ga.book_number,
       ga.chapter_number,
       ga.title,
       ga.grade_type,
       ga.grader_id,
       ga.grader_name,
       ga.grader_date
FROM dbo.signatures_ga_grader_view ga
UNION
SELECT r.id,
       r.signature_id,
       r.teacher_id,
       r.teacher_name,
       r.teacher_church_name,
       r.study_id,
       r.book_number,
       r.chapter_number,
       r.title,
       r.grade_type,
       r.grader_id,
       r.grader_name,
       r.grader_date
FROM dbo.signatures_r_grader_view r
UNION
SELECT t.id,
       t.signature_id,
       t.teacher_id,
       t.teacher_name,
       t.teacher_church_name,
       t.study_id,
       t.book_number,
       t.chapter_number,
       t.title,
       t.grade_type,
       t.grader_id,
       t.grader_name,
       t.grader_date
FROM dbo.signatures_t_grader_view t
go

