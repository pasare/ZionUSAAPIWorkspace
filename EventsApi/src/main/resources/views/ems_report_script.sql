-- Used to generate EMS report

Select ep.title             AS title,
       ep.branch_name,
       ep.proposed_date     AS start_date,
       ep.proposed_end_date as end_date,
       ep.proposed_time     as start_time,
       ep.proposed_end_time as end_time,
       l.location_name      as location,
       ep.purpose,
       ep.description,
       ec.title             AS event_category,
       et.title             AS event_type,

       evm.male_adults,
       evm.male_college_students,
       evm.male_teenagers,
       evm.male_young_adults,
       evm.female_adults,
       evm.female_college_students,
       evm.female_teenagers,
       evm.female_young_adults,
       evm.branch_name      as volunteer_branch,
       evm.helping_branch,
       evm.type             as volunteer_type,
       ep.members_total,
       ep.contacts_total,
       ep.vips_total


from event_proposals ep
       join event_categories ec on ep.event_category_id = ec.id
       join event_types et on ep.event_type_id = et.id
       join event_volunteer_management evm on ep.id = evm.event_proposal_id
       join locations l on ep.location_id = l.id

where ep.event_category_id = 3;

select rs.title as results_survey_title,
       ec.title as category,
       ep.title as event_proposal_title,
       rs.proposed_date,
       rs.proposed_end_date,
       rs.proposed_time,
       rs.proposed_end_time,
       branch_name,
       et.title,
       location_name,
       volunteer_female_adults,
       volunteer_female_college_students,
       volunteer_female_teenagers,
       volunteer_female_young_adults,
       volunteer_male_adults,
       volunteer_male_college_students,
       volunteer_male_teenagers,
       volunteer_male_young_adults,
       volunteer_young_adults,
       volunteer_teenagers,
       visitors,
       rs.different_number_of_volunteer,
       rs.homes_helped,
       rs.man_hours_per_volunteer,
       rs.people_helped

from results_survey rs

       join event_proposals ep on rs.event_proposal_id = ep.id
       join event_categories ec on rs.event_category_id = ec.id
       join event_types et on rs.event_type_id = et.id
where ep.event_category_id = 2;
