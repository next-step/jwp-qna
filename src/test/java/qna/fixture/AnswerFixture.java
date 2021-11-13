package qna.fixture;

import qna.domain.Answer;
import qna.domain.Question;
import qna.domain.User;

public class AnswerFixture {
    private AnswerFixture() {
        throw new UnsupportedOperationException();
    }

    public static Answer create(User user, Question question, String contents) {
        return create(null, user, question, contents);
    }

    public static Answer create(Long id, User user, Question question, String contents) {
        return new Answer(id, user, question, contents);
    }
}
