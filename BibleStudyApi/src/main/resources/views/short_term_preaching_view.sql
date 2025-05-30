CREATE VIEW dbo.short_term_preaching_view AS
SELECT id,
       city,
       end_date,
       start_date,
       state_id,
       name,
       enabled
FROM zionusa_management_test.dbo.short_term_preaching
    go
