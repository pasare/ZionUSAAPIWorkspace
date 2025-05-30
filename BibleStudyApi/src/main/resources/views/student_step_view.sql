CREATE view dbo.student_step_view WITH SCHEMABINDING as
SELECT s.id                                  AS id,
       s.church_name                         AS church_name,
       s.first_name                          AS first_name,
       s.last_name                           AS last_name,
       s.lost_sheep                          AS lost_sheep,
       s.gender                              AS gender,
       CONCAT_WS(', ', s.city, s.state_name) AS location,
       s.picture_url                         AS picture_url,
       s.relationship_type                   AS relationship_type,
       COUNT(DISTINCT b.study_id)            AS study_count,
       CASE
           WHEN NULLIF(s.become_evangelist, '') IS NOT NULL THEN 18
           WHEN NULLIF(s.four_time_attendance, '') IS NOT NULL THEN 17
           WHEN NULLIF(s.one_time_attendance, '') IS NOT NULL THEN 16
           WHEN NULLIF(s.baptism_date, '') IS NOT NULL THEN 15
           WHEN NULLIF(s.visit_church, '') IS NOT NULL THEN 14
           WHEN NULLIF(s.visit_home, '') IS NOT NULL THEN 13
           WHEN NULLIF(s.develop_relationship, '') IS NOT NULL AND NULLIF(s.introduce_church, '') IS NOT NULL AND
                COUNT(b.attended) >= 10 THEN 12
           WHEN NULLIF(s.develop_relationship, '') IS NOT NULL AND NULLIF(s.introduce_church, '') IS NOT NULL AND
                COUNT(b.attended) = 9 THEN 11
           WHEN NULLIF(s.develop_relationship, '') IS NOT NULL AND NULLIF(s.introduce_church, '') IS NOT NULL AND
                COUNT(b.attended) = 8 THEN 10
           WHEN NULLIF(s.develop_relationship, '') IS NOT NULL AND NULLIF(s.introduce_church, '') IS NOT NULL AND
                COUNT(b.attended) = 7 THEN 9
           WHEN NULLIF(s.develop_relationship, '') IS NOT NULL AND NULLIF(s.introduce_church, '') IS NOT NULL AND
                COUNT(b.attended) = 6 THEN 8
           WHEN NULLIF(s.develop_relationship, '') IS NOT NULL AND NULLIF(s.introduce_church, '') IS NOT NULL AND
                COUNT(b.attended) = 5 THEN 7
           WHEN NULLIF(s.develop_relationship, '') IS NOT NULL AND NULLIF(s.introduce_church, '') IS NOT NULL AND
                COUNT(b.attended) = 4 THEN 6
           WHEN NULLIF(s.develop_relationship, '') IS NOT NULL AND NULLIF(s.introduce_church, '') IS NOT NULL AND
                COUNT(b.attended) = 3 THEN 5
           WHEN NULLIF(s.develop_relationship, '') IS NOT NULL AND NULLIF(s.introduce_church, '') IS NOT NULL AND
                COUNT(b.attended) = 2 THEN 4
           WHEN NULLIF(s.develop_relationship, '') IS NOT NULL AND NULLIF(s.introduce_church, '') IS NOT NULL AND
                COUNT(b.attended) = 1 THEN 3
           WHEN NULLIF(s.introduce_church, '') IS NOT NULL THEN 2
           WHEN NULLIF(s.develop_relationship, '') IS NOT NULL THEN 1
           ELSE 0
           END                               as step,
       CASE
           WHEN NULLIF(s.become_evangelist, '') IS NOT NULL THEN 8
           WHEN NULLIF(s.four_time_attendance, '') IS NOT NULL THEN 7
           WHEN NULLIF(s.one_time_attendance, '') IS NOT NULL THEN 6
           WHEN NULLIF(s.baptism_date, '') IS NOT NULL THEN 5
           WHEN NULLIF(s.visit_church, '') IS NOT NULL AND NULLIF(s.visit_home, '') IS NOT NULL THEN 4
           WHEN NULLIF(s.develop_relationship, '') IS NOT NULL AND NULLIF(s.introduce_church, '') IS NOT NULL AND
                COUNT(b.attended) > 0 THEN 3
           WHEN NULLIF(s.introduce_church, '') IS NOT NULL THEN 2
           ELSE 1
           END                               AS display_step,
       COALESCE(
               NULLIF(s.become_evangelist, ''),
               NULLIF(s.four_time_attendance, ''),
               NULLIF(s.one_time_attendance, ''),
               NULLIF(s.baptism_date, ''),
               NULLIF(s.visit_church, ''),
               NULLIF(s.visit_home, ''),
               NULLIF(s.introduce_church, ''),
               NULLIF(s.develop_relationship, '')
           )                                 AS step_date,
       s.develop_relationship,
       s.user_display_name1,
       s.user_display_name2,
       s.user_display_name3,
       s.user_id1,
       s.user_id2,
       s.user_id3
FROM dbo.students s
         LEFT JOIN dbo.bible_studies b ON b.student_id = s.id AND b.attended = 1
         LEFT JOIN dbo.studies t ON b.study_id = t.id
    AND t.book_number = 1
    AND t.study_category_id = 3
WHERE NULLIF(s.develop_relationship, '') IS NOT NULL AND s.archived = 0
GROUP BY s.id,
         s.baptism_date,
         s.become_evangelist,
         s.church_name,
         s.city,
         s.develop_relationship,
         s.first_name,
         s.four_time_attendance,
         s.gender,
         s.introduce_church,
         s.last_name,
         s.lost_sheep,
         s.one_time_attendance,
         s.state_name,
         s.picture_url,
         s.relationship_type,
         s.visit_church,
         s.visit_home,
         s.user_display_name1,
         s.user_display_name2,
         s.user_display_name3,
         s.user_id1,
         s.user_id2,
         s.user_id3
go

