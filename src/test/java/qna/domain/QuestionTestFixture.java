package qna.domain;

public class QuestionTestFixture {
    public static final Question Q1 = new Question("title1", "contents1").writeBy(UserTestFixture.JAVAJIGI);
    public static final Question Q2 = new Question("title2", "contents2").writeBy(UserTestFixture.SANJIGI);
}
