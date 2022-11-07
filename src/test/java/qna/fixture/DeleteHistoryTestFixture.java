package qna.fixture;

import qna.domain.ContentType;
import qna.domain.DeleteHistory;

import java.time.LocalDateTime;

public class DeleteHistoryTestFixture {
    public static final DeleteHistory D1 = new DeleteHistory(ContentType.QUESTION, 1L, UserTestFixture.JAVAJIGI, LocalDateTime.now());
    public static final DeleteHistory D2 = new DeleteHistory(ContentType.ANSWER, 1L, UserTestFixture.SANJIGI, LocalDateTime.now());
}
