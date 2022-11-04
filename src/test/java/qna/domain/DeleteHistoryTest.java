package qna.domain;

import static qna.domain.AnswerTest.A1;
import static qna.domain.AnswerTest.A2;
import static qna.domain.UserTest.JAVAJIGI;
import static qna.domain.UserTest.SANJIGI;

import java.time.LocalDateTime;

public class DeleteHistoryTest {
    public static final DeleteHistory D1 = new DeleteHistory(ContentType.ANSWER, A1.getId(), JAVAJIGI.getId(),
            LocalDateTime.now());
    public static final DeleteHistory D2 = new DeleteHistory(ContentType.ANSWER, A2.getId(), SANJIGI.getId(),
            LocalDateTime.now());

}
