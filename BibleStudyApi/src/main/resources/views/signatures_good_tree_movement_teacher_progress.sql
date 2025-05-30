create view dbo.signatures_good_tree_movement_teacher_progress WITH SCHEMABINDING as
select COUNT_BIG(*) as tmp,
       teacher_id,
       teacher_name,
       SUM(IIF(general_assembly >= 1, 1, 0)) as ga_signature_count,
       SUM(IIF(ready >= 1, 1, 0))            as r_signature_count,
       SUM(IIF(teaching >= 1, 1, 0))         as t_signature_count
from dbo.signatures
where general_assembly_updated_date >= '2020-08-01' and general_assembly_updated_date <= '2021-02-28'
group by teacher_id, teacher_name
go

create unique clustered index IDX_V1
    on signatures_good_tree_movement_teacher_progress (teacher_id, teacher_name)
go

