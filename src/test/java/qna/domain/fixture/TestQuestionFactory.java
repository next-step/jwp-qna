package qna.domain.fixture;

import qna.domain.Question;
import qna.domain.User;

public class TestQuestionFactory {

    public static Question create(final User user) {
        return new Question("title", "contents", user);
    }
}
