CREATE VIEW dbo.bible_studies_display_materialized_view WITH SCHEMABINDING as
SELECT b.id                                      AS id,
       b.date                                    AS date,
       b.time                                    AS time,
       c.id                                      AS student_id,
       CONCAT_WS(' ', c.first_name, c.last_name) AS student_display_name,
       study.id                                  AS study_id,
       study.book_number                         AS study_book_number,
       study.chapter_number                      AS study_chapter_number,
       study.title                               AS study_title,
       step.id                                   AS step_id,
       step.display_name                         AS step_display_name,
       step.translation_key                      AS step_translation_key,
       step.name                                 AS step_name,
       p.updated_date_time                       AS step_updated_date_time,
       b.teacher_id                              AS teacher_id
FROM dbo.bible_studies b
         JOIN dbo.bible_studies_progress p on b.id = p.bible_study_id
         JOIN dbo.bible_studies_steps step ON p.bible_study_step_id = step.id
         JOIN dbo.students c ON b.student_id = c.id
         JOIN dbo.studies study ON b.study_id = study.id
go

create unique clustered index IDX_V1
    on dbo.bible_studies_display_materialized_view (id)
go

