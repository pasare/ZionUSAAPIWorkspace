IF NOT EXISTS (select * FROM sys.columns WHERE  object_id = OBJECT_ID(N'[dbo].[event_teams]') AND name = 'audio_visual_engineer_id') BEGIN
    exec sp_rename 'event_teams.audio_visual', audio_visual_engineer_id, 'COLUMN';
    exec sp_rename 'event_teams.graphics', graphic_designer_id, 'COLUMN';
    exec sp_rename 'event_teams.public_relations', public_relations_representative_id, 'COLUMN';

    exec sp_rename 'event_teams_aud.audio_visual', audio_visual_engineer_id, 'COLUMN';
    exec sp_rename 'event_teams_aud.graphics', graphic_designer_id, 'COLUMN';
    exec sp_rename 'event_teams_aud.public_relations', public_relations_representative_id, 'COLUMN';
END