package qna.fixture;

import qna.domain.Question;
import qna.domain.User;

public class TestQuestionFactory {
    public static Question create(User writer) {
        Question question = new Question("title", "contents");
        question.writeBy(writer);
        return question;
    }
}
