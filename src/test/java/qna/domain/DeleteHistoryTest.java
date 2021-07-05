package qna.domain;

import java.time.LocalDateTime;

public class DeleteHistoryTest {
    public static final DeleteHistory HISTORY1 = new DeleteHistory(ContentType.QUESTION, 0L, UserTest.JAVAJIGI, LocalDateTime.now());
    public static final DeleteHistory HISTORY2 = new DeleteHistory(ContentType.ANSWER, 0L, UserTest.SANJIGI, LocalDateTime.now());
}
