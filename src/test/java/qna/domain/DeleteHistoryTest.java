package qna.domain;

import java.time.LocalDateTime;

public class DeleteHistoryTest {
    //ContentType contentType, Long contentId, Long deletedById, LocalDateTime createDate
    public static final DeleteHistory history1 = new DeleteHistory(ContentType.QUESTION, 1L, UserTest.JAVAJIGI, LocalDateTime.now());
    public static final DeleteHistory history2 = new DeleteHistory(ContentType.QUESTION, 2L, UserTest.SANJIGI, LocalDateTime.now());
}
