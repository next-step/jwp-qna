package qna.domain.answer;

import qna.domain.question.Question;
import qna.domain.user.User;

public class AnswerTestFactory {

    public static Answer create(User writer, Question question, String content) {
        return create(writer, question, content, false);
    }

    public static Answer create(User writer, Question question, String content, boolean deleted) {
        return new Answer(writer, question, content, deleted);
    }
}
