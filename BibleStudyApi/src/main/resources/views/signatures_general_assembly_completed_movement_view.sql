CREATE VIEW dbo.signatures_general_assembly_completed_movement_view WITH SCHEMABINDING as
SELECT signatures.teacher_id                                                                 AS teacher_id,
       SUM(signatures.general_assembly)                                                      AS total_count,
       COUNT_BIG(CASE WHEN studies.study_category_id = 3 AND studies.book_number = 1 THEN 1 END) AS level_one_completed,
       COUNT_BIG(CASE WHEN studies.study_category_id = 3 AND studies.book_number = 2 THEN 1 END) AS level_two_completed,
       COUNT_BIG(CASE WHEN studies.study_category_id = 3 AND studies.book_number = 3 THEN 1 END) AS level_three_completed,
       COUNT_BIG(CASE WHEN studies.study_category_id = 3 AND studies.book_number = 4 THEN 1 END) AS level_four_completed,
       COUNT_BIG(CASE WHEN studies.study_category_id = 3 AND studies.book_number = 5 THEN 1 END) AS level_five_completed,
       COUNT_BIG(CASE WHEN studies.study_category_id = 11 THEN 1 END)                            AS staff_one_completed,
       COUNT_BIG(*)                                                                              AS total_completed
FROM dbo.signatures signatures
         JOIN dbo.studies studies ON signatures.study_id = studies.id
WHERE signatures.general_assembly > 0
  AND signatures.general_assembly_updated_date >= '2020-08-01'
  AND (studies.study_category_id = 3 or studies.study_category_id = 11)
GROUP BY signatures.teacher_id
go

create unique clustered index IDX_V1
    on signatures_general_assembly_completed_movement_view (teacher_id)
go

