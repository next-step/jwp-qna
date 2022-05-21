package qna.domain;

import java.time.LocalDateTime;

public class DeleteHistoryTest {
    public static final DeleteHistory D1 = new DeleteHistory(ContentType.ANSWER, AnswerTest.A1.getId(),
            UserTest.JAVAJIGI.getId(), LocalDateTime.now());
    public static final DeleteHistory D2 = new DeleteHistory(ContentType.QUESTION, QuestionTest.Q1.getId(),
            UserTest.SANJIGI.getId(), LocalDateTime.now());
}
