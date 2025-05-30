if not exists (select * from sys.tables where name = 'user_preferences') BEGIN
    create table user_preferences
    (
        id                 int identity
            constraint user_preferences_pk
                primary key nonclustered,
        user_id            int,
        preference_key     varchar(50),
        preference_value   varchar(150),
        created_by         varchar(100),
        created_date       datetime2,
        last_modified_by   varchar(100),
        last_modified_date datetime2
    )

    create table user_preferences_aud
    (
        id               int not null,
        rev              int not null
            constraint FKalwkc642rd2r9dt75krt4gl3r
                references revinfo,
        revtype          smallint,
        user_id          int,
        preference_key   varchar(255),
        preference_value varchar(255),
        primary key (id, rev)
    )
END
go
