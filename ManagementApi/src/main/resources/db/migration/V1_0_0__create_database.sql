if not exists (select * from sys.tables where name = 'accesses') BEGIN
  create table accesses
  (
    id int identity
      primary key,
    name varchar(20),
    sort_order smallint,
  )

  INSERT INTO accesses (id, name, sort_order) VALUES (1, 'Admin', 1);
  INSERT INTO accesses (id, name, sort_order) VALUES (2, 'Church', 3);
  INSERT INTO accesses (id, name, sort_order) VALUES (3, 'Group', 4);
  INSERT INTO accesses (id, name, sort_order) VALUES (4, 'Team', 5);
  INSERT INTO accesses (id, name, sort_order) VALUES (5, 'Member', 6);
  INSERT INTO accesses (id, name, sort_order) VALUES (6, 'Overseer', 2);
END
go

if not exists (select * from sys.tables where name = 'application_roles')
  create table application_roles
  (
    id int identity
      constraint application_roles_id_pk
      primary key,
    name varchar(50),
    displayName VARCHAR(100),
    description varchar(200)
  )
go

if not exists (select * from sys.tables where name = 'applications')
  create table applications
  (
    id int identity
      primary key,
    name varchar(100),
    active bit default 1 not null,
    apple_app_store_url varchar(300),
    google_play_store_url varchar(300),
    icon_title varchar(50),
    icon_url varchar(200),
    launch_url varchar(200),
  )
go

if not exists (select * from sys.tables where name='church_organization') BEGIN
  create table church_organization
  (
    id int identity
      primary key,
    church_id int not null,
    organization_data text,
  )
  create unique index church_organization_church_id_uindex
    on church_organization (church_id)

  END
go

if not exists (select * from sys.tables where name='church_pictures')
create table church_pictures
  (
    id int identity
      constraint PK__church_p__3213E83FAC14703A
      primary key,
    church_id int not null,
    type_id int default 0 not null,
    name varchar(100),
    description varchar(100),
    picture_url_full varchar(200),
    thumbnail_url varchar(200),
    picture_url_medium varchar(200)
  )
go

if not exists (select * from sys.tables where name='church_types') BEGIN
  create table church_types
  (
    id int identity
      primary key,
    name varchar(30) not null,
    sort_order smallint not null,
  )

  INSERT INTO church_types (id, name, sort_order) VALUES (1, 'Temple', 1);
  INSERT INTO church_types (id, name, sort_order) VALUES (2, 'Office Church', 2);
  INSERT INTO church_types (id, name, sort_order) VALUES (3, 'House Church', 3);
  INSERT INTO church_types (id, name, sort_order) VALUES (4, 'Study Location', 4);
END
go

if not exists (select * from sys.tables where name='churches') BEGIN
  create table churches
  (
    id int identity
      constraint PK__churches__3213E83F94F15BA6
      primary key,
    parent_church_id int
      constraint churches_churches_id_fk
      references churches,
    leader_id int,
    type_id int not null
      constraint churches_church_types_id_fk
      references church_types,
    state_id int not null,
    name varchar(50) not null,
    address varchar(100),
    phone varchar(15),
    email varchar(50),
    city varchar(50),
    latitude varchar(50),
    longitude varchar(50),
    postal_code varchar(20),
  )

  alter table church_pictures
    add constraint church_pictures_churches_id_fk
  foreign key (church_id) references churches
END
go

if not exists (select * from sys.tables where name='groups')
create table groups
  (
    id int identity
      constraint PK__groups__3213E83F8EDB124A
      primary key,
    church_id int not null
      constraint groups_churches_id_fk
      references churches,
    leader_id int,
    name varchar(50),
    church_group bit default 0 not null,
    archived bit default 0 not null,
    effective_date datetime2 default sysdatetime() not null,
  )
go

if not exists (select * from sys.tables where name='permissions')
  create table permissions
  (
    id int identity
      constraint PK__permissi__3213E83FF47C04AA
      primary key,
    reference int not null
      constraint chk_reference
      check ([reference]=2 OR [reference]=1 OR [reference]=0),
    reference_id int not null,
    access int,
    organization_level int
      constraint chk_organization_level
      check ([organization_level]=3 OR [organization_level]=2 OR [organization_level]=1 OR [organization_level]=0),
    organization_level_ids varchar(400),
    user_ids varchar(400),
    constraint permissions_reference_reference_id_pk
    unique (reference, reference_id)
  )
go

if not exists (select * from sys.tables where name='releases')
  create table releases
  (
    id int identity
      primary key,
    type varchar(20),
    version varchar(20),
    release_date date,
  )
go

if not exists (select * from sys.tables where name='roles') BEGIN
  create table roles
  (
    id int identity
      primary key,
    name varchar(20),
    sort_order smallint not null,
  )

  INSERT INTO roles (id, name, sort_order) VALUES (1, 'Overseer', 1);
  INSERT INTO roles (id, name, sort_order) VALUES (2, 'Church Leader', 2);
  INSERT INTO roles (id, name, sort_order) VALUES (3, 'Group Leader', 3);
  INSERT INTO roles (id, name, sort_order) VALUES (4, 'Team Leader', 4);
  INSERT INTO roles (id, name, sort_order) VALUES (5, 'Member', 5);
END
go

if not exists (select * from sys.tables where name='states') BEGIN
  create table states
  (
    id int identity
      primary key,
    full_name varchar(32) not null,
    short_name varchar(8) not null,
  )

  INSERT INTO states (id, full_name, short_name) VALUES (1, 'Alabama', 'AL');
  INSERT INTO states (id, full_name, short_name) VALUES (2, 'Alaska', 'AK');
  INSERT INTO states (id, full_name, short_name) VALUES (3, 'Arizona', 'AZ');
  INSERT INTO states (id, full_name, short_name) VALUES (4, 'Arkansas', 'AR');
  INSERT INTO states (id, full_name, short_name) VALUES (5, 'California', 'CA');
  INSERT INTO states (id, full_name, short_name) VALUES (6, 'Colorado', 'CO');
  INSERT INTO states (id, full_name, short_name) VALUES (7, 'Connecticut', 'CT');
  INSERT INTO states (id, full_name, short_name) VALUES (8, 'Delaware', 'DE');
  INSERT INTO states (id, full_name, short_name) VALUES (9, 'District of Columbia', 'DC');
  INSERT INTO states (id, full_name, short_name) VALUES (10, 'Florida', 'FL');
  INSERT INTO states (id, full_name, short_name) VALUES (11, 'Georgia', 'GA');
  INSERT INTO states (id, full_name, short_name) VALUES (12, 'Hawaii', 'HI');
  INSERT INTO states (id, full_name, short_name) VALUES (13, 'Idaho', 'ID');
  INSERT INTO states (id, full_name, short_name) VALUES (14, 'Illinois', 'IL');
  INSERT INTO states (id, full_name, short_name) VALUES (15, 'Indiana', 'IN');
  INSERT INTO states (id, full_name, short_name) VALUES (16, 'Iowa', 'IA');
  INSERT INTO states (id, full_name, short_name) VALUES (17, 'Kansas', 'KS');
  INSERT INTO states (id, full_name, short_name) VALUES (18, 'Kentucky', 'KY');
  INSERT INTO states (id, full_name, short_name) VALUES (19, 'Louisiana', 'LA');
  INSERT INTO states (id, full_name, short_name) VALUES (20, 'Maine', 'ME');
  INSERT INTO states (id, full_name, short_name) VALUES (21, 'Maryland', 'MD');
  INSERT INTO states (id, full_name, short_name) VALUES (22, 'Massachusetts', 'MA');
  INSERT INTO states (id, full_name, short_name) VALUES (23, 'Michigan', 'MI');
  INSERT INTO states (id, full_name, short_name) VALUES (24, 'Minnesota', 'MN');
  INSERT INTO states (id, full_name, short_name) VALUES (25, 'Mississippi', 'MS');
  INSERT INTO states (id, full_name, short_name) VALUES (26, 'Montana', 'MT');
  INSERT INTO states (id, full_name, short_name) VALUES (27, 'Nebraska', 'NE');
  INSERT INTO states (id, full_name, short_name) VALUES (28, 'Nevada', 'NV');
  INSERT INTO states (id, full_name, short_name) VALUES (29, 'New Hampshire', 'NH');
  INSERT INTO states (id, full_name, short_name) VALUES (30, 'New Jersey', 'NJ');
  INSERT INTO states (id, full_name, short_name) VALUES (31, 'New Mexico', 'NM');
  INSERT INTO states (id, full_name, short_name) VALUES (32, 'New York', 'NY');
  INSERT INTO states (id, full_name, short_name) VALUES (33, 'North Carolina', 'NC');
  INSERT INTO states (id, full_name, short_name) VALUES (34, 'North Dakota', 'ND');
  INSERT INTO states (id, full_name, short_name) VALUES (35, 'Ohio', 'OH');
  INSERT INTO states (id, full_name, short_name) VALUES (36, 'Oklahoma', 'OK');
  INSERT INTO states (id, full_name, short_name) VALUES (37, 'Oregon', 'OR');
  INSERT INTO states (id, full_name, short_name) VALUES (38, 'Pennsylvania', 'PA');
  INSERT INTO states (id, full_name, short_name) VALUES (39, 'Rhode Island', 'RI');
  INSERT INTO states (id, full_name, short_name) VALUES (40, 'South Carolina', 'SC');
  INSERT INTO states (id, full_name, short_name) VALUES (41, 'South Dakota', 'SD');
  INSERT INTO states (id, full_name, short_name) VALUES (42, 'Tennessee', 'TN');
  INSERT INTO states (id, full_name, short_name) VALUES (43, 'Texas', 'TX');
  INSERT INTO states (id, full_name, short_name) VALUES (44, 'Utah', 'UT');
  INSERT INTO states (id, full_name, short_name) VALUES (45, 'Vermont', 'VT');
  INSERT INTO states (id, full_name, short_name) VALUES (46, 'Virginia', 'VA');
  INSERT INTO states (id, full_name, short_name) VALUES (47, 'Washington', 'WA');
  INSERT INTO states (id, full_name, short_name) VALUES (48, 'West Virginia', 'WV');
  INSERT INTO states (id, full_name, short_name) VALUES (49, 'Wisconsin', 'WI');
  INSERT INTO states (id, full_name, short_name) VALUES (50, 'Wyoming', 'WY');
  INSERT INTO states (id, full_name, short_name) VALUES (51, 'Bahamas', 'BA');
  INSERT INTO states (id, full_name, short_name) VALUES (52, 'Trinidad & Tobago', 'TT');
  INSERT INTO states (id, full_name, short_name) VALUES (53, 'Haiti', 'HI');
  INSERT INTO states (id, full_name, short_name) VALUES (54, 'Dominican Republic', 'DR');
  INSERT INTO states (id, full_name, short_name) VALUES (55, 'Puerto Rico', 'PR');
  INSERT INTO states (id, full_name, short_name) VALUES (56, 'Barbados', 'BB');
  INSERT INTO states (id, full_name, short_name) VALUES (57, 'Jamaica', 'JA');
  INSERT INTO states (id, full_name, short_name) VALUES (58, ' Naples (Italy)', 'IT');
END
go

if not exists (select * from sys.tables where name='teams')
  create table teams
  (
    id int identity
      constraint PK__teams__3213E83F7DB5DDAE
      primary key,
    group_id int not null
      constraint teams_groups_id_fk
      references groups,
    leader_id int,
    name varchar(50) not null,
    church_team bit default 0 not null,
    effective_date datetime2 default sysdatetime(),
    archived bit default 0 not null,
    church_id int,
    church_group bit
  )
go

if not exists (select * from sys.tables where name='titles') BEGIN
  create table titles
  (
    id int identity
      primary key,
    name varchar(20),
    display_name varchar(5),
    sort_order smallint,
  )

  INSERT INTO titles (id, name, display_name, sort_order) VALUES (1, 'Pastor', 'P.', 1);
  INSERT INTO titles (id, name, display_name, sort_order) VALUES (2, 'Elder', 'E.', 2);
  INSERT INTO titles (id, name, display_name, sort_order) VALUES (3, 'Senior Deaconess', 'K.', 3);
  INSERT INTO titles (id, name, display_name, sort_order) VALUES (4, 'Missionary', 'M.', 4);
  INSERT INTO titles (id, name, display_name, sort_order) VALUES (5, 'Deacon', 'D.', 5);
  INSERT INTO titles (id, name, display_name, sort_order) VALUES (6, 'Deaconess', 'D.', 6);
  INSERT INTO titles (id, name, display_name, sort_order) VALUES (7, 'Brother', 'B.', 7);
  INSERT INTO titles (id, name, display_name, sort_order) VALUES (8, 'Sister', 'S.', 8);
END
go

if not exists (select * from sys.tables where name='transfer_request_history')
  create table transfer_request_history
  (
    id int identity
      primary key,
    user_id int,
    current_group_id int not null,
    current_group_name varchar(50),
    current_church_id int not null,
    current_church_name varchar(50),
    new_group_id int not null,
    new_group_name varchar(50),
    new_church_id int not null,
    new_church_name varchar(50),
    request_status varchar(2) default 'P',
    request_date datetime2 default sysdatetime(),
    comment varchar(300),
    member_name varchar(60),
    reviewer_id int,
    reviewer_name varchar(50)
  )
go

if not exists (select * from sys.tables where name='transfer_requests')
  create table transfer_requests
  (
    id int identity
      primary key,
    user_id int,
    current_team_id int,
    current_team_name varchar(50),
    current_group_id int not null,
    current_group_name varchar(50),
    current_church_id int not null,
    current_church_name varchar(50),
    new_team_id int not null
      constraint transfer_requests_teams_id_fk
      references teams,
    new_team_name varchar(50) not null,
    new_group_id int not null
      constraint transfer_requests_new_group_id_fk
      references groups,
    new_group_name varchar(50),
    new_church_id int not null
      constraint transfer_requests_new_church_id_fk
      references churches,
    new_church_name varchar(50),
    request_status varchar(2) default 'P',
    request_date datetime2 default sysdatetime(),
    comment varchar(300),
    member_name varchar(60),
    reviewer_id int,
    reviewer_name varchar(50)
  )
  go

CREATE OR ALTER procedure [dbo].[update_user_association_information] AS
BEGIN
SET NOCOUNT ON;
DECLARE @churchId INT, @groupId INT, @teamId INT, @userId INT;
DECLARE @churchName VARCHAR(75), @groupName VARCHAR(75), @teamName VARCHAR(75);

--cursor for users table
DECLARE users_table CURSOR LOCAL FOR select id, team_id from users

                                                             OPEN users_table;

FETCH NEXT FROM users_table INTO @userId ,@teamId

WHILE @@FETCH_STATUS = 0
BEGIN
select @groupId = group_id, @teamName = name from teams where id = @teamId;
select @churchId = church_id, @groupName = name from groups where id = @groupId;
select @churchName = name from churches where id = @churchId;
update users set team_name = @teamName, group_id = @groupId, group_name = @groupName, church_id = @churchId, church_name = @churchName
where id = @userId;

FETCH NEXT FROM users_table INTO @userId ,@teamId
END

CLOSE users_table;
DEALLOCATE users_table;

END
go

if not exists (select * from sys.tables where name='user_application_roles')
create table user_application_roles
  (
    id int identity,
    user_id int not null,
    application_role_id int not null
      constraint user_application_roles_application_roles_id_fk
      references application_roles,
  )
go

if not exists (select * from sys.tables where name='user_pictures') BEGIN
  create table user_pictures
  (
    id int identity
      constraint PK__user_pic__3213E83F787588CB
      primary key,
    user_id int not null,
    name varchar(100),
    description varchar(100),
    picture_url_full varchar(200),
    thumbnail_url varchar(200),
    picture_url_medium varchar(200)
  )

create unique index user_pictures_user_id_uindex
  on user_pictures (user_id)

END
go



if not exists (select * from sys.tables where name='user_profile_categories') BEGIN
  create table user_profile_categories
  (
    id int identity
      primary key,
    category varchar(50),
  )

  INSERT INTO user_profile_categories (id, category) VALUES (1, 'Church Activity');
  INSERT INTO user_profile_categories (id, category) VALUES (2, 'Church Position');
  INSERT INTO user_profile_categories (id, category) VALUES (3, 'Service Duty');
  INSERT INTO user_profile_categories (id, category) VALUES (4, 'Professional Skill');
  INSERT INTO user_profile_categories (id, category) VALUES (5, 'Technical Skill');
  INSERT INTO user_profile_categories (id, category) VALUES (6, 'Talent');
  INSERT INTO user_profile_categories (id, category) VALUES (7, 'Tithe');
  INSERT INTO user_profile_categories (id, category) VALUES (8, 'Incident Report');
  INSERT INTO user_profile_categories (id, category) VALUES (10, 'Previous Churches');
  INSERT INTO user_profile_categories (id, category) VALUES (11, 'Language');
END
go

if not exists (select * from sys.tables where name='user_profile_information') BEGIN
create table user_profile_information
  (
    id int identity
      constraint PK__user_pro__3213E83FFADAE4E4
      primary key,
    profile_id int,
    category_id int
      constraint user_profile_information_user_profile_categories_id_fk
      references user_profile_categories,
    information varchar(70),
    start_date date,
    end_date date,
  )

  create unique index user_profile_fields_id_uindex
    on user_profile_information (id)

END
go


if not exists (select * from sys.tables where name='user_profiles') BEGIN
  create table user_profiles
  (
    id int identity
      constraint PK__user_pro__3213E83F00A70A33
      primary key,
    user_id int not null,
    church_id int
      constraint user_profiles_churches_id_fk
      references churches,
    gospel_aspirations varchar(100),
    gospel_plan varchar(100),
    company varchar(100),
    certifications varchar(100),
    desired_field varchar(100),
    job_title varchar(100),
    occupation varchar(100),
    professional_licenses varchar(100),
    type_of_business varchar(100),
    bachelors_degree varchar(100),
    bachelors_school varchar(100),
    masters_degree varchar(100),
    masters_school varchar(100),
    doctorate_degree varchar(100),
    doctorate_school varchar(100),
    children int default 0 not null,
    ethnicity varchar(100),
    hobbies varchar(100),
    personality varchar(100),
    iba varchar(50),
    iba_organization varchar(100),
    current_job_duration varchar(20),
    university varchar(75),
    university_position varchar(75),
    faith_level varchar(25)
  )

  create unique index user_profiles_user_id_uindex
    on user_profiles (user_id)

  alter table user_profile_information
    add constraint user_profile_information_user_profiles_id_fk
  foreign key (profile_id) references user_profiles
END
go

if not exists (select * from sys.tables where name='users') BEGIN
  create table users
  (
    id int identity
      constraint users_id_pk
      primary key,
    active_directory_id varchar(100),
    parent_church_id int,
    church_id int,
    group_id int,
    team_id int
      constraint users_teams_id_fk
      references teams,
    access_id int
      constraint users_accesses_id_fk
      references accesses,
    spouse_id int,
    role_id int
      constraint users_roles_id_fk
      references roles,
    title_id int
      constraint users_titles_id_fk
      references titles,
    picture_id int,
    first_name varchar(45),
    middle_name varchar(45),
    last_name varchar(45),
    user_name varchar(65),
    gender varchar(2),
    birthday date,
    baptism_date date,
    church_name varchar(75),
    group_name varchar(75),
    team_name varchar(75),
    archived bit default 0,
    enabled bit default 1,
    married bit default 0,
    suffix varchar(20),
    prefix varchar(20),
    account_not_expired bit default 1,
    credentials_not_expired bit default 1,
    account_not_locked bit default 1,
    parent_church_name varchar(100),
    gospel_worker bit default 1,
    theological_student bit default 0 not null
  )

  alter table user_application_roles
    add constraint user_application_roles_users_id_fk
  foreign key (user_id) references users
END
go
