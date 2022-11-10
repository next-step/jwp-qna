package qna.domain;

import java.time.LocalDateTime;

import static qna.domain.AnswerTest.ANSWER_1;
import static qna.domain.QuestionTest.QUESTION_1;
import static qna.domain.UserTest.JAVAJIGI;

public class DeleteHistoryTest {
    public static final DeleteHistory DELETE_HISTORY_ANSWER = new DeleteHistory(ContentType.ANSWER, ANSWER_1.getId(), JAVAJIGI.getId(), LocalDateTime.now());
    public static final DeleteHistory DELETE_HISTORY_QUESTION = new DeleteHistory(ContentType.QUESTION, QUESTION_1.getId(), QUESTION_1.getWriterId(), LocalDateTime.now());
}
