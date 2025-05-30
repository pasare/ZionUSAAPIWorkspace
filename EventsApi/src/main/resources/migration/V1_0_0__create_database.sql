if not exists (select * from sys.tables where name = 'contacts') BEGIN
  create table contacts
  (
    id int identity
      primary key,
    created_by varchar(255),
    created_date datetime2,
    last_modified_by varchar(255),
    last_modified_date datetime2,
    country_region varchar(255),
    business_office_phone varchar(255),
    cell_phone varchar(255),
    city varchar(255),
    company_name varchar(255),
    email_address varchar(255),
    first_name varchar(255),
    last_name varchar(255),
    organization_name varchar(255),
    state_province varchar(255)
  )

  create table contacts_aud
  (
    id int not null,
    rev int not null,
    revtype smallint,
    country_region varchar(255),
    business_office_phone varchar(255),
    cell_phone varchar(255),
    city varchar(255),
    company_name varchar(255),
    email_address varchar(255),
    first_name varchar(255),
    last_name varchar(255),
    organization_name varchar(255),
    state_province varchar(255),
    primary key (id, rev)
  )
  END
go

if not exists (select * from sys.tables where name = 'event_attendance') BEGIN
  create table event_attendance
  (
    id int identity
      constraint PK__event_at__3213E83F80BDF25C
      primary key,
    title varchar(255),
    event_proposal_id int,
    archived bit,
    attended bit,
    confirmed bit,
    contact_id int,
    same_name_filed bit,
    same_position_as_filed bit,
    same_organization_as_filed bit,
    created_by varchar(255),
    created_date datetime2,
    last_modified_by varchar(255),
    last_modified_date datetime2
  )

  create table event_attendance_aud
  (
    id int not null,
    rev int not null,
    revtype smallint,
    same_position_as_filed bit,
    archived bit,
    attended bit,
    confirmed bit,
    contact_id int,
    event_proposal_id int,
    same_name_filed bit,
    same_organization_as_filed bit,
    title varchar(255),
    primary key (id, rev)
  )
  END
go

if not exists (select * from sys.tables where name = 'event_proposals') BEGIN
  create table event_proposals
  (
    id int identity
      constraint PK__event_pr__3213E83FA4F61218
      primary key,
    additional_attire bit,
    additional_audio_person bit,
    additional_photo_person bit,
    additional_video_person bit,
    admin_approved bit,
    admin_comments varchar(255),
    advertising bit,
    advertising_website bit,
    advertising_why_not varchar(255),
    archived bit,
    audio_visual_allowed bit,
    audio_visual_assigned int,
    banner_budget int,
    banner_detail varchar(255),
    banner_mount varchar(255),
    banner_type varchar(255),
    banners int,
    blood_drive_video_shown bit,
    church_attire varchar(255),
    church_budget_banner int,
    church_budget_donated_items int,
    church_budget_food int,
    church_budget_gas int,
    church_budget_misc int,
    church_id int,
    church_items varchar(255),
    church_name varchar(255),
    church_services varchar(255),
    cleanup_video_shown bit,
    description varchar(255),
    event_proposal_work_flow varchar(255),
    event_type_id int,
    finalized bit,
    finalized_first_date varchar(255),
    flyer_assistance bit,
    food_budget int,
    food_detail varchar(255),
    ga_rejected bit,
    ga_submitted bit,
    ga_submitted_date varchar(255),
    gas_budget int,
    gas_detail varchar(255),
    government_official_why_not varchar(255),
    government_officials_id int,
    government_officials_name varchar(255),
    indoor_banner_number int,
    indoor_outdoor varchar(255),
    informational_proposal bit,
    initiation_needed bit,
    insurance_required bit,
    intro_video_reaction varchar(255),
    intro_video_shown bit,
    invitation bit,
    itinerary_detail1 varchar(255),
    itinerary_detail2 varchar(255),
    itinerary_detail3 varchar(255),
    itinerary_detail4 varchar(255),
    itinerary_detail5 varchar(255),
    itinerary_detail6 varchar(255),
    itinerary_detail7 varchar(255),
    itinerary_detail8 varchar(255),
    itinerary_time1 varchar(255),
    itinerary_time2 varchar(255),
    itinerary_time3 varchar(255),
    itinerary_time4 varchar(255),
    itinerary_time5 varchar(255),
    itinerary_time6 varchar(255),
    itinerary_time7 varchar(255),
    itinerary_time8 varchar(255),
    late_submission_reason varchar(255),
    location_id int,
    location_name varchar(255),
    making_flyer bit,
    manager_approved varchar(255),
    manager_comments varchar(255),
    materials bit,
    materials_needed bit,
    materials_options varchar(255),
    media bit,
    media_name varchar(255),
    media_status varchar(255),
    media_why_not varchar(255),
    misc_budget int,
    misc_detail varchar(255),
    monthly_chosun_shown bit,
    monthly_joongang_shown bit,
    overseer_church_help bit,
    partner_media bit,
    partner_organization varchar(255),
    partner_organization_budget bit,
    partner_organization_budget_donated int,
    partner_organization_budget_sponsor int,
    partner_organization_id int,
    partner_organization_items varchar(255),
    partner_organization_services varchar(255),
    pastor_approved varchar(255),
    pastor_comments varchar(255),
    performances bit,
    permits_required bit,
    photo_allowed bit,
    photo_video_before bit,
    photographer_assigned int,
    preach_strategy varchar(255),
    present_organization bit,
    present_organization_why_not varchar(255),
    press_reporting varchar(255),
    proposed_date varchar(255),
    proposed_end_date varchar(255),
    proposed_end_time varchar(255),
    proposed_time varchar(255),
    public_relations_assigned int,
    public_strategy varchar(255),
    purpose varchar(255),
    queens_award_video_shown bit,
    requester_id int,
    scout_photographer bit,
    scout_videographer bit,
    scouting bit,
    scouting_date varchar(255),
    setup_help bit,
    spokesperson_assigned varchar(255),
    stage_needed bit,
    standing_banner_number int,
    supplies_budget int,
    supplies_detail varchar(255),
    target_female_adults int,
    target_male_adults int,
    target_teenagers int,
    target_total int,
    target_young_adults int,
    tent_banner_number int,
    tent_needed bit,
    tent_used bit,
    title varchar(255),
    transportation_budget int,
    transportation_bus bit,
    transportation_car bit,
    transportation_car_number int,
    transportation_detail varchar(255),
    transportation_subway bit,
    transportation_walk bit,
    video_allowed bit,
    videographer_assigned int,
    volunteer int,
    volunteer_female_adults int,
    volunteer_male_adults int,
    volunteer_teenagers int,
    volunteer_total int,
    volunteer_young_adults int,
    volunteers_other_church varchar(255),
    workflow_status varchar(255),
    writer_assigned int,
    created_by varchar(255),
    created_date datetime2,
    last_modified_by varchar(255),
    last_modified_date datetime2
  )

  create table event_proposals_aud
  (
    id int not null,
    rev int not null,
    revtype smallint,
    additional_attire bit,
    additional_audio_person bit,
    additional_photo_person bit,
    additional_video_person bit,
    admin_approved bit,
    admin_comments varchar(255),
    advertising bit,
    advertising_website bit,
    advertising_why_not varchar(255),
    archived bit,
    audio_visual_allowed bit,
    audio_visual_assigned int,
    banner_budget int,
    banner_detail varchar(255),
    banner_mount varchar(255),
    banner_type varchar(255),
    banners int,
    blood_drive_video_shown bit,
    church_attire varchar(255),
    church_budget_banner int,
    church_budget_donated_items int,
    church_budget_food int,
    church_budget_gas int,
    church_budget_misc int,
    church_id int,
    church_items varchar(255),
    church_name varchar(255),
    church_services varchar(255),
    cleanup_video_shown bit,
    description varchar(255),
    event_proposal_work_flow varchar(255),
    event_type_id int,
    finalized bit,
    finalized_first_date varchar(255),
    flyer_assistance bit,
    food_budget int,
    food_detail varchar(255),
    ga_rejected bit,
    ga_submitted bit,
    ga_submitted_date varchar(255),
    gas_budget int,
    gas_detail varchar(255),
    government_official_why_not varchar(255),
    government_officials_id int,
    government_officials_name varchar(255),
    indoor_banner_number int,
    indoor_outdoor varchar(255),
    informational_proposal bit,
    initiation_needed bit,
    insurance_required bit,
    intro_video_reaction varchar(255),
    intro_video_shown bit,
    invitation bit,
    itinerary_detail1 varchar(255),
    itinerary_detail2 varchar(255),
    itinerary_detail3 varchar(255),
    itinerary_detail4 varchar(255),
    itinerary_detail5 varchar(255),
    itinerary_detail6 varchar(255),
    itinerary_detail7 varchar(255),
    itinerary_detail8 varchar(255),
    itinerary_time1 varchar(255),
    itinerary_time2 varchar(255),
    itinerary_time3 varchar(255),
    itinerary_time4 varchar(255),
    itinerary_time5 varchar(255),
    itinerary_time6 varchar(255),
    itinerary_time7 varchar(255),
    itinerary_time8 varchar(255),
    late_submission_reason varchar(255),
    location_id int,
    location_name varchar(255),
    making_flyer bit,
    manager_approved varchar(255),
    manager_comments varchar(255),
    materials bit,
    materials_needed bit,
    materials_options varchar(255),
    media bit,
    media_name varchar(255),
    media_status varchar(255),
    media_why_not varchar(255),
    misc_budget int,
    misc_detail varchar(255),
    monthly_chosun_shown bit,
    monthly_joongang_shown bit,
    overseer_church_help bit,
    partner_media bit,
    partner_organization varchar(255),
    partner_organization_budget bit,
    partner_organization_budget_donated int,
    partner_organization_budget_sponsor int,
    partner_organization_id int,
    partner_organization_items varchar(255),
    partner_organization_services varchar(255),
    pastor_approved varchar(255),
    pastor_comments varchar(255),
    performances bit,
    permits_required bit,
    photo_allowed bit,
    photo_video_before bit,
    photographer_assigned int,
    preach_strategy varchar(255),
    present_organization bit,
    present_organization_why_not varchar(255),
    press_reporting varchar(255),
    proposed_date varchar(255),
    proposed_end_date varchar(255),
    proposed_end_time varchar(255),
    proposed_time varchar(255),
    public_relations_assigned int,
    public_strategy varchar(255),
    purpose varchar(255),
    queens_award_video_shown bit,
    requester_id int,
    scout_photographer bit,
    scout_videographer bit,
    scouting bit,
    scouting_date varchar(255),
    setup_help bit,
    spokesperson_assigned varchar(255),
    stage_needed bit,
    standing_banner_number int,
    supplies_budget int,
    supplies_detail varchar(255),
    target_female_adults int,
    target_male_adults int,
    target_teenagers int,
    target_total int,
    target_young_adults int,
    tent_banner_number int,
    tent_needed bit,
    tent_used bit,
    title varchar(255),
    transportation_budget int,
    transportation_bus bit,
    transportation_car bit,
    transportation_car_number int,
    transportation_detail varchar(255),
    transportation_subway bit,
    transportation_walk bit,
    video_allowed bit,
    videographer_assigned int,
    volunteer int,
    volunteer_female_adults int,
    volunteer_male_adults int,
    volunteer_teenagers int,
    volunteer_total int,
    volunteer_young_adults int,
    volunteers_other_church varchar(255),
    workflow_status varchar(255),
    writer_assigned int,
    primary key (id, rev)
  )
  END
go

if not exists (select * from sys.tables where name = 'event_status') BEGIN
  create table event_status
  (
    id int identity
      constraint PK__event_st__3213E83FB832F830
      primary key,
    admin_approved bit,
    archived bit,
    event_proposal_id int not null,
    finalized bit,
    ga_approved bit,
    ga_submitted bit,
    manager_approved bit,
    requester_id int not null,
    started bit,
    created_by varchar(255),
    created_date datetime2,
    last_modified_by varchar(255),
    last_modified_date datetime2
  )

  create table event_status_aud
  (
    id int not null,
    rev int not null,
    revtype smallint,
    admin_approved bit,
    archived bit,
    event_proposal_id int,
    finalized bit,
    ga_approved bit,
    ga_submitted bit,
    manager_approved bit,
    requester_id int,
    started bit,
    primary key (id, rev)
  )
  END
go

if not exists (select * from sys.tables where name = 'event_teams') BEGIN
  create table event_teams
  (
    id int identity
      constraint PK__event_te__3213E83F2D786D94
      primary key,
    event_id int,
    role_id int,
    user_id int,
    audio_visual int,
    graphics int,
    photographer_id int,
    public_relations int,
    videographer_id int,
    created_by varchar(255),
    created_date datetime2,
    last_modified_by varchar(255),
    last_modified_date datetime2
  )

  create table event_teams_aud
  (
    id int not null,
    rev int not null,
    revtype smallint,
    audio_visual int,
    event_id int,
    graphics int,
    photographer_id int,
    public_relations int,
    role_id int,
    user_id int,
    videographer_id int,
    primary key (id, rev)
  )
  END
go

if not exists (select * from sys.tables where name = 'event_types') BEGIN
  create table event_types
  (
    id int identity
      constraint PK__event_ty__3213E83F627A9805
      primary key,
    active bit,
    category varchar(255),
    title varchar(255),
    created_by varchar(255),
    created_date datetime2,
    last_modified_by varchar(255),
    last_modified_date datetime2
  )

  create table event_types_aud
  (
    id int not null,
    rev int not null,
    revtype smallint,
    active bit,
    category varchar(255),
    title varchar(255),
    primary key (id, rev)
  )
  END
go

if not exists (select * from sys.tables where name = 'flyway_schema_history') BEGIN
  create table flyway_schema_history
  (
    installed_rank int not null
      constraint flyway_schema_history_pk
      primary key,
    version nvarchar(50),
    description nvarchar(200),
    type nvarchar(20) not null,
    script nvarchar(1000) not null,
    checksum int,
    installed_by nvarchar(100) not null,
    installed_on datetime not null,
    execution_time int not null,
    success bit not null
  )

  create index flyway_schema_history_s_idx on flyway_schema_history (success)
END
go

if not exists (select * from sys.tables where name = 'locations') BEGIN
  create table locations
  (
    id int identity
      primary key,
    created_by varchar(255),
    created_date datetime2,
    last_modified_by varchar(255),
    last_modified_date datetime2,
    address varchar(255),
    city varchar(255),
    country_region varchar(255),
    description varchar(255),
    indoor_outdoor varchar(255),
    location_name varchar(255),
    permit_deadline varchar(255),
    permits bit,
    point_of_contact int,
    point_of_contact_id int,
    state_province varchar(255),
    zip_postal_code varchar(255)
  )

  create table locations_aud
  (
    id int not null,
    rev int not null,
    revtype smallint,
    address varchar(255),
    city varchar(255),
    country_region varchar(255),
    description varchar(255),
    indoor_outdoor varchar(255),
    location_name varchar(255),
    permit_deadline varchar(255),
    permits bit,
    point_of_contact int,
    point_of_contact_id int,
    state_province varchar(255),
    zip_postal_code varchar(255),
    primary key (id, rev)
  )
  END
go

if not exists (select * from sys.tables where name = 'notifications') BEGIN
  create table notifications
  (
    id int identity
      primary key,
    created_by varchar(255),
    created_date datetime2,
    last_modified_by varchar(255),
    last_modified_date datetime2,
    category varchar(255),
    message varchar(255),
    next_process_time varchar(255),
    process_time varchar(255),
    processed bit not null,
    recipients varchar(255),
    sub_category varchar(255),
    sub_title varchar(255),
    title varchar(255),
    type varchar(255)
  )

  create table notifications_aud
  (
    id int not null,
    rev int not null,
    revtype smallint,
    category varchar(255),
    message varchar(255),
    next_process_time varchar(255),
    process_time varchar(255),
    processed bit,
    recipients varchar(255),
    sub_category varchar(255),
    sub_title varchar(255),
    title varchar(255),
    type varchar(255),
    primary key (id, rev)
  )
END
go

if not exists (select * from sys.tables where name = 'organizations') BEGIN
  create table organizations
  (
    id int identity
      primary key,
    created_by varchar(255),
    created_date datetime2,
    last_modified_by varchar(255),
    last_modified_date datetime2,
    address varchar(255),
    city varchar(255),
    company_main_phone varchar(255),
    department varchar(255),
    e_mail varchar(255),
    point_of_contact varchar(255),
    state_province varchar(255),
    title varchar(255),
    web_page varchar(255),
    zip_postal_code varchar(255)
  )

  create table organizations_aud
  (
    id int not null,
    rev int not null,
    revtype smallint,
    address varchar(255),
    city varchar(255),
    company_main_phone varchar(255),
    department varchar(255),
    e_mail varchar(255),
    point_of_contact varchar(255),
    state_province varchar(255),
    title varchar(255),
    web_page varchar(255),
    zip_postal_code varchar(255),
    primary key (id, rev)
  )
END
go

if not exists (select * from sys.tables where name = 'results_survey') BEGIN
create table results_survey
(
  id int identity
    primary key,
  created_by varchar(255),
  created_date datetime2,
  last_modified_by varchar(255),
  last_modified_date datetime2,
  banner_group_photo bit,
  church_items varchar(255),
  church_services varchar(255),
  distributed_materials varchar(255),
  event_proposal_id int,
  event_video bit,
  finalized bit,
  fragrances varchar(255),
  fragrances_recorded bit,
  media_attended bit,
  media_name varchar(255),
  new_donations int,
  non_members int,
  partner_organization_services varchar(255),
  partner_organizations varchar(255),
  same_event_day bit,
  same_event_location bit,
  same_number_of_volunteers bit,
  title varchar(255),
  vip_photos bit,
  volunteer_female_adults int,
  volunteer_male_adults int,
  volunteer_teenagers int,
  volunteer_young_adults int,
  workflow_status varchar(255),
  write_up varchar(255)
)

create table results_survey_aud
(
  id int not null,
  rev int not null,
  revtype smallint,
  banner_group_photo bit,
  church_items varchar(255),
  church_services varchar(255),
  distributed_materials varchar(255),
  event_proposal_id int,
  event_video bit,
  finalized bit,
  fragrances varchar(255),
  fragrances_recorded bit,
  media_attended bit,
  media_name varchar(255),
  new_donations int,
  non_members int,
  partner_organization_services varchar(255),
  partner_organizations varchar(255),
  same_event_day bit,
  same_event_location bit,
  same_number_of_volunteers bit,
  title varchar(255),
  vip_photos bit,
  volunteer_female_adults int,
  volunteer_male_adults int,
  volunteer_teenagers int,
  volunteer_young_adults int,
  workflow_status varchar(255),
  write_up varchar(255),
  primary key (id, rev)
)
  END
go

if not exists (select * from sys.tables where name = 'revinfo') BEGIN
  create table revinfo
  (
    rev int identity
      primary key,
    revtstmp bigint
  )


  alter table contacts_aud
    add constraint FK5osfnm6mydnq2ru8nhiaf1k74
  foreign key (rev) references revinfo


  alter table event_attendance_aud
    add constraint FK7gb9u5dutdd8jhyfw5p2s6lm3
  foreign key (rev) references revinfo


  alter table event_proposals_aud
    add constraint FKcldm4ca4hpw3jdsiolrawj7on
  foreign key (rev) references revinfo


  alter table event_status_aud
    add constraint FKs207kathda1aldotc0tma9kts
  foreign key (rev) references revinfo


  alter table event_teams_aud
    add constraint FK1qo689l2gcdqlgidvq0cwih4a
  foreign key (rev) references revinfo


  alter table event_types_aud
    add constraint FKlmvtgfefwfu5q5rwikjhevc2g
  foreign key (rev) references revinfo


  alter table locations_aud
    add constraint FKhd9g58687y6f9lc0cs3nwuhbi
  foreign key (rev) references revinfo


  alter table notifications_aud
    add constraint FK72ev2gylm6yndv9vc7uxrg9dx
  foreign key (rev) references revinfo


  alter table organizations_aud
    add constraint FKfxeq0lp5j0m5ftym4oq5xo2yq
  foreign key (rev) references revinfo


  alter table results_survey_aud
    add constraint FK8gk46ebhjhnq6dh1378scpgx5
  foreign key (rev) references revinfo
END

