CREATE VIEW dbo.applications AS
SELECT id,
       active,
       apple_app_store_url,
       archived,
       description,
       enabled,
       google_play_store_url,
       hidden,
       icon_title,
       icon_url,
       launch_url,
       name,
       unique_id
FROM zionusa_management_test.dbo.applications a
  go
