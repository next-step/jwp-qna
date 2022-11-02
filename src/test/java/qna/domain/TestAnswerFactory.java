package qna.domain;

public class TestAnswerFactory {

    Answer create(User writer, Question question) {
        return new Answer(writer, question, "contents");
    }
}
