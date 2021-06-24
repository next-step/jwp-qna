package qna.domain;

public class QuestionTest {
    public static final Question Q1 = new Question("title1", "contents1");
    public static final Question Q2 = new Question("title2", "contents2");

    static {
        Q1.writtenBy(UserTest.JAVAJIGI);
        Q2.writtenBy(UserTest.SANJIGI);
        Q2.delete();
    }
}
