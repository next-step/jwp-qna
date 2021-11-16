package qna.domain;

public class TestAnswerFactory {

    private TestAnswerFactory() {

    }

    public static Answer create(User writer, String contents) {
        return new Answer(writer, contents);
    }

}
