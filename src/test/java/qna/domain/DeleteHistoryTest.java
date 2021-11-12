package qna.domain;

import qna.deletehistory.DeleteHistory;

public class DeleteHistoryTest {
    public static final DeleteHistory DH_ANSWER = new DeleteHistory(1L, ContentType.ANSWER, 1L, UserTest.JAVAJIGI);
    public static final DeleteHistory DH_QUESTION = new DeleteHistory(2L, ContentType.QUESTION, 2L, UserTest.SANJIGI);
}
