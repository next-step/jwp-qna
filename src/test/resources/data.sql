---- USER
insert into `user` (id, email, `name`, password, user_id, created_at, updated_at) values (5, 'josh@gmail.com', 'josh', 'password', 'joshua', CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP());
insert into `user` (id, email, `name`, password, user_id, created_at, updated_at) values (6, 'navi@gmail.com', 'navi', 'password', 'navi', CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP());
insert into `user` (id, email, `name`, password, user_id, created_at, updated_at) values (7, 'dodo@gmail.com', 'dodo', 'password', 'dodo', CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP());
insert into `user` (id, email, `name`, password, user_id, created_at, updated_at) values (8, 'zeze@gmail.com', 'zeze', 'password', 'zeze', CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP());

---- Question
insert into `question` (id, title, contents, deleted, writer_id, created_at, updated_at) values (5, 'title1', 'contents dodododo', 0, 5, CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP());
insert into `question` (id, title, contents, deleted, writer_id, created_at, updated_at) values (6, 'title6', 'contents six contents', 0, 6, CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP());
insert into `question` (id, title, contents, deleted, writer_id, created_at, updated_at) values (7, 'title7', 'contents seven dodododo', 0, 7, CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP());
insert into `question` (id, title, contents, deleted, writer_id, created_at, updated_at) values (8, 'title8', 'contents eight dodododo', 0, 6, CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP());
insert into `question` (id, title, contents, deleted, writer_id, created_at, updated_at) values (9, 'title9', 'contents nine dodododo', 0, 5, CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP());

---- Answer
insert into `answer` (id, contents, question_id, writer_id, deleted, created_at, updated_at) values (1, 'contents1', 5, 6, 0, CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP());
insert into `answer` (id, contents, question_id, writer_id, deleted, created_at, updated_at) values (2, 'contents2', 5, 7, 0, CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP());
insert into `answer` (id, contents, question_id, writer_id, deleted, created_at, updated_at) values (3, 'contents3', 5, 8, 0, CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP());
insert into `answer` (id, contents, question_id, writer_id, deleted, created_at, updated_at) values (4, 'contents4', 5, 8, 0, CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP());
insert into `answer` (id, contents, question_id, writer_id, deleted, created_at, updated_at) values (5, 'contents5', 5, 7, 0, CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP());
insert into `answer` (id, contents, question_id, writer_id, deleted, created_at, updated_at) values (6, 'contents6', 5, 6, 0, CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP());
insert into `answer` (id, contents, question_id, writer_id, deleted, created_at, updated_at) values (7, 'contents7', 5, 7, 0, CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP());
insert into `answer` (id, contents, question_id, writer_id, deleted, created_at, updated_at) values (8, 'contents8', 5, 8, 0, CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP());
