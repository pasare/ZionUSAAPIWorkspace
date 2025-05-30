-- This script is used to easily search for members to add to lre table with possible given info. Edit where clause to use script.

select u.id, udv.id, u.user_name, u.first_name, u.last_name, udv.display_name, u.church_id, u.church_name, u.parent_church_id, u.parent_church_name
from users u
       join users_display_view udv on u.user_name = udv.username
where u.user_name like '%%'
-- where u.recovery_email like '%%'
-- where u.first_name like '%%' and u.last_name like '%%'
-- where u.first_name like '%%'
-- where u.last_name like '%%'
