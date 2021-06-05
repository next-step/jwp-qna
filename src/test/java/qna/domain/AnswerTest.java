package qna.domain;

public class AnswerTest {
    public static final Answer A1 = new Answer(UserTest.JAVAJIGI, QuestionTest.Q1, "Answers Contents1");
    public static final Answer A2 = new Answer(UserTest.SANJIGI, QuestionTest.Q1, "Answers Contents2");

    public static final Answer DELETED_ANSWER1 = new Answer(UserTest.SANJIGI, QuestionTest.Q1, "Deleted Content1");
    public static final Answer DELETED_ANSWER2 = new Answer(UserTest.SANJIGI, QuestionTest.Q1, "Deleted Content2");
    public static final Answer DELETED_ANSWER3 = new Answer(UserTest.SANJIGI, QuestionTest.Q1, "Deleted Content3");
}
