package qna.domain;

public class QuestionTest {
    public static final Question QUESTION1 = new Question("title1", "contents1").writeBy(UserTest.JAVAJIGI);
    public static final Question QUESTION2 = new Question("title2", "contents2").writeBy(UserTest.SANJIGI);
}
