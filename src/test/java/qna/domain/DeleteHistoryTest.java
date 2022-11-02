package qna.domain;

import java.time.LocalDateTime;

public class DeleteHistoryTest {
    public static final DeleteHistory QH = new DeleteHistory(ContentType.QUESTION, 1L, 1L, LocalDateTime.now());
    public static final DeleteHistory AH = new DeleteHistory(ContentType.ANSWER, 1L, 1L, LocalDateTime.now());
}
