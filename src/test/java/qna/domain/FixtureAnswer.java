package qna.domain;

public final class FixtureAnswer {
    public static final Answer A1 = new Answer(FixtureUser.JAVAJIGI, FixtureQuestion.Q1, "Answers Contents1");
    public static final Answer A2 = new Answer(FixtureUser.SANJIGI, FixtureQuestion.Q1, "Answers Contents2");

    private FixtureAnswer() {
    }
}
