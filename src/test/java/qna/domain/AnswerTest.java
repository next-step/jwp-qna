package qna.domain;

public class AnswerTest {
    public static final Answer A1 = new Answer(UserTest.JAVAJIGI, QuestionTest.Q1, new Contents("Answers Contents1"));
    public static final Answer A2 = new Answer(UserTest.SANJIGI, QuestionTest.Q2, new Contents("Answers Contents2"));
    public static final Answer A3 = new Answer(UserTest.TESTUSER, QuestionTest.Q3, new Contents("Answers Contents3"));
    public static final Answer A4 = new Answer(UserTest.TESTUSER, QuestionTest.Q4, new Contents("Answers Contents4"));
}
