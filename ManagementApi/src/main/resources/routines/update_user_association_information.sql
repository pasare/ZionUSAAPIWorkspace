CREATE procedure [dbo].[update_user_association_information] AS
BEGIN
    SET NOCOUNT ON;
    DECLARE @churchId INT, @groupId INT, @teamId INT, @userId INT;
    DECLARE @churchName VARCHAR(75), @groupName VARCHAR(75), @teamName VARCHAR(75);

--cursor for users table
    DECLARE users_table CURSOR LOCAL FOR select id, team_id from users

    OPEN users_table;

    FETCH NEXT FROM users_table INTO @userId ,@teamId

    WHILE @@FETCH_STATUS = 0
        BEGIN
            select @groupId = group_id, @teamName = name from teams where id = @teamId;
            select @churchId = church_id, @groupName = name from groups where id = @groupId;
            select @churchName = name from churches where id = @churchId;
            update users
            set team_name   = @teamName,
                group_id    = @groupId,
                group_name  = @groupName,
                church_id   = @churchId,
                church_name = @churchName
            where id = @userId;

            FETCH NEXT FROM users_table INTO @userId ,@teamId
        END

    CLOSE users_table;
    DEALLOCATE users_table;

END
go

