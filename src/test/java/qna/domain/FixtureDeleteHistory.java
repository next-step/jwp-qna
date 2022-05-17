package qna.domain;

import static qna.domain.FixtureAnswer.A1;
import static qna.domain.FixtureQuestion.Q1;

public final class FixtureDeleteHistory {

    public static final DeleteHistory DH_A1 = DeleteHistory.ofAnswer(A1);
    public static final DeleteHistory DH_Q1 = DeleteHistory.ofQuestion(Q1);

    private FixtureDeleteHistory() {
    }
}
