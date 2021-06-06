package qna.domain;

public class QuestionTest {
    public static final Question Q1 = new Question("title1", "contents1").writeBy(UserTest.JAVAJIGI);
    public static final Question Q2 = new Question("title2", "contents2").writeBy(UserTest.SANJIGI);

    public static final Question DELETED_QUESTION1 = new Question("deleted question title1", "deleted question content1").writeBy(UserTest.SANJIGI);
    public static final Question DELETED_QUESTION2 = new Question("deleted question title2", "deleted question content2").writeBy(UserTest.SANJIGI);

    static {
        DELETED_QUESTION1.setDeleted(true);
        DELETED_QUESTION2.setDeleted(true);
    }
}
