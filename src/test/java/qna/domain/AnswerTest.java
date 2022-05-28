package qna.domain;

public class AnswerTest {
    public static final Answer A1 = new Answer(UserTest.JAVAJIGI, QuestionTest.Q1, "Answers Contents1");
    public static final Answer A2 = new Answer(UserTest.SANJIGI, QuestionTest.Q1, "Answers Contents2");

    public static final Answer getAnswer1(User user, Question question) {
        return new Answer(user, question, "Answers Contents1");
    }

    public static final Answer getAnswer2(User user, Question question) {
        return new Answer(user, question, "Answers Contents2");
    }
}