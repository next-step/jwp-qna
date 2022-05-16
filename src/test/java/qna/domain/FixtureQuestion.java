package qna.domain;

public final class FixtureQuestion {
    public static final Question Q1 = new Question("title1", "contents1").writeBy(FixtureUser.JAVAJIGI);
    public static final Question Q2 = new Question("title2", "contents2").writeBy(FixtureUser.SANJIGI);

    private FixtureQuestion() {
    }
}
