package qna.domain;

public class QuestionTest {
    public static final String Q1_TITLE = "title1";
    public static final String Q1_CONTENT = "contents1";

    public static final Question Q1 = new Question(Q1_TITLE, Q1_CONTENT).writeBy(UserTest.JAVAJIGI);
    public static final Question Q2 = new Question("title2", "contents2").writeBy(UserTest.SANJIGI);
}
