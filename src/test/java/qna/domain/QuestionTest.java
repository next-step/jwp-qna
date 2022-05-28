package qna.domain;

public class QuestionTest {
    public static final Question Q1 = new Question("title1", "contents1").writeBy(UserTest.JAVAJIGI);
    public static final Question Q2 = new Question("title2", "contents2").writeBy(UserTest.SANJIGI);

    public static Question getQuestion1(User user) {
        return new Question("title1", "contents1").writeBy(user);
    }

    public static Question getQuestion2(User user) {
        return new Question("title2", "contents2").writeBy(user);
    }

}