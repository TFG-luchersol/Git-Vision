-- -- git_token_1 -> "$2a$10$qZbZlKF8x6PvuFir3lNU8u2ZbA/x64BZLmTyS1D7LBnyHmKaESMTS"

INSERT INTO users(id,username,github_token,clockify_token,email,avatar_url) VALUES 
                 (1,'username_1','$2a$10$qZbZlKF8x6PvuFir3lNU8u2ZbA/x64BZLmTyS1D7LBnyHmKaESMTS','clockify_token_1','email_1@gmail.com','https://avatars.githubusercontent.com/u/1'),
                 (2,'username_2','git_token_2','clockify_token_2','email_2@gmail.com','https://avatars.githubusercontent.com/u/2'),
                 (3,'username_3','git_token_3','clockify_token_3','email_3@gmail.com','https://avatars.githubusercontent.com/u/3'),
                 (4,'username_4','git_token_4','clockify_token_4','email_4@gmail.com','https://avatars.githubusercontent.com/u/4'),
                 (5,'username_5','git_token_5','clockify_token_5','email_5@gmail.com','https://avatars.githubusercontent.com/u/5'),
                 (6,'username_6','git_token_6','clockify_token_6','email_6@gmail.com','https://avatars.githubusercontent.com/u/6');

INSERT INTO user_repository(id,user_id,repository_id,repository_name) VALUES
                           (1,1,1);

INSERT INTO user_workspace(id,user_id,workspace_id,name) VALUES
                          (1,1,'1','User_1_Workspace_1');