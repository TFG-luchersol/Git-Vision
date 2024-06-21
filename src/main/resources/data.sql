
INSERT INTO users(id,username,github_token,clockify_token,email,avatar_url) VALUES 
                 (1,'user_1','git_token_1','clockify_token_1','email_1','avatar_url_1'),
                 (2,'user_2','git_token_2','clockify_token_2','email_2','avatar_url_2'),
                 (3,'user_3','git_token_3','clockify_token_3','email_3','avatar_url_3'),
                 (4,'user_4','git_token_4','clockify_token_4','email_4','avatar_url_4'),
                 (5,'user_5','git_token_5','clockify_token_5','email_5','avatar_url_5'),
                 (6,'user_6','git_token_6','clockify_token_6','email_6','avatar_url_6');

INSERT INTO commits(id,message,date,additions,deletions,author_id) VALUES 
                   (1,'message_1','2003-01-01 16:00:49',10,2,1),
                   (2,'message_2','2003-01-01 17:00:49',0,1,1),
                   (3,'message_3','2003-01-01 18:00:49',23,4,1),
                   (4,'message_4','2003-01-01 19:00:49',5,2,1),
                   (5,'message_5','2003-02-02 19:30:49',1,1,1),
                   (6,'message_6','2003-03-01 16:00:49',1,0,1),
                   (7,'message_7','2003-03-01 16:00:49',0,1,2),
                   (8,'message_8','2003-01-02 16:00:49',0,0,2),
                   (9,'message_9','2003-01-03 16:00:49',14,5,2),
                   (10,'message_10','2004-01-04 16:00:49',12,2,2),
                   (11,'message_11','2004-02-01 23:00:49',4,54,2),
                   (12,'message_12','2004-02-02 16:00:49',23,44,3),
                   (13,'message_13','2004-02-02 16:00:49',65,90,4),
                   (14,'message_14','2004-02-02 16:00:49',1,23,4),
                   (15,'message_15','2004-02-03 16:00:49',43,5,5),
                   (16,'message_16','2004-02-04 16:00:49',12,34,5),
                   (17,'message_17','2004-02-04 17:00:49',23,45,5),
                   (18,'message_18','2004-02-05 16:00:49',56,2,5);

-- INSERT INTO organizations(id,name) VALUES ();
-- INSERT INTO repositories(id,name) VALUES ();
-- INSERT INTO issues(id,message) VALUES ();
-- INSERT INTO files(id,route,name) VALUES ();