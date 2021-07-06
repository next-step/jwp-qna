package qna.domain;

import qna.CannotDeleteException;

public class QuestionTest {
    public static final Question Q1 = new Question("title1", "contents1");
    public static final Question Q2 = new Question("title2", "contents2");

    static {
        Q1.writtenBy(UserTest.JAVAJIGI);
        Q2.writtenBy(UserTest.SANJIGI);
        try {
            Q2.delete(UserTest.SANJIGI);
        } catch (CannotDeleteException e) {
            e.printStackTrace();
        }
    }
}
