INSERT INTO user_information (id, user_id, password, name, email, create_at, updated_at)
VALUES
(1, 'A', 'bush123', 'bush', 'bush@gmail.com', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(2, 'B', 'obama123!', 'obama', 'obama@gmail.com', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(3, 'C', 'trump456@', 'trump', 'trump@gmail.com', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(4, 'D', 'biden475!', 'biden', 'biden@gmail.com', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO question (id, writer_id, title, contents, deleted, create_at, updated_at)
VALUES
(1, 1, '질문 타이틀1', '질문1', 'false', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(2, 2, '질문 타이틀2', '질문2', 'false', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(3, 3, '질문 타이틀3', '질문3', 'false', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(4, 4, '질문 타이틀4', '질문4', 'false', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO answer (id, writer_id, question_id, contents, deleted, create_at, updated_at)
VALUES
(1, 1, 1, '답변1', 'false', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(2, 2, 2, '답변2', 'false', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(3, 3, 3, '답변3', 'false', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(4, 4, 4, '답변4', 'false', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);