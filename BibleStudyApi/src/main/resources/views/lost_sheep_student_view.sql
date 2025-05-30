CREATE VIEW dbo.lost_sheep_student_view WITH SCHEMABINDING as
SELECT s.id                                    AS id,
       s.baptism_date                          AS baptism_date,
       s.church_id                             AS church_id,
       s.church_name                           AS church_name,
       s.city                                  AS city,
       CASE
           WHEN s.develop_relationship >= '2020-11-01' AND s.develop_relationship <= '2020-12-31'
               THEN s.develop_relationship END AS develop_relationship,
       s.first_name                            AS first_name,
       CASE
           WHEN s.four_time_attendance >= '2020-11-01' AND s.four_time_attendance <= '2020-12-31'
               THEN s.four_time_attendance END AS four_time_attendance,
       s.gender                                AS gender,
       s.last_name                             AS last_name,
       s.middle_name                           AS middle_name,
       CASE
           WHEN s.one_time_attendance >= '2020-11-01' AND s.one_time_attendance <= '2020-12-31'
               THEN s.one_time_attendance END  AS one_time_attendance,
       s.picture_url                           AS picture_url,
       s.relationship_type AS relationship_type,
       s.state_name                            AS state_name,
       COUNT(b.bible_study_count)              AS studies_completed,
       COUNT(b.bible_study_count) * 100 / 50   AS studies_percentage,
       CASE
           WHEN s.visit_church >= '2020-11-01' AND s.visit_church <= '2020-12-31'
               THEN s.visit_church END         AS visit_church,
       CASE
           WHEN s.visit_home >= '2020-11-01' AND s.visit_home <= '2020-12-31'
               THEN s.visit_home END           AS visit_home
FROM dbo.students AS s
         LEFT JOIN (SELECT student_id, COUNT(id) AS bible_study_count
                    FROM dbo.bible_studies
                    WHERE date >= '2020-11-01'
                      AND date <= '2020-12-31'
                      AND attended = 1
                    GROUP BY student_id) b
                   ON b.student_id = s.id
WHERE s.lost_sheep = 1
  AND s.archived = 0
GROUP BY s.baptism_date,
         s.church_id,
         s.church_name,
         s.city,
         s.develop_relationship,
         s.first_name,
         s.four_time_attendance,
         s.gender,
         s.id,
         s.last_name,
         s.middle_name,
         s.one_time_attendance,
         s.picture_url,
         s.relationship_type,
         s.state_name,
         s.visit_church,
         s.visit_home
go

