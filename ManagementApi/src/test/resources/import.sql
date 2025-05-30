--
-- Security
--

INSERT INTO applications (id, name, apple_app_store_url, google_play_store_url, icon_title, icon_url, launch_url, unique_id, description, enabled, archived, hidden) VALUES (1, N'Management API Swagger', null, null, N'Management API', null, N'https://managementapi.zionusa.net/swagger-ui.html', N'X-APPLICATION-ID', null, 1, 0, 0);
INSERT INTO applications (id, name, apple_app_store_url, google_play_store_url, icon_title, icon_url, launch_url, unique_id, description, enabled, archived, hidden) VALUES (2, N'Archived', null, null, N'Archived', null, N'', N'Archived', null, 1, 1, 0);
INSERT INTO applications (id, name, apple_app_store_url, google_play_store_url, icon_title, icon_url, launch_url, unique_id, description, enabled, archived, hidden) VALUES (3, N'Hidden', null, null, N'Hidden', null, N'', N'Hidden', null, 1, 1, 1);
INSERT INTO applications (id, name, apple_app_store_url, google_play_store_url, icon_title, icon_url, launch_url, unique_id, description, enabled, archived, hidden) VALUES (4, N'Other', null, null, N'Other', null, N'', N'Other', null, 1, 1, 1);

--
-- User Supporting Data
--

-- Access
INSERT INTO accesses (id, name, sort_order, archived, hidden) VALUES (1, N'Admin', 1, 0, 0);
INSERT INTO accesses (id, name, sort_order, archived, hidden) VALUES (2, N'Branch', 3, 0, 0);
INSERT INTO accesses (id, name, sort_order, archived, hidden) VALUES (3, N'Group', 4, 0, 0);
INSERT INTO accesses (id, name, sort_order, archived, hidden) VALUES (4, N'Team', 5, 0, 0);
INSERT INTO accesses (id, name, sort_order, archived, hidden) VALUES (5, N'Member', 6, 0, 0);
INSERT INTO accesses (id, name, sort_order, archived, hidden) VALUES (6, N'Overseer', 2, 0, 0);
INSERT INTO accesses (id, name, sort_order, archived, hidden) VALUES (7, N'Archived', 2, 1, 0);
INSERT INTO accesses (id, name, sort_order, archived, hidden) VALUES (8, N'Hidden', 2, 1, 1);

-- Roles
INSERT INTO roles (id, name, sort_order, archived, hidden) VALUES (1, N'Overseer', 1, 0, 0);
INSERT INTO roles (id, name, sort_order, archived, hidden) VALUES (2, N'Branch Leader', 2, 0, 0);
INSERT INTO roles (id, name, sort_order, archived, hidden) VALUES (3, N'Group Leader', 3, 0, 0);
INSERT INTO roles (id, name, sort_order, archived, hidden) VALUES (4, N'Team Leader', 4, 0, 0);
INSERT INTO roles (id, name, sort_order, archived, hidden) VALUES (5, N'Member', 5, 0, 0);
INSERT INTO roles (id, name, sort_order, archived, hidden) VALUES (6, N'Archived', 6, 1, 0);
INSERT INTO roles (id, name, sort_order, archived, hidden) VALUES (7, N'Hidden', 7, 1, 1);
INSERT INTO roles (id, name, sort_order, archived, hidden) VALUES (8, N'Other', 7, 1, 1);

-- Titles
INSERT INTO titles (id, name, display_name, sort_order, archived, hidden) VALUES (1, N'Pastor', N'P.', 1, 0, 0);
INSERT INTO titles (id, name, display_name, sort_order, archived, hidden) VALUES (2, N'Elder', N'E.', 2, 0, 0);
INSERT INTO titles (id, name, display_name, sort_order, archived, hidden) VALUES (3, N'Senior Deaconess', N'K.', 3, 0, 0);
INSERT INTO titles (id, name, display_name, sort_order, archived, hidden) VALUES (4, N'Missionary', N'M.', 4, 0, 0);
INSERT INTO titles (id, name, display_name, sort_order, archived, hidden) VALUES (5, N'Deacon', N'D.', 5, 0, 0);
INSERT INTO titles (id, name, display_name, sort_order, archived, hidden) VALUES (6, N'Deaconess', N'D.', 6, 0, 0);
INSERT INTO titles (id, name, display_name, sort_order, archived, hidden) VALUES (7, N'Brother', N'B.', 7, 0, 0);
INSERT INTO titles (id, name, display_name, sort_order, archived, hidden) VALUES (8, N'Sister', N'S.', 8, 0, 0);
INSERT INTO titles (id, name, display_name, sort_order, archived, hidden) VALUES (9, N'Member', N'', 9, 1, 0);
INSERT INTO titles (id, name, display_name, sort_order, archived, hidden) VALUES (10, N'None', N'', 10, 1, 1);
INSERT INTO titles (id, name, display_name, sort_order, archived, hidden) VALUES (11, N'Other', N'', 10, 1, 1);

--
-- Branch Supporting Data
--

-- Countries
INSERT INTO countries (id, archived, full_name, hidden, short_name) VALUES (1, 0, N'United States of America', 0, N'U.S.A.');
INSERT INTO countries (id, archived, full_name, hidden, short_name) VALUES (2, 0, N'Canada', 0, N'Canada');
INSERT INTO countries (id, archived, full_name, hidden, short_name) VALUES (3, 0, N'Mexico', 0, N'Mexico');
INSERT INTO countries (id, archived, full_name, hidden, short_name) VALUES (4, 0, N'Haiti', 0, N'Haiti');
INSERT INTO countries (id, archived, full_name, hidden, short_name) VALUES (5, 0, N'Cuba', 0, N'Cuba');
INSERT INTO countries (id, archived, full_name, hidden, short_name) VALUES (6, 0, N'Bahamas', 0, N'Bahamas');
INSERT INTO countries (id, archived, full_name, hidden, short_name) VALUES (7, 0, N'Trinidad & Tobago', 0, N'Trinidad');
INSERT INTO countries (id, archived, full_name, hidden, short_name) VALUES (8, 0, N'Dominican Republic', 0, N'D.R.');
INSERT INTO countries (id, archived, full_name, hidden, short_name) VALUES (9, 0, N'Barbados', 0, N'Barbados');
INSERT INTO countries (id, archived, full_name, hidden, short_name) VALUES (10, 0, N'Jamaica', 0, N'Jamaica');
INSERT INTO countries (id, archived, full_name, hidden, short_name) VALUES (11, 0, N'Italy', 0, N'Italy');
INSERT INTO countries (id, archived, full_name, hidden, short_name) VALUES (12, 1, N'Greece', 0, N'Greece');
INSERT INTO countries (id, archived, full_name, hidden, short_name) VALUES (13, 0, N'Ghana', 1, N'Ghana');
INSERT INTO countries (id, archived, full_name, hidden, short_name) VALUES (14, 1, N'Grenada', 1, N'Grenada');


-- States
INSERT INTO states (id, full_name, short_name, archived, hidden) VALUES (1, N'Alabama', N'AL', 0, 0);
INSERT INTO states (id, full_name, short_name, archived, hidden) VALUES (2, N'Alaska', N'AK', 0, 0);
INSERT INTO states (id, full_name, short_name, archived, hidden) VALUES (3, N'Arizona', N'AZ', 0, 0);
INSERT INTO states (id, full_name, short_name, archived, hidden) VALUES (4, N'Arkansas', N'AR', 0, 0);
INSERT INTO states (id, full_name, short_name, archived, hidden) VALUES (5, N'California', N'CA', 0, 0);
INSERT INTO states (id, full_name, short_name, archived, hidden) VALUES (6, N'Colorado', N'CO', 0, 0);
INSERT INTO states (id, full_name, short_name, archived, hidden) VALUES (7, N'Connecticut', N'CT', 0, 0);
INSERT INTO states (id, full_name, short_name, archived, hidden) VALUES (8, N'Delaware', N'DE', 0, 0);
INSERT INTO states (id, full_name, short_name, archived, hidden) VALUES (9, N'District of Columbia', N'DC', 0, 0);
INSERT INTO states (id, full_name, short_name, archived, hidden) VALUES (10, N'Florida', N'FL', 0, 0);
INSERT INTO states (id, full_name, short_name, archived, hidden) VALUES (11, N'Georgia', N'GA', 0, 0);
INSERT INTO states (id, full_name, short_name, archived, hidden) VALUES (12, N'Hawaii', N'HI', 0, 0);
INSERT INTO states (id, full_name, short_name, archived, hidden) VALUES (13, N'Idaho', N'ID', 0, 0);
INSERT INTO states (id, full_name, short_name, archived, hidden) VALUES (14, N'Illinois', N'IL', 0, 0);
INSERT INTO states (id, full_name, short_name, archived, hidden) VALUES (15, N'Indiana', N'IN', 0, 0);
INSERT INTO states (id, full_name, short_name, archived, hidden) VALUES (16, N'Iowa', N'IA', 0, 0);
INSERT INTO states (id, full_name, short_name, archived, hidden) VALUES (17, N'Kansas', N'KS', 0, 0);
INSERT INTO states (id, full_name, short_name, archived, hidden) VALUES (18, N'Kentucky', N'KY', 0, 0);
INSERT INTO states (id, full_name, short_name, archived, hidden) VALUES (19, N'Louisiana', N'LA', 0, 0);
INSERT INTO states (id, full_name, short_name, archived, hidden) VALUES (20, N'Maine', N'ME', 0, 0);
INSERT INTO states (id, full_name, short_name, archived, hidden) VALUES (21, N'Maryland', N'MD', 0, 0);
INSERT INTO states (id, full_name, short_name, archived, hidden) VALUES (22, N'Massachusetts', N'MA', 0, 0);
INSERT INTO states (id, full_name, short_name, archived, hidden) VALUES (23, N'Michigan', N'MI', 0, 0);
INSERT INTO states (id, full_name, short_name, archived, hidden) VALUES (24, N'Minnesota', N'MN', 0, 0);
INSERT INTO states (id, full_name, short_name, archived, hidden) VALUES (25, N'Mississippi', N'MS', 0, 0);
INSERT INTO states (id, full_name, short_name, archived, hidden) VALUES (26, N'Montana', N'MT', 0, 0);
INSERT INTO states (id, full_name, short_name, archived, hidden) VALUES (27, N'Nebraska', N'NE', 0, 0);
INSERT INTO states (id, full_name, short_name, archived, hidden) VALUES (28, N'Nevada', N'NV', 0, 0);
INSERT INTO states (id, full_name, short_name, archived, hidden) VALUES (29, N'New Hampshire', N'NH', 0, 0);
INSERT INTO states (id, full_name, short_name, archived, hidden) VALUES (30, N'New Jersey', N'NJ', 0, 0);
INSERT INTO states (id, full_name, short_name, archived, hidden) VALUES (31, N'New Mexico', N'NM', 0, 0);
INSERT INTO states (id, full_name, short_name, archived, hidden) VALUES (32, N'New York', N'NY', 0, 0);
INSERT INTO states (id, full_name, short_name, archived, hidden) VALUES (33, N'North Carolina', N'NC', 0, 0);
INSERT INTO states (id, full_name, short_name, archived, hidden) VALUES (34, N'North Dakota', N'ND', 0, 0);
INSERT INTO states (id, full_name, short_name, archived, hidden) VALUES (35, N'Ohio', N'OH', 0, 0);
INSERT INTO states (id, full_name, short_name, archived, hidden) VALUES (36, N'Oklahoma', N'OK', 0, 0);
INSERT INTO states (id, full_name, short_name, archived, hidden) VALUES (37, N'Oregon', N'OR', 0, 0);
INSERT INTO states (id, full_name, short_name, archived, hidden) VALUES (38, N'Pennsylvania', N'PA', 0, 0);
INSERT INTO states (id, full_name, short_name, archived, hidden) VALUES (39, N'Rhode Island', N'RI', 0, 0);
INSERT INTO states (id, full_name, short_name, archived, hidden) VALUES (40, N'South Carolina', N'SC', 0, 0);
INSERT INTO states (id, full_name, short_name, archived, hidden) VALUES (41, N'South Dakota', N'SD', 0, 0);
INSERT INTO states (id, full_name, short_name, archived, hidden) VALUES (42, N'Tennessee', N'TN', 0, 0);
INSERT INTO states (id, full_name, short_name, archived, hidden) VALUES (43, N'Texas', N'TX', 0, 0);
INSERT INTO states (id, full_name, short_name, archived, hidden) VALUES (44, N'Utah', N'UT', 0, 0);
INSERT INTO states (id, full_name, short_name, archived, hidden) VALUES (45, N'Vermont', N'VT', 0, 0);
INSERT INTO states (id, full_name, short_name, archived, hidden) VALUES (46, N'Virginia', N'VA', 0, 0);
INSERT INTO states (id, full_name, short_name, archived, hidden) VALUES (47, N'Washington', N'WA', 0, 0);
INSERT INTO states (id, full_name, short_name, archived, hidden) VALUES (48, N'West Virginia', N'WV', 0, 0);
INSERT INTO states (id, full_name, short_name, archived, hidden) VALUES (49, N'Wisconsin', N'WI', 0, 0);
INSERT INTO states (id, full_name, short_name, archived, hidden) VALUES (50, N'Wyoming', N'WY', 0, 0);
INSERT INTO states (id, full_name, short_name, archived, hidden) VALUES (51, N'Bahamas', N'BA', 0, 0);
INSERT INTO states (id, full_name, short_name, archived, hidden) VALUES (52, N'Trinidad & Tobago', N'TT', 0, 0);
INSERT INTO states (id, full_name, short_name, archived, hidden) VALUES (53, N'Haiti', N'HI', 0, 0);
INSERT INTO states (id, full_name, short_name, archived, hidden) VALUES (54, N'Dominican Republic', N'DR', 0, 0);
INSERT INTO states (id, full_name, short_name, archived, hidden) VALUES (55, N'Puerto Rico', N'PR', 0, 0);
INSERT INTO states (id, full_name, short_name, archived, hidden) VALUES (56, N'Barbados', N'BB', 0, 0);
INSERT INTO states (id, full_name, short_name, archived, hidden) VALUES (57, N'Jamaica', N'JA', 0, 0);
INSERT INTO states (id, full_name, short_name, archived, hidden) VALUES (58, N'U.S. Virgin Islands', N'VI', 0, 0);
INSERT INTO states (id, full_name, short_name, archived, hidden) VALUES (59, N'Canada', N'CA', 0, 0);
INSERT INTO states (id, full_name, short_name, archived, hidden) VALUES (60, N'Cuba', N'CU', 0, 0);
INSERT INTO states (id, full_name, short_name, archived, hidden) VALUES (61, N'Italy', N'IT', 0, 0);
INSERT INTO states (id, full_name, short_name, archived, hidden) VALUES (62, N'Greece', N'GR', 0, 0);
INSERT INTO states (id, full_name, short_name, archived, hidden) VALUES (63, N'Ghana', N'GH', 0, 0);
INSERT INTO states (id, full_name, short_name, archived, hidden) VALUES (64, N'Missouri', N'MO', 0, 0);
INSERT INTO states (id, full_name, short_name, archived, hidden) VALUES (65, N'Grenada', N'GD', 0, 0);
INSERT INTO states (id, full_name, short_name, archived, hidden) VALUES (66, N'Archived 1', N'A1', 1, 0);
INSERT INTO states (id, full_name, short_name, archived, hidden) VALUES (67, N'Archived 2', N'A2', 1, 0);
INSERT INTO states (id, full_name, short_name, archived, hidden) VALUES (68, N'Archived 3', N'A3', 1, 0);
INSERT INTO states (id, full_name, short_name, archived, hidden) VALUES (69, N'Hidden 1', N'H1', 1, 1);
INSERT INTO states (id, full_name, short_name, archived, hidden) VALUES (70, N'Hidden 2', N'H2', 1, 1);

-- -- Files
-- INSERT INTO files (id, archived, data, hidden, name, type, display_name) VALUES (N'0d2585c4-922a-46b3-a6d8-b101c0e7bb27', 0, 0, 0, N'01', N'image/jpeg', N'1');
-- INSERT INTO files (id, archived, data, hidden, name, type, display_name) VALUES (N'0dfa518b-3bc3-4591-b1fb-c693168d1623', 0, 0, 0, N'02', N'image/jpeg', N'2');
-- INSERT INTO files (id, archived, data, hidden, name, type, display_name) VALUES (N'2d40a6dc-7aa5-4a54-8fb3-1bba70193796', 0, 0, 0, N'03', N'image/jpeg', N'3');
-- INSERT INTO files (id, archived, data, hidden, name, type, display_name) VALUES (N'53873d66-ef28-4865-a3f0-4c58ee1c7ad5', 0, 0, 0, N'HS', N'image/jpeg', N'H');
-- INSERT INTO files (id, archived, data, hidden, name, type, display_name) VALUES (N'81c76003-6e46-4b40-b68a-faa9b2e038ff', 0, 0, 0, N'04', N'image/jpeg', N'4');
-- INSERT INTO files (id, archived, data, hidden, name, type, display_name) VALUES (N'b67b7e70-662c-4119-bf65-99d463bd0a5d', 0, 0, 0, N'05', N'image/jpeg', N'5');
-- INSERT INTO files (id, archived, data, hidden, name, type, display_name) VALUES (N'c244769b-2fff-478b-8f23-78aa6bfe13c8', 1, 0, 0, N'06', N'image/jpeg', null);
-- INSERT INTO files (id, archived, data, hidden, name, type, display_name) VALUES (N'ddf9dfd6-403b-4777-bf0c-951faf287ecc', 0, 0, 1, N'De', N'image/jpeg', N'D');
-- INSERT INTO files (id, archived, data, hidden, name, type, display_name) VALUES (N'f107b737-6fa2-4caf-86f4-7882923008d1', 1, 0, 1, N'07', N'image/jpeg', null);

--
-- Organizational Structure
--

-- Associations
INSERT INTO associations (id, leader_id, name, archived, hidden, leader_two_id, picture_file_id, thumbnail_file_id) VALUES (1, null, N'Northeast', 0, 0, null, null, null);
INSERT INTO associations (id, leader_id, name, archived, hidden, leader_two_id, picture_file_id, thumbnail_file_id) VALUES (2, null, N'Southeast', 0, 0, null, null, null);
INSERT INTO associations (id, leader_id, name, archived, hidden, leader_two_id, picture_file_id, thumbnail_file_id) VALUES (3, null, N'N/A', 0, 0, null, null, null);
INSERT INTO associations (id, leader_id, name, archived, hidden, leader_two_id, picture_file_id, thumbnail_file_id) VALUES (4, null, N'Canada', 0, 0, null, null, null);
INSERT INTO associations (id, leader_id, name, archived, hidden, leader_two_id, picture_file_id, thumbnail_file_id) VALUES (5, null, N'West Central', 0, 0, null, null, null);
INSERT INTO associations (id, leader_id, name, archived, hidden, leader_two_id, picture_file_id, thumbnail_file_id) VALUES (6, null, N'North Central', 0, 0, null, null, null);
INSERT INTO associations (id, leader_id, name, archived, hidden, leader_two_id, picture_file_id, thumbnail_file_id) VALUES (7, null, N'South Central', 0, 0, null, null, null);
INSERT INTO associations (id, leader_id, name, archived, hidden, leader_two_id, picture_file_id, thumbnail_file_id) VALUES (8, null, N'West Coast', 0, 0, null, null, null);
INSERT INTO associations (id, leader_id, name, archived, hidden, leader_two_id, picture_file_id, thumbnail_file_id) VALUES (9, null, N'Archived', 1, 0, null, null, null);
INSERT INTO associations (id, leader_id, name, archived, hidden, leader_two_id, picture_file_id, thumbnail_file_id) VALUES (10, null, N'Hidden', 1, 1, null, null, null);

-- Main Branches
INSERT INTO main_branches (id, archived, association_id, hidden, leader_id, leader_two_id, name, picture_file_id, thumbnail_file_id) VALUES (1, 0, 1, 0, null, null, 'New Windsor, NY', null, null);
INSERT INTO main_branches (id, archived, association_id, hidden, leader_id, leader_two_id, name, picture_file_id, thumbnail_file_id) VALUES (2, 0, 1, 0, null, null, 'Belleville, NJ', null, null);
INSERT INTO main_branches (id, archived, association_id, hidden, leader_id, leader_two_id, name, picture_file_id, thumbnail_file_id) VALUES (3, 0, 1, 0, null, null, 'Pennsylvania, PA', null, null);
INSERT INTO main_branches (id, archived, association_id, hidden, leader_id, leader_two_id, name, picture_file_id, thumbnail_file_id) VALUES (4, 0, 1, 0, null, null, 'Washington, DC', null, null);
INSERT INTO main_branches (id, archived, association_id, hidden, leader_id, leader_two_id, name, picture_file_id, thumbnail_file_id) VALUES (5, 0, 1, 0, null, null, 'Ridgewood, NJ', null, null);
INSERT INTO main_branches (id, archived, association_id, hidden, leader_id, leader_two_id, name, picture_file_id, thumbnail_file_id) VALUES (6, 0, 1, 0, null, null, 'Bronx, NY', null, null);
INSERT INTO main_branches (id, archived, association_id, hidden, leader_id, leader_two_id, name, picture_file_id, thumbnail_file_id) VALUES (7, 0, 2, 0, null, null, 'Atlanta, GA', null, null);
INSERT INTO main_branches (id, archived, association_id, hidden, leader_id, leader_two_id, name, picture_file_id, thumbnail_file_id) VALUES (8, 0, 2, 0, null, null, 'Miami, FL', null, null);
INSERT INTO main_branches (id, archived, association_id, hidden, leader_id, leader_two_id, name, picture_file_id, thumbnail_file_id) VALUES (9, 0, 2, 0, null, null, 'Charlotte, NC', null, null);
INSERT INTO main_branches (id, archived, association_id, hidden, leader_id, leader_two_id, name, picture_file_id, thumbnail_file_id) VALUES (11, 0, 3, 1, null, null, 'Hidden', null, null);
INSERT INTO main_branches (id, archived, association_id, hidden, leader_id, leader_two_id, name, picture_file_id, thumbnail_file_id) VALUES (12, 1, 3, 1, null, null, 'Archived Hidden', null, null);
INSERT INTO main_branches (id, archived, association_id, hidden, leader_id, leader_two_id, name, picture_file_id, thumbnail_file_id) VALUES (13, 1, 3, 0, null, null, 'Archived', null, null);


-- Branch Types
INSERT INTO branches_types (id, name, sort_order, archived, hidden) VALUES (N'TEMPLE', N'Temple', 1, 0, 0);
INSERT INTO branches_types (id, name, sort_order, archived, hidden) VALUES (N'OFFICE', N'Office Church', 2, 0, 0);
INSERT INTO branches_types (id, name, sort_order, archived, hidden) VALUES (N'HOUSE', N'House Church', 3, 0, 0);
INSERT INTO branches_types (id, name, sort_order, archived, hidden) VALUES (N'BIBLE_STUDY_CENTER', N'Bible Study Center', 4, 0, 0);
INSERT INTO branches_types (id, name, sort_order, archived, hidden) VALUES (N'DEFAULT', N'Archived Hidden', 4, 1, 1);

-- Branches
INSERT INTO branches (id, address, archived, city, distant, hidden, latitude, leader_id, leader_two_id, longitude, main_branch_id, name, parent_branch_id, state_id, type_id, thumbnail_file_id, picture_file_id) VALUES (1, N'880 Jackson Avenue', 0, N'New Windsor', 0, 0, null, null, null, null, 1, N'New Windsor', null, 1, N'TEMPLE', null, null);
INSERT INTO branches (id, address, archived, city, distant, hidden, latitude, leader_id, leader_two_id, longitude, main_branch_id, name, parent_branch_id, state_id, type_id, thumbnail_file_id, picture_file_id) VALUES (2, N'1 Broadway', 0, N'New York', 0, 0, null, null, null, null, 1, N'New York', null, 1, N'OFFICE', null, null);
INSERT INTO branches (id, address, archived, city, distant, hidden, latitude, leader_id, leader_two_id, longitude, main_branch_id, name, parent_branch_id, state_id, type_id, thumbnail_file_id, picture_file_id) VALUES (3, N'1234 Street', 0, N'New Jersey', 0, 0, null, null, null, null, 1, N'New Jersey', null, 1, N'TEMPLE', null, null);
INSERT INTO branches (id, address, archived, city, distant, hidden, latitude, leader_id, leader_two_id, longitude, main_branch_id, name, parent_branch_id, state_id, type_id, thumbnail_file_id, picture_file_id) VALUES (4, N'240 Madison', 0, N'Central Jersey', 0, 0, null, null, null, null, 1, N'Central Jersey', null, 1, N'OFFICE', null, null);
INSERT INTO branches (id, address, archived, city, distant, hidden, latitude, leader_id, leader_two_id, longitude, main_branch_id, name, parent_branch_id, state_id, type_id, thumbnail_file_id, picture_file_id) VALUES (5, N'880 Jackson Avenue', 0, N'Boston', 0, 0, null, null, null, null, 1, N'Boston', null, 1, N'OFFICE', null, null);
INSERT INTO branches (id, address, archived, city, distant, hidden, latitude, leader_id, leader_two_id, longitude, main_branch_id, name, parent_branch_id, state_id, type_id, thumbnail_file_id, picture_file_id) VALUES (6, N'1 Route', 0, N'Parsippany', 0, 0, null, null, null, null, 1, N'Parsippany', null, 1, N'HOUSE', null, null);
INSERT INTO branches (id, address, archived, city, distant, hidden, latitude, leader_id, leader_two_id, longitude, main_branch_id, name, parent_branch_id, state_id, type_id, thumbnail_file_id, picture_file_id) VALUES (7, N'123 Ave', 0, N'Maine', 0, 0, null, null, null, null, 1, N'Maine', null, 1, N'BIBLE_STUDY_CENTER', null, null);
INSERT INTO branches (id, address, archived, city, distant, hidden, latitude, leader_id, leader_two_id, longitude, main_branch_id, name, parent_branch_id, state_id, type_id, thumbnail_file_id, picture_file_id) VALUES (8, N'1 Broadway', 1, N'Archived', 0, 0, null, null, null, null, 1, N'New York', null, 1, N'OFFICE', null, null);
INSERT INTO branches (id, address, archived, city, distant, hidden, latitude, leader_id, leader_two_id, longitude, main_branch_id, name, parent_branch_id, state_id, type_id, thumbnail_file_id, picture_file_id) VALUES (9, N'880 Jackson Avenue', 0, N'Hidden', 0, 1, null, null, null, null, 1, N'New Windsor', null, 1, N'TEMPLE', null, null);
INSERT INTO branches (id, address, archived, city, distant, hidden, latitude, leader_id, leader_two_id, longitude, main_branch_id, name, parent_branch_id, state_id, type_id, thumbnail_file_id, picture_file_id) VALUES (10, N'1 Broadway', 1, N'Archived Hidden', 0, 1, null, null, null, null, 1, N'New York', null, 1, N'OFFICE', null, null);

-- --
-- -- Below is a work in progress
-- --
--
-- INSERT INTO churches (id, association_id, parent_church_id, leader_id, name, type_id, address, phone, email, city,
--                       state_id, postal_code, archived)
-- VALUES (1, 1, null, null, 'Test Church 1', 1, '123 Test Address', null, null, null, 1, null, false);
-- INSERT INTO churches (id, association_id, parent_church_id, leader_id, name, type_id, address, phone, email, city,
--                       state_id, postal_code, archived)
-- VALUES (2, 1, 1, null, 'Test Branch Church 1', 2, '123 Test Address', null, null, null, 2, null, false);
-- INSERT INTO churches (id, association_id, parent_church_id, leader_id, name, type_id, address, phone, email, city,
--                       state_id, postal_code, archived)
-- VALUES (3, 1, 1, null, 'Test Branch Church 2', 3, '123 Test Address', null, null, null, 1, null, false);
-- INSERT INTO churches (id, association_id, parent_church_id, leader_id, name, type_id, address, phone, email, city,
--                       state_id, postal_code, archived)
-- VALUES (4, 1, 2, null, 'Test Branch Church 3', 3, '123 Test Address', null, null, null, 2, null, false);
--
-- INSERT INTO groups (id, church_id, leader_id, name, church_group, archived, effective_date)
-- VALUES (1, 1, null, 'Test Group 1', 0, 0, '2017-06-17 02:12:07.1900000');
-- INSERT INTO groups (id, church_id, leader_id, name, church_group, archived, effective_date)
-- VALUES (2, 1, null, 'Test Group 2', 1, 0, '2017-06-17 02:12:07.1900000');
-- INSERT INTO groups (id, church_id, leader_id, name, church_group, archived, effective_date)
-- VALUES (3, 2, null, 'Test Group 3', 0, 0, '2017-06-17 02:12:07.1900000');
-- INSERT INTO groups (id, church_id, leader_id, name, church_group, archived, effective_date)
-- VALUES (4, 3, null, 'Test Group 4', 1, 0, '2017-06-17 02:12:07.1900000');
-- INSERT INTO groups (id, church_id, leader_id, name, church_group, archived, effective_date)
-- VALUES (5, 4, null, 'Test Group 5', 1, 0, '2017-06-17 02:12:07.1900000');
-- INSERT INTO groups (id, church_id, leader_id, name, church_group, archived, effective_date)
-- VALUES (6, 2, null, 'Test Group 6', 1, 0, '2017-06-17 02:12:07.1900000');
--
-- INSERT INTO teams (id, group_id, leader_id, name, church_team, effective_date, archived)
-- VALUES (1, 1, null, 'Team1', 0, '2017-06-19 23:20:01.2900000', 0);
-- INSERT INTO teams (id, group_id, leader_id, name, church_team, effective_date, archived)
-- VALUES (2, 1, null, 'Team2', 0, '2017-06-19 23:20:10.5066667', 0);
-- INSERT INTO teams (id, group_id, leader_id, name, church_team, effective_date, archived)
-- VALUES (3, 1, null, 'Team3', 1, '2017-06-19 23:20:10.5066667', 0);
-- INSERT INTO teams (id, group_id, leader_id, name, church_team, effective_date, archived)
-- VALUES (4, 2, null, 'Team4', 1, '2017-06-19 23:20:10.5066667', 0);
-- INSERT INTO teams (id, group_id, leader_id, name, church_team, effective_date, archived)
-- VALUES (5, 3, null, 'Team5', 0, '2017-06-19 23:20:10.5066667', 0);
-- INSERT INTO teams (id, group_id, leader_id, name, church_team, effective_date, archived)
-- VALUES (6, 3, null, 'Team6', 0, '2017-06-19 23:20:10.5066667', 0);
-- INSERT INTO teams (id, group_id, leader_id, name, church_team, effective_date, archived)
-- VALUES (7, 4, null, 'Team7', 0, '2017-06-19 23:20:10.5066667', 0);
-- INSERT INTO teams (id, group_id, leader_id, name, church_team, effective_date, archived)
-- VALUES (8, 3, null, 'Team8', 0, '2017-06-19 23:20:10.5066667', 0);
-- INSERT INTO teams (id, group_id, leader_id, name, church_team, effective_date, archived)
-- VALUES (9, 4, null, 'Team9', 1, '2017-06-19 23:20:10.5066667', 0);
-- INSERT INTO teams (id, group_id, leader_id, name, church_team, effective_date, archived)
-- VALUES (10, 5, null, 'Team10', 0, '2017-06-19 23:20:10.5066667', 0);
-- INSERT INTO teams (id, group_id, leader_id, name, church_team, effective_date, archived)
-- VALUES (11, 6, null, 'Team11', 1, '2017-06-19 23:20:10.5066667', 0);
-- INSERT INTO teams (id, group_id, leader_id, name, church_team, effective_date, archived)
-- VALUES (12, 3, null, 'Team12', 0, '2017-06-19 23:20:10.5066667', 0);
--
-- INSERT INTO users (id, active_directory_id, team_id, access_id, role_id, title_id, first_name, middle_name, last_name,
--                    user_name, gender, archived, married, theological_student, account_not_expired,
--                    credentials_not_expired, account_not_locked, ga_grader, teacher, ready_grader)
-- VALUES (1, 'f2c296da-497c-a359-66c3d57500c9', 1, 1, 1, 1, 'Test1', 'Test', 'User', 'patrick.asare67@zionusa.org', 'M',
--         0, 1, 1, 0, 1, 1, 1, 0, 0, 0);
-- INSERT INTO users (id, active_directory_id, team_id, access_id, role_id, title_id, first_name, middle_name, last_name,
--                    user_name, gender, archived, married, theological_student, account_not_expired,
--                    credentials_not_expired, account_not_locked, ga_grader, teacher, ready_grader)
-- VALUES (2, null, 2, 5, 5, 5, 'Test2', 'Test', 'User', 'patrick.asare1@zionusa.org', 'M', 0, 1, 1, 1, 1, 1, 1, 0, 0, 0);
-- INSERT INTO users (id, active_directory_id, team_id, access_id, role_id, title_id, first_name, middle_name, last_name,
--                    user_name, gender, archived, married, theological_student, account_not_expired,
--                    credentials_not_expired, account_not_locked, ga_grader, teacher, ready_grader)
-- VALUES (3, null, 1, 1, 1, 1, 'Test3', 'Test', 'User', 'patrick.asare9@zionusa.org', 'M', 0, 1, 0, 0, 1, 1, 1, 0, 0, 0);
-- INSERT INTO users (id, active_directory_id, team_id, access_id, role_id, title_id, first_name, middle_name, last_name,
--                    user_name, gender, archived, married, theological_student, account_not_expired,
--                    credentials_not_expired, account_not_locked, ga_grader, teacher, ready_grader)
-- VALUES (4, null, 3, 1, 1, 1, 'Test4', 'Test', 'User', 'patrick.asare2@zionusa.org', 'F', 0, 1, 0, 1, 0, 1, 1, 0, 0, 0);
-- INSERT INTO users (id, active_directory_id, team_id, access_id, role_id, title_id, first_name, middle_name, last_name,
--                    user_name, gender, archived, married, theological_student, account_not_expired,
--                    credentials_not_expired, account_not_locked, ga_grader, teacher, ready_grader)
-- VALUES (5, null, 2, 1, 1, 1, 'Test5', 'Test', 'User', 'patrick.asare10@zionusa.org', 'M', 0, 1, 0, 1, 1, 1, 1, 0, 0, 0);
-- INSERT INTO users (id, active_directory_id, team_id, access_id, role_id, title_id, first_name, middle_name, last_name,
--                    user_name, gender, archived, married, theological_student, account_not_expired,
--                    credentials_not_expired, account_not_locked, ga_grader, teacher, ready_grader)
-- VALUES (6, null, 10, 1, 1, 1, 'Test6', 'Test', 'User', 'patrick.asare3@zionusa.org', 'M', 0, 1, 1, 1, 1, 1, 1, 0, 0, 0);
-- INSERT INTO users (id, active_directory_id, team_id, access_id, role_id, title_id, first_name, middle_name, last_name,
--                    user_name, gender, archived, married, theological_student, account_not_expired,
--                    credentials_not_expired, account_not_locked, ga_grader, teacher, ready_grader)
-- VALUES (7, null, 2, 1, 1, 1, 'Test7', 'Test', 'User', 'patrick.asare11@zionusa.org', 'M', 0, 1, 1, 1, 1, 1, 1, 0, 0, 0);
-- INSERT INTO users (id, active_directory_id, team_id, access_id, role_id, title_id, first_name, middle_name, last_name,
--                    user_name, gender, archived, married, theological_student, account_not_expired,
--                    credentials_not_expired, account_not_locked, ga_grader, teacher, ready_grader)
-- VALUES (8, null, 4, 1, 1, 1, 'Test8', 'Test', 'User', 'patrick.asare4@zionusa.org', 'F', 0, 1, 1, 1, 1, 1, 1, 0, 0, 0);
-- INSERT INTO users (id, active_directory_id, team_id, access_id, role_id, title_id, first_name, middle_name, last_name,
--                    user_name, gender, archived, married, theological_student, account_not_expired,
--                    credentials_not_expired, account_not_locked, ga_grader, teacher, ready_grader)
-- VALUES (9, null, 5, 1, 1, 1, 'Test9', 'Test', 'User', 'patrick.asare5@zionusa.org', 'M', 0, 1, 1, 0, 1, 1, 1, 0, 0, 0);
-- INSERT INTO users (id, active_directory_id, team_id, access_id, role_id, title_id, first_name, middle_name, last_name,
--                    user_name, gender, archived, married, theological_student, account_not_expired,
--                    credentials_not_expired, account_not_locked, ga_grader, teacher, ready_grader)
-- VALUES (10, null, 8, 1, 1, 1, 'Test10', 'Test', 'User', 'patrick.asare6@zionusa.org', 'M', 0, 1, 1, 1, 1, 1, 1, 0, 0,
--         0);
-- INSERT INTO users (id, active_directory_id, team_id, access_id, role_id, title_id, first_name, middle_name, last_name,
--                    user_name, gender, archived, married, theological_student, account_not_expired,
--                    credentials_not_expired, account_not_locked, ga_grader, teacher, ready_grader)
-- VALUES (11, null, 6, 1, 1, 1, 'Test11', 'Test', 'User', 'patrick.asare7@zionusa.org', 'M', 0, 1, 1, 1, 1, 1, 1, 0, 0,
--         0);
-- INSERT INTO users (id, active_directory_id, team_id, access_id, role_id, title_id, first_name, middle_name, last_name,
--                    user_name, gender, archived, married, theological_student, account_not_expired,
--                    credentials_not_expired, account_not_locked, ga_grader, teacher, ready_grader)
-- VALUES (12, null, 11, 1, 1, 1, 'Test12', 'Test', 'User', 'patrick.asare8@zionusa.org', 'F', 1, 0, 1, 1, 1, 1, 1, 0, 0,
--         0);
--
-- INSERT INTO church_organization (id, church_id, organization_data)
-- VALUES (1, 1, 'Mock Data');
-- INSERT INTO church_organization (id, church_id, organization_data)
-- VALUES (2, 2, 'Mock Data');
-- INSERT INTO church_organization (id, church_id, organization_data)
-- VALUES (3, 4, 'Mock Data');
--
-- INSERT INTO transfer_requests (id, user_id, current_team_id, current_team_name, current_group_id, current_group_name,
--                                current_church_id, current_church_name, new_team_id, new_team_name, new_group_id,
--                                new_group_name, new_church_id, new_church_name, request_status, request_date, comment,
--                                member_name, reviewer_id, reviewer_name)
-- VALUES (1, 1, 1, 'B. Adrian Hernandez', 1, 'B. Adrian Hernandez', 1, 'Aguadilla', 1, 'Burke', 1, 'Burke', 1, 'Burke',
--         'P', '2017-07-11 00:00:00.000', 'I am cool', 'Tester 1', null, null);
-- INSERT INTO transfer_requests (id, user_id, current_team_id, current_team_name, current_group_id, current_group_name,
--                                current_church_id, current_church_name, new_team_id, new_team_name, new_group_id,
--                                new_group_name, new_church_id, new_church_name, request_status, request_date, comment,
--                                member_name, reviewer_id, reviewer_name)
-- VALUES (2, 2, 1, 'B. Adrian Hernandez', 1, 'B. Adrian Hernandez', 1, 'Aguadilla', 5, 'Canada', 5, 'Canada', 5, 'Canada',
--         'P', '2017-07-11 00:00:00.000', 'I am cool', 'Tester 1', null, null);
-- INSERT INTO transfer_requests (id, user_id, current_team_id, current_team_name, current_group_id, current_group_name,
--                                current_church_id, current_church_name, new_team_id, new_team_name, new_group_id,
--                                new_group_name, new_church_id, new_church_name, request_status, request_date, comment,
--                                member_name, reviewer_id, reviewer_name)
-- VALUES (3, 3, 1, 'B. Adrian Hernandez', 1, 'B. Adrian Hernandez', 1, 'Aguadilla', 5, 'Canada', 5, 'Canada', 5, 'Canada',
--         'A', '2017-07-11 00:00:00.000', 'I am cool', 'Tester 1', null, null);
-- INSERT INTO transfer_requests (id, user_id, current_team_id, current_team_name, current_group_id, current_group_name,
--                                current_church_id, current_church_name, new_team_id, new_team_name, new_group_id,
--                                new_group_name, new_church_id, new_church_name, request_status, request_date, comment,
--                                member_name, reviewer_id, reviewer_name)
-- VALUES (4, 1, 1, 'B. Adrian Hernandez', 1, 'B. Adrian Hernandez', 1, 'Aguadilla', 5, 'Canada', 5, 'Canada', 5, 'Canada',
--         'D', '2017-07-11 00:00:00.000', 'I am cool', 'Tester 1', null, null);
