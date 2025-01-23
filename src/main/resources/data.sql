-- -- git_token_1 -> "$2a$10$qZbZlKF8x6PvuFir3lNU8u2ZbA/x64BZLmTyS1D7LBnyHmKaESMTS"

-- INSERT INTO users(id,username,password,github_token,clockify_token,email,avatar_url) VALUES 
--                  (1,'username_1','$2a$10$qZbZlKF8x6PvuFir3lNU8u2ZbA/x64BZLmTyS1D7LBnyHmKaESMTS',
--                  'github_token_1','clockify_token_1','email_1@gmail.com',
--                  'https://avatars.githubusercontent.com/u/1');

-- INSERT INTO user_repository(id,user_id,repository_id,name,token) VALUES
--                            (1,1,1,"owner_1/repo_1","token_1");

-- INSERT INTO user_workspace(id,user_id,workspace_id,name) VALUES
--                           (1,1,'1','User_1_Workspace_1');

-- INSERT INTO linker_repo_work(id,user_repo_id,user_workspace_id) VALUES
--                             (1,1,1);