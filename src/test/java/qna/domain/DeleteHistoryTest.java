package qna.domain;

public class DeleteHistoryTest {
    public static final DeleteHistory HISTORY1 = DeleteHistory.ofQuestion(0L, UserTest.JAVAJIGI);
    public static final DeleteHistory HISTORY2 = DeleteHistory.ofAnswer(0L, UserTest.SANJIGI);
}
