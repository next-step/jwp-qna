-- USERS
INSERT INTO user (id, user_id, password, name, email) VALUES (1, 'javajigi', 'password', 'name', 'javajigi@slipp.net');
INSERT INTO user (id, user_id, password, name, email) VALUES (2, 'sanjigi', 'password', 'name', 'sanjigi@slipp.net');

-- QUESTIONS
INSERT INTO question (id, title, contents, writer_id, deleted) VALUES (1, 'title1', 'contents1', 1, false);
INSERT INTO question (id, title, contents, writer_id, deleted) VALUES (2, 'title2', 'contents2', 2, false);

-- ANSWERS
INSERT INTO answer (writer_id, question_id, contents, deleted) VALUES (1, 1, 'Answers Contents1', false);
INSERT INTO answer (writer_id, question_id, contents, deleted) VALUES (2, 1, 'Answers Contents2', false);