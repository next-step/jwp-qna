package qna.domain;

public class QuestionTest {
    public static final Question Q1 = new Question("title1", Contents.of("contents1")).writeBy(UserTest.JAVAJIGI);
    public static final Question Q2 = new Question("title2", Contents.of("contents2")).writeBy(UserTest.SANJIGI);
}
