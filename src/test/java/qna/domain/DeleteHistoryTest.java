package qna.domain;

import java.time.LocalDateTime;

public class DeleteHistoryTest {
    public static final DeleteHistory DH1 = new DeleteHistory(ContentType.QUESTION, 1L, UserTest.JAVAJIGI, LocalDateTime.now());
    public static final DeleteHistory DH2 = new DeleteHistory(ContentType.ANSWER, 2L, UserTest.SANJIGI                                                                                     , LocalDateTime.now());
}