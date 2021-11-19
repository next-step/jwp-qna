package qna.domain;

public class TestAnswerFactory {
    public static Answer create() {
        Question question = TestQuestionFactory.create();
        return new Answer(question.getWriter(), question, "Answers Contents1");
    }
}