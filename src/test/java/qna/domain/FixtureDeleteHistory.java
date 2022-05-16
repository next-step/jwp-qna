package qna.domain;

import static qna.domain.FixtureAnswer.A1;
import static qna.domain.FixtureQuestion.Q1;

public final class FixtureDeleteHistory {

    public static final DeleteHistory DH_A1 = new AnswerDeleteHistory(A1);
    public static final DeleteHistory DH_Q1 = new QuestionDeleteHistory(Q1);

    private FixtureDeleteHistory() {
    }
}
