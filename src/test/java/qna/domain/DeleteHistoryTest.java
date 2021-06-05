package qna.domain;

import java.time.LocalDateTime;

import static qna.domain.AnswerTest.*;
import static qna.domain.ContentType.ANSWER;
import static qna.domain.ContentType.QUESTION;
import static qna.domain.QuestionTest.DELETED_QUESTION1;
import static qna.domain.QuestionTest.DELETED_QUESTION2;
import static qna.domain.UserTest.JORDY;

public class DeleteHistoryTest {
    public static final DeleteHistory DELETE_HISTORY_ANSWER1 = new DeleteHistory(ANSWER, DELETED_ANSWER1.getId(), JORDY.getId(), LocalDateTime.now());
    public static final DeleteHistory DELETE_HISTORY_ANSWER2 = new DeleteHistory(ANSWER, DELETED_ANSWER2.getId(), JORDY.getId(), LocalDateTime.now());
    public static final DeleteHistory DELETE_HISTORY_ANSWER3 = new DeleteHistory(ANSWER, DELETED_ANSWER3.getId(), JORDY.getId(), LocalDateTime.now());

    public static final DeleteHistory DELETE_HISTORY_QUESTION1 = new DeleteHistory(QUESTION, DELETED_QUESTION1.getId(), JORDY.getId(), LocalDateTime.now());
    public static final DeleteHistory DELETE_HISTORY_QUESTION2 = new DeleteHistory(QUESTION, DELETED_QUESTION2.getId(), JORDY.getId(), LocalDateTime.now());
}
