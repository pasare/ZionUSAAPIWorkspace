CREATE VIEW dbo.event_proposals_vips_view WITH SCHEMABINDING as
SELECT ev.id                   AS id,
       ev.archived             AS archived,
       ev.hidden               AS hidden,
       c.id                    AS vip_id,
       c.business_office_phone AS business_office_phone,
       c.cell_phone            AS cell_phone,
       c.city                  AS city,
       c.company_name          AS company_name,
       c.country_region        AS country_region,
       c.email_address         AS email_address,
       c.facebook_url          AS facebook_url,
       e.event_category_id     AS event_category_id,
       ec.title                AS event_category_title,
       e.id                    AS event_proposal_id,
       e.branch_id             AS event_proposal_branch_id,
       e.branch_name           AS event_proposal_branch_name,
       e.proposed_date         AS event_proposal_proposed_date,
       e.title                 AS event_proposal_title,
       e.event_type_id         AS event_type_id,
       et.title                AS event_type_title,
       c.first_name            AS first_name,
       c.instagram_url         AS instagram_url,
       c.last_name             AS last_name,
       c.linked_in_url         AS linked_in_url,
       c.picture_url           AS picture_url,
       c.state_province        AS state_province,
       c.title                 AS title,
       c.twitter_url           AS twitter_url,
       COUNT_BIG(*)            as instances
FROM dbo.contacts c
         JOIN dbo.event_proposals_vips ev ON ev.contact_id = c.id
         JOIN dbo.event_proposals e ON e.id = ev.event_proposal_id
         JOIN dbo.event_categories ec ON ec.id = e.event_category_id
         JOIN dbo.event_types et ON et.id = e.event_type_id
GROUP BY ev.id, ev.archived, ev.hidden, c.id, c.business_office_phone, et.title, e.title, ec.title, c.cell_phone,
         c.city, c.company_name,
         c.country_region, c.email_address, c.facebook_url, e.event_category_id, e.id, e.branch_id, e.branch_name,
         e.proposed_date, e.event_type_id, c.first_name, c.instagram_url, c.last_name, c.linked_in_url,
         c.picture_url, c.state_province, c.title, c.twitter_url
go

create unique clustered index IDX_V1
    on dbo.event_proposals_vips_view (id)
go
