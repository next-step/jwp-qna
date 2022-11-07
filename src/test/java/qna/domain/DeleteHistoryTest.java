package qna.domain;

import java.time.LocalDateTime;

public class DeleteHistoryTest {

    public static final DeleteHistory questionDeleted =
        new DeleteHistory(ContentType.QUESTION, 1L, 1L, LocalDateTime.now());
    private static final DeleteHistory answerDeleted =
        new DeleteHistory(ContentType.ANSWER, 1L, 1L, LocalDateTime.now());

}