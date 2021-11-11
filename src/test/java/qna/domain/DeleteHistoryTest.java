package qna.domain;

public class DeleteHistoryTest {
    public static final DeleteHistory DH_ANSWER = new DeleteHistory(1L, ContentType.ANSWER, 1L, 1L);
    public static final DeleteHistory DH_QUESTION = new DeleteHistory(2L, ContentType.QUESTION, 2L, 2L);
}
