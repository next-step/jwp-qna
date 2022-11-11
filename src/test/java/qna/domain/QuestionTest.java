package qna.domain;

public class QuestionTest {
    public static final Question Q1 = new Question(new Title("title1"), new Contents("contents1")).writeBy(UserTest.JAVAJIGI);
    public static final Question Q2 = new Question(new Title("title2"), new Contents("contents2")).writeBy(UserTest.SANJIGI);
}
