CREATE VIEW dbo.lost_sheep_view WITH SCHEMABINDING as
SELECT MAX(s.church_name)                           AS church_name,
       MAX(s.church_id)                             AS church_id,
       COUNT(CASE WHEN s.lost_sheep = 1 THEN 1 END) AS num_of_lost_sheep,
       COUNT(CASE
                 WHEN s.develop_relationship >= '2020-11-01' AND s.develop_relationship <= '2020-12-31'
                     THEN 1 END)                    AS num_of_contacted,
       COUNT(CASE
                 WHEN (s.visit_church >= '2020-11-01' AND s.visit_church <= '2020-12-31') OR
                      (s.visit_home >= '2020-11-01' AND s.visit_home <= '2020-12-31')
                     THEN 1 END)                    AS num_of_visited,
       COUNT(CASE
                 WHEN s.one_time_attendance >= '2020-11-01' AND s.develop_relationship <= '2020-12-31'
                     THEN 1 END)                    AS num_of_one_timer,
       COUNT(CASE
                 WHEN s.four_time_attendance >= '2020-11-01' AND s.develop_relationship <= '2020-12-31'
                     THEN 1 END)                    AS num_of_four_timer,
       COUNT(b.bible_study_count)                   AS bible_study_count
FROM dbo.students AS s
         LEFT JOIN (SELECT student_id, COUNT(id) AS bible_study_count
                    FROM dbo.bible_studies
                    WHERE date >= '2020-11-01'
                      AND date <= '2020-12-31'
                      AND attended = 1
                    GROUP BY student_id) b ON b.student_id = s.id
WHERE s.lost_sheep = 1
  AND s.archived = 0
GROUP BY s.church_id
go

