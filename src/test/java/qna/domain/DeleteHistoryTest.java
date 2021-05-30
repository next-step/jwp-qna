package qna.domain;

import java.time.LocalDateTime;

public class DeleteHistoryTest {
    public static final DeleteHistory DELETE_HISTORY1 = new DeleteHistory(ContentType.ANSWER, AnswerTest.ANSWER1.getId(), AnswerTest.ANSWER1.getWriterId(), LocalDateTime.now());
    public static final DeleteHistory DELETE_HISTORY2 = new DeleteHistory(ContentType.QUESTION, QuestionTest.QUESTION1.getId(), QuestionTest.QUESTION1.getWriterId(), LocalDateTime.now());
}
