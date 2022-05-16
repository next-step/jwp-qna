package qna.domain;

import java.time.LocalDateTime;

public final class FixtureDeleteHistory {

    public static final DeleteHistory DH_A1 = new DeleteHistory(ContentType.ANSWER, 1L, 1L, LocalDateTime.now());
    public static final DeleteHistory DH_Q1 = new DeleteHistory(ContentType.QUESTION, 1L, 1L, LocalDateTime.now());

    private FixtureDeleteHistory() {
    }
}
