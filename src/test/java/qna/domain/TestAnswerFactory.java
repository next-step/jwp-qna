package qna.domain;

public class TestAnswerFactory {

    public static Answer create(Long id, User writer, Question question, String contents) {
        return new Answer(id, writer, question, contents);
    }

    public static Answer create(User writer, Question question, String contents) {
        return new Answer(writer, question, contents);
    }
}
