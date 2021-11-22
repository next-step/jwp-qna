package qna.domain.fixture;

import qna.domain.Answer;
import qna.domain.Answers;

public class TestAnswersFactory {

    public static Answers create(Answer answer) {
        return Answers.of(answer);
    }
}
