package qna.domain;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static qna.domain.AnswerTest.A1;
import static qna.domain.QuestionTest.Q1;

public class DeleteHistoryTest {
    public static final DeleteHistory D1 = new DeleteHistory(ContentType.ANSWER, A1.getId(), A1.getWriterId(), LocalDateTime.now());
    public static final DeleteHistory D2 = new DeleteHistory(ContentType.QUESTION, Q1.getId(), Q1.getWriterId(), LocalDateTime.now());
}