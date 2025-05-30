IF OBJECT_ID('event_status_event_proposals_id_fk') IS NULL
    BEGIN
        alter table event_proposals add event_status_id int;
        alter table event_proposals_aud add event_status_id int;

        truncate table event_status;
        alter table event_status
            add constraint event_status_event_proposals_id_fk
                foreign key (event_proposal_id) references event_proposals

    END
go