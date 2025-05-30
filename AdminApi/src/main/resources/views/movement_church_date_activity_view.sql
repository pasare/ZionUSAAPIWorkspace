create view dbo.movement_church_date_activity_view WITH SCHEMABINDING as
select newid()               as id,
       a.id                  as activity_id,
       a.name                as activity_name,
       m.id                  as movement_id,
       m.name                as movement_name,
       m.start_date          as start_date,
       m.end_date            as end_date,
       al.church_id          as church_id,
       al.church_name        as church_name,
       al.date               as activity_date,
       count(al.activity_id) as activity_count,
       sum(a.points)         as activity_points
from dbo.activity_logs al
         join dbo.movements m on al.movement_id = m.id
         join dbo.activities a on al.activity_id = a.id
group by m.id, m.name, m.start_date, m.end_date, al.church_id, al.church_name, al.date, a.id, a.name
go
