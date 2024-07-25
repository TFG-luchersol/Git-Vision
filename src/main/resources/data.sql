-- git_token_1 -> "$2a$10$qZbZlKF8x6PvuFir3lNU8u2ZbA/x64BZLmTyS1D7LBnyHmKaESMTS"

INSERT INTO users(id,username,github_token,clockify_token,email,avatar_url) VALUES 
                 (1,'username_1','$2a$10$qZbZlKF8x6PvuFir3lNU8u2ZbA/x64BZLmTyS1D7LBnyHmKaESMTS','clockify_token_1','email_1@gmail.com','https://avatars.githubusercontent.com/u/1'),
                 (2,'username_2','git_token_2','clockify_token_2','email_2@gmail.com','https://avatars.githubusercontent.com/u/2'),
                 (3,'username_3','git_token_3','clockify_token_3','email_3@gmail.com','https://avatars.githubusercontent.com/u/3'),
                 (4,'username_4','git_token_4','clockify_token_4','email_4@gmail.com','https://avatars.githubusercontent.com/u/4'),
                 (5,'username_5','git_token_5','clockify_token_5','email_5@gmail.com','https://avatars.githubusercontent.com/u/5'),
                 (6,'username_6','git_token_6','clockify_token_6','email_6@gmail.com','https://avatars.githubusercontent.com/u/6');

INSERT INTO github_users(id,username,email,avatar_url) VALUES 
                        (1,'username_1','email_1@gmail.com','https://avatars.githubusercontent.com/u/1'),
                        (2,'username_2','email_2@gmail.com','https://avatars.githubusercontent.com/u/2'),
                        (3,'username_3','email_3@gmail.com','https://avatars.githubusercontent.com/u/3'),
                        (4,'username_4','email_4@gmail.com','https://avatars.githubusercontent.com/u/4'),
                        (5,'username_5','email_5@gmail.com','https://avatars.githubusercontent.com/u/5'),
                        (6,'username_6','email_6@gmail.com','https://avatars.githubusercontent.com/u/6');

INSERT INTO repositories(id,name,update_date) VALUES 
                        (1,'repo_1','2024-01-01 00:00:00'),
                        (2,'repo_2','2024-01-01 00:00:00'),
                        (3,'repo_3','2024-01-01 00:00:00'),
                        (4,'repo_4','2024-01-01 00:00:00');

INSERT INTO collaborators(id,repository_id,collaborator_id) VALUES
                         (1,1,1),
                         (2,1,2),
                         (3,1,3),
                         (4,2,3),
                         (5,2,4),
                         (6,2,5),
                         (7,3,5),
                         (8,4,6);

INSERT INTO commits(id,message,date,additions,deletions,author_id,repository_id) VALUES 
                   (1,'message_1','2003-01-01 16:00:49',10,2,1,1),
                   (2,'message_2','2003-01-01 17:00:49',0,1,1,1),
                   (3,'message_3','2003-01-01 18:00:49',23,4,1,1),
                   (4,'message_4','2003-01-01 19:00:49',5,2,1,1),
                   (5,'message_5','2003-02-02 19:30:49',1,1,1,1),
                   (6,'message_6','2003-03-01 16:00:49',1,0,1,1),
                   (7,'message_7','2003-03-01 16:00:49',0,1,2,1),
                   (8,'message_8','2003-01-02 16:00:49',0,0,2,1),
                   (9,'message_9','2003-01-03 16:00:49',14,5,2,1),
                   (10,'message_10','2004-01-04 16:00:49',12,2,2,1),
                   (11,'message_11','2004-02-01 23:00:49',4,54,2,1),
                   (12,'message_12','2004-02-02 16:00:49',23,44,3,1),
                   (13,'message_13','2004-02-02 16:00:49',65,90,4,2),
                   (14,'message_14','2004-02-02 16:00:49',1,23,4,2),
                   (15,'message_15','2004-02-03 16:00:49',43,5,5,3),
                   (16,'message_16','2004-02-04 16:00:49',12,34,5,3),
                   (17,'message_17','2004-02-04 17:00:49',23,45,5,4),
                   (18,'message_18','2004-02-05 16:00:49',56,2,6,4);

INSERT INTO issues(id,title,number,repository_id) VALUES
                  (1,'issue_1',1,1),
                  (2,'issue_2',2,1),
                  (3,'issue_3',3,1),
                  (4,'issue_4',4,1),
                  (5,'issue_5',5,1),
                  (6,'issue_6',6,1),
                  (7,'issue_7',1,2),
                  (8,'issue_8',2,2),
                  (9,'issue_9',3,2),
                  (10,'issue_10',1,3),
                  (11,'issue_11',2,3);

INSERT INTO issues_commits(id,issue_id,commit_id) VALUES
                          (1,1,1),
                          (2,1,2),
                          (3,2,1),
                          (4,2,3),
                          (5,3,4),
                          (6,3,5),
                          (7,4,5);

INSERT INTO files(id,path,extension,repository_id) VALUES  
                 (1,'src/main/java/claseJava.java','java',1),
                 (2,'src/main/java/claseJava2.java','java',1),
                 (3,'src/main/python/clasePython.py','py',1),
                 (4,'src/main/c/claseC.c','c',1),
                 (5,'src/main/cpp/claseCpp.cpp','cpp',1),
                 (6,'src/main/cpp/claseCpp.c++','c++',1),
                 (7,'README.md','md',1),
                 (8,'SinExtension',null,1),
                 (9,'README.md','md',2);
                 

-- INSERT INTO organizations(id,name) VALUES ();
-- INSERT INTO issues(id,message) VALUES ();
-- INSERT INTO files(id,route,name) VALUES ();