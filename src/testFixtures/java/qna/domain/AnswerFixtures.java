package qna.domain;

public class AnswerFixtures {
    public static Answer A1 = new Answer(UserFixtures.JAVAJIGI, QuestionFixtures.Q1, "Answers Contents1");
    public static Answer A2 = new Answer(UserFixtures.SANJIGI, QuestionFixtures.Q2, "Answers Contents2");

    public static Answer create(User user, Question question, String content) {
        return new Answer(user, question, content);
    }

    public static Answer createDefault(User user, Question question) {
        return new Answer(user, question, "Answers Contets");
    }
}
