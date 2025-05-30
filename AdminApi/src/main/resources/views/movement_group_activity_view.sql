create view dbo.movement_group_activity_view WITH SCHEMABINDING as
select newid()               as id,
       a.id                  as activity_id,
       a.name                as activity_name,
       m.id                  as movement_id,
       m.name                as movement_name,
       m.start_date          as start_date,
       m.end_date            as end_date,
       al.group_id           as group_id,
       al.group_name         as group_name,
       count(al.activity_id) as activity_count,
       sum(a.points)         as activity_points
from dbo.activity_logs al
         join dbo.movements m on al.movement_id = m.id
         join dbo.activities a on al.activity_id = a.id
group by a.id, a.name, m.id, m.name, m.start_date, m.end_date, al.group_id, al.group_name
go

