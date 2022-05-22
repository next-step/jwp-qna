package qna.domain;

import java.time.LocalDateTime;

public class DeleteHistoryTest {
    public static final DeleteHistory DH1 = new DeleteHistory(ContentType.ANSWER, AnswerTest.A1.getQuestionId(), UserTest.JAVAJIGI.getId(), LocalDateTime.now());
    public static final DeleteHistory DH2 = new DeleteHistory(ContentType.ANSWER, AnswerTest.A2.getQuestionId(), UserTest.SANJIGI.getId(), LocalDateTime.now());
}
