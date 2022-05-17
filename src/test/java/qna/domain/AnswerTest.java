package qna.domain;

public class AnswerTest {
    public static final Answer A1 = new Answer.AnswerBuilder(UserTest.JAVAJIGI, QuestionTest.Q1)
            .contents("Answers Contents1")
            .build();
    public static final Answer A2 = new Answer.AnswerBuilder(UserTest.SANJIGI, QuestionTest.Q1)
            .contents("Answers Contents2")
            .build();
}
