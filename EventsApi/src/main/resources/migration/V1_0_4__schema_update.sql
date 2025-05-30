if not exists(select *
              from sys.tables
              where name = 'printing_requests')
    BEGIN

        create table printing_requests
        (
            id                 int identity
                primary key,
            created_by         varchar(255),
            created_date       datetime2,
            last_modified_by   varchar(255),
            last_modified_date datetime2,
            banner_type        varchar(255),
            church_id          int,
            event_id           int,
            title              varchar(255),
            workflow           varchar(255)
        )


        create table printing_requests_aud
        (
            id          int not null,
            rev         int not null
                constraint FK19sl6q6w53ndy06d0j5dmpm6a
                    references revinfo,
            revtype     smallint,
            banner_type varchar(255),
            church_id   int,
            event_id    int,
            title       varchar(255),
            workflow    varchar(255),
            primary key (id, rev)
        )
    END
go

