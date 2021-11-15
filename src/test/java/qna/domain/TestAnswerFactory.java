package qna.domain;

public class TestAnswerFactory {

    private TestAnswerFactory() {

    }

    public static Answer create(User writer, Question question, String contents) {
        return new Answer(writer, question, contents);
    }

}
