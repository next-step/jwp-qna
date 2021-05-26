package qna.domain;

import java.time.LocalDateTime;

public class DeleteHistoryTest {
    public static final DeleteHistory D1 = new DeleteHistory(ContentType.ANSWER, 1L, UserTest.JAVAJIGI, LocalDateTime.now());
    public static final DeleteHistory D2 = new DeleteHistory(ContentType.QUESTION, 2L, UserTest.SANJIGI, LocalDateTime.now());
}
