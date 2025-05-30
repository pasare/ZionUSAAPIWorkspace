-- first copy the soj tables into the target db, then append migration_soj
--- Migration Script from  christah_shout Churches table
---  manually disable all the constraints
alter TABLE accesses NOCHECK CONSTRAINT all;
alter TABLE church_types NOCHECK CONSTRAINT all;
ALTER table roles NOCHECK CONSTRAINT ALL;
ALTER table states NOCHECK CONSTRAINT all;
alter table churches NOCHECK CONSTRAINT all;
alter TABLE church_organization NOCHECK CONSTRAINT all;
alter TABLE church_pictures NOCHECK CONSTRAINT All;
alter TABLE groups NOCHECK CONSTRAINT ALL
alter TABLE teams NOCHECK CONSTRAINT ALL;
alter table users NOCHECK CONSTRAINT ALL;
ALTER TABLE user_pictures NOCHECK CONSTRAINT ALL;
alter TABLE transfer_requests NOCHECK CONSTRAINT all;
alter TABLE transfer_request_history NOCHECK CONSTRAINT ALL;
ALTER TABLE titles NOCHECK CONSTRAINT ALL;

--- delete churches and copy from Shout of Joshua table
delete from churches;
set IDENTITY_INSERT churches ON;
insert INTO churches (id, parent_church_id, leader_id, name, type_id, address, phone, email, city, state, state_id, latitude, longitude, postal_code, created_date, updated_date)
  select id, parent_church_id, leader_id, name, type_id, address, phone, email, city, 'none', state_id, latitude, longitude, postal_code, created_date, updated_date from churches_migration_soj;
set IDENTITY_INSERT churches OFF;


----Migration Script of christah_shout Group table
--- manually disable all the constraints


delete from groups;
set IDENTITY_INSERT groups ON;
insert INTO groups (id, church_id, leader_id, church_group, effective_date, created_date, updated_date, name, archived)
  select id, church_id, leader_id, church_group, effective_date, created_date, updated_date, name, archived from groups_migration_soj ;
set IDENTITY_INSERT groups OFF;


--- Migration from christah_shout teams


delete from teams;
set IDENTITY_INSERT teams ON;
insert INTO teams (id, group_id, leader_id, name, church_team, effective_date, created_date, updated_date, archived)
  select id, id,  null, name, church_group, effective_date, created_date, updated_date, archived from groups_migration_soj;
set IDENTITY_INSERT teams OFF;


--- Migration from christah_shout users


delete from users;
set IDENTITY_INSERT users ON;
insert INTO users (	id, team_id, access_id, role_id, title_id, first_name, middle_name, last_name, user_name, gender, archived, enabled, account_not_expired, credentials_not_expired, account_not_locked, created_date, updated_date)
  select 	id, group_id, access_id, role_id, title_id, first_name, middle_name, last_name,user_name, gender, archived, enabled, account_not_expired, credentials_not_expired, account_not_locked, created_date, updated_date
  from users_migration_soj;
set IDENTITY_INSERT users OFF;

--- enable the constraints
alter table churches CHECK CONSTRAINT all;
alter table church_pictures CHECK CONSTRAINT ALL;
alter TABLE groups CHECK CONSTRAINT ALL
alter TABLE teams CHECK CONSTRAINT ALL;
alter table users CHECK CONSTRAINT ALL;
alter table user_pictures CHECK CONSTRAINT ALL;
ALTER table states CHECK CONSTRAINT all;
alter TABLE accesses CHECK CONSTRAINT all;
alter TABLE church_types CHECK CONSTRAINT all;
ALTER table roles CHECK CONSTRAINT ALL;
ALTER TABLE titles CHECK CONSTRAINT ALL
alter TABLE transfer_requests CHECK CONSTRAINT ALL;
alter TABLE transfer_request_history CHECK CONSTRAINT ALL;

--update the role mappings


update users set role_id = 1 where role_id = 6; --overseer
update users set role_id = 2 where role_id = 3; --church leader
update users set role_id = 3 where role_id = 4; --group leader


update church set type_id = 4 where type_id = 1; --house church temp
update churches set type_id = 1 where type_id = 3; --temples
update churches set type_id = 3 where type_id = 4; -- house churches

--- check to see if all constraints are enabled
select * from sys.foreign_keys

drop table churches_migration_soj;
drop table groups_migration_soj;
drop table users_migration_soj;


--  update title mapping
	update users
	set title_id = 8
	where title_id = 7;

	update users
	set title_id = 7
	where title_id = 6;

	update users
	set title_id =6
	where title_id = 5;

	update users
	set title_id = 5
	where title_id = 4;

	update users
	set title_id = 4
	where title_id = 3;


----Migration Script of christah_shout Group table
--- manually disable all the constraints