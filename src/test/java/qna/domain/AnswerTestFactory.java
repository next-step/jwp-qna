package qna.domain;

public class AnswerTestFactory {
    private AnswerTestFactory() {
        throw new UnsupportedOperationException();
    }

    public static Answer create(User user, Question question, String contents) {
        return new Answer(user, question, contents);
    }

    public static Answer create(Long id, User user, Question question, String contents) {
        return new Answer(id, user, question, contents);
    }
}
