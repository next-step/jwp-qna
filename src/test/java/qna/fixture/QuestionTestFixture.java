package qna.fixture;

import qna.domain.Question;

public class QuestionTestFixture {
    public static final Question Q1 = new Question(1L, UserTestFixture.JAVAJIGI, "title1", "contents1").writeBy(UserTestFixture.JAVAJIGI);
    public static final Question Q2 = new Question(2L, UserTestFixture.SANJIGI, "title2", "contents2").writeBy(UserTestFixture.SANJIGI);
}
