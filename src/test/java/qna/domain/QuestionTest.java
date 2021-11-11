package qna.domain;

public class QuestionTest {
    public static final Question Q1 = new Question(1L,"title1", "contents1").writeBy(UserTest.JAVAJIGI);
    public static final Question Q2 = new Question("title2", "contents2").writeBy(UserTest.SANJIGI);
}
