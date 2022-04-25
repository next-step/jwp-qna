package qna.domain;

public class QuestionTest {
    public static final Question Q1 = new Question("title1", new Contents("contents1")).writeBy(UserTest.JAVAJIGI);
    public static final Question Q2 = new Question("title2", new Contents("contents2")).writeBy(UserTest.SANJIGI);
    public static final Question Q3 = new Question("title3", new Contents("contents3")).writeBy(UserTest.TESTUSER);
    public static final Question Q4 = new Question("title4", new Contents("contents4")).writeBy(UserTest.TESTUSER);
    public static final Question Q5 = new Question("title5", new Contents("contents5")).writeBy(UserTest.TESTUSER);
    public static final Question Q6 = new Question("title6", new Contents("contents6")).writeBy(UserTest.TESTUSER);
}
