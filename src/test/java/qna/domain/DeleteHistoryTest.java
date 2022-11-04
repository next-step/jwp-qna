package qna.domain;

import java.time.LocalDateTime;

public class DeleteHistoryTest {
    public static final DeleteHistory DH1 =
            new DeleteHistory(ContentType.ANSWER, 1L, 1L, LocalDateTime.now());
}
