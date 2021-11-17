insert into user(id, user_id, password, name, email, created_at)
values (1L, 'javajigi', 'password', 'user1', 'javajigi@slipp.net', '2021-11-11 17:50:57.246979'),
       (2L, 'sanjigi', 'password', 'user2', 'sanjigi@slipp.net', '2021-11-11 17:50:57.246979');

insert into question(id, title, contents, writer_id, deleted, created_at)
values (1L, 'question1', 'contents1', 1L, false, '2021-11-11 17:50:57.246979'),
       (2L, 'question2', 'contents2', 2L, false, '2021-11-11 17:50:57.246979'),
       (3L, 'question3', 'contents2', 1L, true, '2021-11-11 17:50:57.246979');

insert into answer(id, writer_id, question_id, contents, deleted, created_at)
values (1L, 2L, 1L, 'Answers Contents1', false, '2021-11-11 17:50:57.246979'),
       (2L, 1L, 2L, 'Answers Contents1', false, '2021-11-11 17:50:57.246979'),
       (3L, 2L, 2L, 'Answers Contents2', false, '2021-11-11 17:50:57.246979'),
       (4L, 1L, 2L, 'Answers Contents3', false, '2021-11-11 17:50:57.246979'),
       (5L, 1L, 2L, 'Answers Contents4', true, '2021-11-11 17:50:57.246979');