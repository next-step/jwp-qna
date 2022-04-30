package qna.domain;

public class QuestionTest {
    public static final Question Q1 = new Question("title1", "contents1").writeBy(UserTest.JAVAJIGI);
    public static final Question Q2 = new Question("title2", "contents2").writeBy(UserTest.SANJIGI);
    public static final Question Q3 = new Question("title3", "contents3").writeBy(UserTest.TESTUSER);
    public static final Question Q4 = new Question("title4", "contents4").writeBy(UserTest.TESTUSER);
    public static final Question Q5 = new Question("title5", "contents5").writeBy(UserTest.TESTUSER);
    public static final Question Q6 = new Question("title6", "contents6").writeBy(UserTest.TESTUSER);
}
