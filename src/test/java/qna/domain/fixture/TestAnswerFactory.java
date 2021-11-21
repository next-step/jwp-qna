package qna.domain.fixture;

import qna.domain.Answer;
import qna.domain.Question;
import qna.domain.User;

public class TestAnswerFactory {

    public static Answer create(final User writer, final Question question) {
        return new Answer(writer, question, "contents");
    }
}
