if not exists (select * from sys.tables where name = 'system_configurations') BEGIN

    create table system_configurations
    (
        id                  int identity
            primary key,
        created_by          varchar(255),
        created_date        datetime2,
        last_modified_by    varchar(255),
        last_modified_date  datetime2,
        category            varchar(255),
        configuration_key   varchar(255),
        configuration_value varchar(255)
    )

    create table system_configurations_aud
    (
        id                  int not null,
        rev                 int not null
            constraint FKff321tpypal2y37t1lvrlriqh
                references revinfo,
        revtype             smallint,
        category            varchar(255),
        configuration_key   varchar(255),
        configuration_value varchar(255),
        primary key (id, rev)
    )
END
go
