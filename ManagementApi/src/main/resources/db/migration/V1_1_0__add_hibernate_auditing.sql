IF NOT EXISTS (select * FROM sys.columns WHERE  object_id = OBJECT_ID(N'[dbo].[accesses]') AND name = 'created_by') BEGIN
  ALTER TABLE accesses add created_by varchar(100);
  ALTER TABLE accesses add created_date datetime2(7);
  ALTER TABLE accesses add last_modified_by varchar(100);
  ALTER TABLE accesses add last_modified_date datetime2(7);
END

IF NOT EXISTS (select * FROM sys.columns WHERE  object_id = OBJECT_ID(N'[dbo].[application_roles]') AND name = 'created_by') BEGIN
  ALTER TABLE application_roles add created_by varchar(100);
  ALTER TABLE application_roles add created_date datetime2(7);
  ALTER TABLE application_roles add last_modified_by varchar(100);
  ALTER TABLE application_roles add last_modified_date datetime2(7);
END

IF NOT EXISTS (select * FROM sys.columns WHERE  object_id = OBJECT_ID(N'[dbo].[churches]') AND name = 'created_by') BEGIN
  ALTER TABLE churches add created_by varchar(100);
  ALTER TABLE churches add created_date datetime2(7);
  ALTER TABLE churches add last_modified_by varchar(100);
  ALTER TABLE churches add last_modified_date datetime2(7);
END

IF NOT EXISTS (select * FROM sys.columns WHERE  object_id = OBJECT_ID(N'[dbo].[church_organization]') AND name = 'created_by') BEGIN
  ALTER TABLE church_organization add created_by varchar(100);
  ALTER TABLE church_organization add created_date datetime2(7);
  ALTER TABLE church_organization add last_modified_by varchar(100);
  ALTER TABLE church_organization add last_modified_date datetime2(7);
END

IF NOT EXISTS (select * FROM sys.columns WHERE  object_id = OBJECT_ID(N'[dbo].[church_pictures]') AND name = 'created_by') BEGIN
  ALTER TABLE church_pictures add created_by varchar(100);
  ALTER TABLE church_pictures add created_date datetime2(7);
  ALTER TABLE church_pictures add last_modified_by varchar(100);
  ALTER TABLE church_pictures add last_modified_date datetime2(7);
END

IF NOT EXISTS (select * FROM sys.columns WHERE  object_id = OBJECT_ID(N'[dbo].[church_types]') AND name = 'created_by') BEGIN
  ALTER TABLE church_types add created_by varchar(100);
  ALTER TABLE church_types add created_date datetime2(7);
  ALTER TABLE church_types add last_modified_by varchar(100);
  ALTER TABLE church_types add last_modified_date datetime2(7);
END

IF NOT EXISTS (select * FROM sys.columns WHERE  object_id = OBJECT_ID(N'[dbo].[groups]') AND name = 'created_by') BEGIN
  ALTER TABLE groups add created_by varchar(100);
  ALTER TABLE groups add created_date datetime2(7);
  ALTER TABLE groups add last_modified_by varchar(100);
  ALTER TABLE groups add last_modified_date datetime2(7);
END

IF NOT EXISTS (select * FROM sys.columns WHERE  object_id = OBJECT_ID(N'[dbo].[roles]') AND name = 'created_by') BEGIN
  ALTER TABLE roles add created_by varchar(100);
  ALTER TABLE roles add created_date datetime2(7);
  ALTER TABLE roles add last_modified_by varchar(100);
  ALTER TABLE roles add last_modified_date datetime2(7);
END

IF NOT EXISTS (select * FROM sys.columns WHERE  object_id = OBJECT_ID(N'[dbo].[states]') AND name = 'created_by') BEGIN
  ALTER TABLE states add created_by varchar(100);
  ALTER TABLE states add created_date datetime2(7);
  ALTER TABLE states add last_modified_by varchar(100);
  ALTER TABLE states add last_modified_date datetime2(7);
END

IF NOT EXISTS (select * FROM sys.columns WHERE  object_id = OBJECT_ID(N'[dbo].[teams]') AND name = 'created_by') BEGIN
  ALTER TABLE teams add created_by varchar(100);
  ALTER TABLE teams add created_date datetime2(7);
  ALTER TABLE teams add last_modified_by varchar(100);
  ALTER TABLE teams add last_modified_date datetime2(7);
END

IF NOT EXISTS (select * FROM sys.columns WHERE  object_id = OBJECT_ID(N'[dbo].[titles]') AND name = 'created_by') BEGIN
  ALTER TABLE titles add created_by varchar(100);
  ALTER TABLE titles add created_date datetime2(7);
  ALTER TABLE titles add last_modified_by varchar(100);
  ALTER TABLE titles add last_modified_date datetime2(7);
END

IF NOT EXISTS (select * FROM sys.columns WHERE  object_id = OBJECT_ID(N'[dbo].[transfer_requests]') AND name = 'created_by') BEGIN
  ALTER TABLE transfer_requests add created_by varchar(100);
  ALTER TABLE transfer_requests add created_date datetime2(7);
  ALTER TABLE transfer_requests add last_modified_by varchar(100);
  ALTER TABLE transfer_requests add last_modified_date datetime2(7);
END

IF NOT EXISTS (select * FROM sys.columns WHERE  object_id = OBJECT_ID(N'[dbo].[users]') AND name = 'created_by') BEGIN
  ALTER TABLE users add created_by varchar(100);
  ALTER TABLE users add created_date datetime2(7);
  ALTER TABLE users add last_modified_by varchar(100);
  ALTER TABLE users add last_modified_date datetime2(7);
END

IF NOT EXISTS (select * FROM sys.columns WHERE  object_id = OBJECT_ID(N'[dbo].[user_application_roles]') AND name = 'created_by') BEGIN
  ALTER TABLE user_application_roles add created_by varchar(100);
  ALTER TABLE user_application_roles add created_date datetime2(7);
  ALTER TABLE user_application_roles add last_modified_by varchar(100);
  ALTER TABLE user_application_roles add last_modified_date datetime2(7);
END

IF NOT EXISTS (select * FROM sys.columns WHERE  object_id = OBJECT_ID(N'[dbo].[user_pictures]') AND name = 'created_by') BEGIN
  ALTER TABLE user_pictures add created_by varchar(100);
  ALTER TABLE user_pictures add created_date datetime2(7);
  ALTER TABLE user_pictures add last_modified_by varchar(100);
  ALTER TABLE user_pictures add last_modified_date datetime2(7);
END

IF NOT EXISTS (select * FROM sys.columns WHERE  object_id = OBJECT_ID(N'[dbo].[user_profile_categories]') AND name = 'created_by') BEGIN
  ALTER TABLE user_profile_categories add created_by varchar(100);
  ALTER TABLE user_profile_categories add created_date datetime2(7);
  ALTER TABLE user_profile_categories add last_modified_by varchar(100);
  ALTER TABLE user_profile_categories add last_modified_date datetime2(7);
END

IF NOT EXISTS (select * FROM sys.columns WHERE  object_id = OBJECT_ID(N'[dbo].[user_profile_information]') AND name = 'created_by') BEGIN
  ALTER TABLE user_profile_information add created_by varchar(100);
  ALTER TABLE user_profile_information add created_date datetime2(7);
  ALTER TABLE user_profile_information add last_modified_by varchar(100);
  ALTER TABLE user_profile_information add last_modified_date datetime2(7);
END

IF NOT EXISTS (select * FROM sys.columns WHERE  object_id = OBJECT_ID(N'[dbo].[user_profiles]') AND name = 'created_by') BEGIN
  ALTER TABLE user_profiles add created_by varchar(100);
  ALTER TABLE user_profiles add created_date datetime2(7);
  ALTER TABLE user_profiles add last_modified_by varchar(100);
  ALTER TABLE user_profiles add last_modified_date datetime2(7);
END

IF NOT EXISTS (select * FROM sys.columns WHERE  object_id = OBJECT_ID(N'[dbo].[users]') AND name = 'created_by') BEGIN
  ALTER TABLE users add created_by varchar(100);
  ALTER TABLE users add created_date datetime2(7);
  ALTER TABLE users add last_modified_by varchar(100);
  ALTER TABLE users add last_modified_date datetime2(7);
END

-- Create All audit tables that hibernate needs
if not exists (select * from sys.tables where name='revinfo')
  CREATE TABLE revinfo
  (
    rev INT IDENTITY
      PRIMARY KEY,
    revtstmp BIGINT
  )
GO


if not exists (select * from sys.tables where name='accesses_aud')
  CREATE TABLE accesses_aud
  (
    id INT NOT NULL,
    rev INT NOT NULL
      CONSTRAINT FKjh5aq968ttughdj63k2c25gvu
      REFERENCES revinfo,
    revtype SMALLINT,
    name VARCHAR(255),
    sort_order SMALLINT,
    PRIMARY KEY (id, rev)
  )
GO

if not exists (select * from sys.tables where name='application_roles_aud')
  create table application_roles_aud
  (
    id int not null,
    rev int not null
      constraint FKsmo3ekhik7snd5jhpyb7ix2e4
      references revinfo,
    revtype smallint,
    description varchar(255),
    display_name varchar(255),
    name varchar(255),
    primary key (id, rev)
  )
GO

if not exists (select * from sys.tables where name='church_church_aud')
  create table church_church_aud
  (
    rev int not null
      constraint FKbd97bp1kq9pep0y0bpuey3p0b
      references revinfo,
    parent_church_id int not null,
    id int not null,
    revtype smallint,
    primary key (rev, parent_church_id, id)
  )
GO

if not exists (select * from sys.tables where name='church_group_aud')
  create table church_group_aud
  (
    rev int not null
      constraint FKe1njicndxci9lm36iijxowglk
      references revinfo,
    church_id int not null,
    id int not null,
    revtype smallint,
    primary key (rev, church_id, id)
  )
GO

if not exists (select * from sys.tables where name='church_organization_aud')
  create table church_organization_aud
  (
    id int not null,
    rev int not null
      constraint FK6ljnsf1v918vcioufrdwxkju0
      references revinfo,
    revtype smallint,
    church_id int,
    organization_data varchar(max),
    primary key (id, rev)
  )
GO

if not exists (select * from sys.tables where name='church_pictures_aud')
  create table church_pictures_aud
  (
    id int not null,
    rev int not null
      constraint FKgx19933r3hsu5re6l8lmsro62
      references revinfo,
    revtype smallint,
    church_id int,
    description varchar(255),
    name varchar(255),
    picture_url_full varchar(255),
    picture_url_medium varchar(255),
    thumbnail_url varchar(255),
    type_id int,
    primary key (id, rev)
  )
GO

if not exists (select * from sys.tables where name='church_types_aud')
  create table church_types_aud
  (
    id int not null,
    rev int not null
      constraint FK9b0fye12xrw14ksihr6yyqmmg
      references revinfo,
    revtype smallint,
    name varchar(255),
    sort_order smallint,
    primary key (id, rev)
  )
GO

if not exists (select * from sys.tables where name='churches_aud')
  create table churches_aud
  (
    id int not null,
    rev int not null
      constraint FK4ww3rpl9rl10jiegof0cbkew8
      references revinfo,
    revtype smallint,
    address varchar(255),
    city varchar(255),
    email varchar(255),
    latitude varchar(255),
    leader_id int,
    longitude varchar(255),
    name varchar(255),
    parent_church_id int,
    phone varchar(255),
    postal_code varchar(255),
    state_id int,
    type_id int,
    primary key (id, rev)
  )
GO

if not exists (select * from sys.tables where name='group_team_aud')
  create table group_team_aud
  (
    rev int not null
      constraint FKnw7qkeebnxe68nxu4u6cmergv
      references revinfo,
    group_id int not null,
    id int not null,
    revtype smallint,
    primary key (rev, group_id, id)
  )
GO

if not exists (select * from sys.tables where name='groups_aud')
  create table groups_aud
  (
    id int not null,
    rev int not null
      constraint FK5pu6tu97wjjxytqhosmwc8ovv
      references revinfo,
    revtype smallint,
    archived bit,
    church_group bit,
    church_id int,
    effective_date datetime2,
    leader_id int,
    name varchar(255),
    primary key (id, rev)
  )
GO

if not exists (select * from sys.tables where name='roles_aud')
  create table roles_aud
  (
    id int not null,
    rev int not null
      constraint FKt0mnl3rej2p0h9gxnbalf2kdd
      references revinfo,
    revtype smallint,
    name varchar(255),
    sort_order smallint,
    primary key (id, rev)
  )
GO

if not exists (select * from sys.tables where name='states_aud')
  create table states_aud
  (
    id int not null,
    rev int not null
      constraint FKd51dxgrh6ggqus202bc7x40mn
      references revinfo,
    revtype smallint,
    full_name varchar(255),
    short_name varchar(255),
    primary key (id, rev)
  )
GO

if not exists (select * from sys.tables where name='team_user_aud')
  create table team_user_aud
  (
    rev int not null
      constraint FKh69p8lit424a0e04tixuoslc0
      references revinfo,
    team_id int not null,
    id int not null,
    revtype smallint,
    primary key (rev, team_id, id)
  )
GO

if not exists (select * from sys.tables where name='teams_aud')
  create table teams_aud
  (
    id int not null,
    rev int not null
      constraint FKma12sm44fu0iv25qenc8ueujb
      references revinfo,
    revtype smallint,
    archived bit,
    church_team bit,
    effective_date datetime2,
    group_id int,
    leader_id int,
    name varchar(255),
    primary key (id, rev)
  )
GO

if not exists (select * from sys.tables where name='titles_aud')
  create table titles_aud
  (
    id int not null,
    rev int not null
      constraint FKle65p8owlj708pj873e80wn75
      references revinfo,
    revtype smallint,
    display_name varchar(255),
    name varchar(255),
    sort_order smallint,
    primary key (id, rev)
  )
GO

if not exists (select * from sys.tables where name='transfer_requests_aud')
  create table transfer_requests_aud
  (
    id int not null,
    rev int not null
      constraint FKkl53hogb27g9w5sw49aj6niaj
      references revinfo,
    revtype smallint,
    comment varchar(255),
    current_church_id int,
    current_church_name varchar(255),
    current_group_id int,
    current_group_name varchar(255),
    current_team_id int,
    current_team_name varchar(255),
    member_name varchar(255),
    new_church_id int,
    new_church_name varchar(255),
    new_group_id int,
    new_group_name varchar(255),
    new_team_id int,
    new_team_name varchar(255),
    request_date datetime2,
    request_status varchar(255),
    reviewer_id int,
    reviewer_name varchar(255),
    user_id int,
    primary key (id, rev)
  )
GO

if not exists (select * from sys.tables where name='user_application_roles_aud')
  create table user_application_roles_aud
  (
    id int not null,
    rev int not null
      constraint FK8n61843n5ui8f5669utrgn0b6
      references revinfo,
    revtype smallint,
    application_role_id int,
    user_id int,
    primary key (id, rev)
  )
GO

if not exists (select * from sys.tables where name='user_pictures_aud')
  create table user_pictures_aud
  (
    id int not null,
    rev int not null
      constraint FKdo0st5j0lncq0ha6afvho3ucu
      references revinfo,
    revtype smallint,
    description varchar(255),
    name varchar(255),
    picture_url_full varchar(255),
    picture_url_medium varchar(255),
    thumbnail_url varchar(255),
    user_id int,
    primary key (id, rev)
  )
GO

if not exists (select * from sys.tables where name='user_profile_categories_aud')
  create table user_profile_categories_aud
  (
    id int not null,
    rev int not null
      constraint FKg5clmg476r3364l7jrwlc952a
      references revinfo,
    revtype smallint,
    category varchar(255),
    primary key (id, rev)
  )
GO

if not exists (select * from sys.tables where name='user_profile_information_aud')
  create table user_profile_information_aud
  (
    id int not null,
    rev int not null
      constraint FKg7erb9egcm79jv58ehsqjsfct
      references revinfo,
    revtype smallint,
    category_id int,
    end_date datetime2,
    information varchar(255),
    profile_id int,
    start_date datetime2,
    primary key (id, rev)
  )
GO

if not exists (select * from sys.tables where name='user_profiles_aud')
  create table user_profiles_aud
  (
    id int not null,
    rev int not null
      constraint FKrysschoom64w9acq0xem35xf7
      references revinfo,
    revtype smallint,
    bachelors_degree varchar(255),
    bachelors_school varchar(255),
    certifications varchar(255),
    children int,
    church_id int,
    company varchar(255),
    current_job_duration varchar(255),
    desired_field varchar(255),
    doctorate_degree varchar(255),
    doctorate_school varchar(255),
    ethnicity varchar(255),
    faith_level varchar(255),
    gospel_aspirations varchar(255),
    gospel_plan varchar(255),
    hobbies varchar(255),
    iba varchar(255),
    iba_organization varchar(255),
    job_title varchar(255),
    masters_degree varchar(255),
    masters_school varchar(255),
    occupation varchar(255),
    personality varchar(255),
    professional_licenses varchar(255),
    type_of_business varchar(255),
    university varchar(255),
    university_position varchar(255),
    user_id int,
    primary key (id, rev)
  )
GO

if not exists (select * from sys.tables where name='user_user_application_role_aud')
  create table user_user_application_role_aud
  (
    rev int not null
      constraint FKfonj88ak4c4nrx0csckvusw4g
      references revinfo,
    user_id int not null,
    id int not null,
    revtype smallint,
    primary key (rev, user_id, id)
  )
GO

if not exists (select * from sys.tables where name='users_aud')
  create table users_aud
  (
    id int not null,
    rev int not null
      constraint FKc4vk4tui2la36415jpgm9leoq
      references revinfo,
    revtype smallint,
    access_id int,
    account_not_expired bit,
    account_not_locked bit,
    active_directory_id varchar(255),
    archived bit,
    baptism_date datetime2,
    birthday datetime2,
    church_id int,
    church_name varchar(255),
    credentials_not_expired bit,
    enabled bit,
    first_name varchar(255),
    gender varchar(255),
    gospel_worker bit,
    group_id int,
    group_name varchar(255),
    last_name varchar(255),
    married bit,
    middle_name varchar(255),
    parent_church_id int,
    parent_church_name varchar(255),
    picture_id int,
    role_id int,
    spouse_id int,
    team_id int,
    team_name varchar(255),
    theological_student bit,
    title_id int,
    user_name varchar(255),
    primary key (id, rev)
  )
GO



































