CREATE view dbo.event_church_view as
select c.id,
       c.archived,
       c.hidden,
       c.name,
       c.leader_id,
       c.state_id
from zionusa_management_api.dbo.churches c
where c.archived = 0
go
