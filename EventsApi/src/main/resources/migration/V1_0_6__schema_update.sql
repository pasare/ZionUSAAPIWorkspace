IF NOT EXISTS(select *
              FROM sys.columns
              WHERE object_id = OBJECT_ID(N'[dbo].[event_status]')
                AND name = 'started_date')
    BEGIN

        alter table event_status add started_date varchar(20);
        alter table event_status_aud add started_date varchar(20);

        alter table event_status add finalized_date varchar(20);
        alter table event_status_aud add finalized_date varchar(20);

        --manager approval
        alter table event_status add expected_manager_approval_date varchar(20);
        alter table event_status_aud add expected_manager_approval_date varchar(20);

        alter table event_status add actual_manager_approval_date varchar(20);
        alter table event_status_aud add actual_manager_approval_date varchar(20);

        --admin approval
        alter table event_status add expected_admin_approval_date varchar(20);
        alter table event_status_aud add expected_admin_approval_date varchar(20);

        alter table event_status add actual_admin_approval_date varchar(20);
        alter table event_status_aud add actual_admin_approval_date varchar(20);

        --GA approval
        alter table event_status add expected_ga_approval_date varchar(20);
        alter table event_status_aud add expected_ga_approval_date varchar(20);

        alter table event_status add actual_ga_approval_date varchar(20);
        alter table event_status_aud add actual_ga_approval_date varchar(20);

    END;