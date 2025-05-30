CREATE VIEW dbo.users_application_role_user_permission_view as
SELECT arp.id,
       arp.archived,
       arp.hidden,
       arp.user_application_role_id,
       arp.user_application_role_archived,
       arp.user_application_role_hidden,
       arp.user_application_role_display_name,
       arp.user_application_role_description,
       arp.user_application_role_name,
       arp.user_permission_id,
       arp.user_permission_archived,
       arp.user_permission_hidden,
       arp.user_permission_display_name,
       arp.user_permission_description,
       arp.user_permission_name
FROM zionusa_management_test.dbo.users_application_role_user_permission_view arp
  go
