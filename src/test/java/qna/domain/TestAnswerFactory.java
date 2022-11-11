package qna.domain;

public class TestAnswerFactory {
    public static Answer create(User writer, Question question) {
        return new Answer(writer, question, "contents");
    }
}
