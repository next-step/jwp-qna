package qna.domain;

public class QuestionTest {
    public static final Question Q1 = Question.of("title1", "contents1").writeBy(UserTest.JAVAJIGI);
    public static final Question Q2 = Question.of("title2", "contents2").writeBy(UserTest.SANJIGI);
}
