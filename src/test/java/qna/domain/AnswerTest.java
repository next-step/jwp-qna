package qna.domain;

public class AnswerTest {
    public static final String A1_CONTENT = "Answers Contents1";
    public static final String A2_CONTENT = "Answers Contents2";

    public static final Answer A1 = new Answer(UserTest.JAVAJIGI, QuestionTest.Q1, A1_CONTENT);
    public static final Answer A2 = new Answer(UserTest.SANJIGI, QuestionTest.Q1, A2_CONTENT);
}
