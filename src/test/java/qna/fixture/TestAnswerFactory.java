package qna.fixture;

import qna.domain.Answer;
import qna.domain.Question;
import qna.domain.User;

public class TestAnswerFactory {
    public static Answer create(User writer, Question question) {
        return new Answer(writer, question, "contents");
    }
}
