package qna.fixture;

import qna.domain.Answer;

public class AnswerTestFixture {
    public static final Answer A1 = new Answer(UserTestFixture.JAVAJIGI, QuestionTestFixture.Q1, "Answers Contents1");
    public static final Answer A2 = new Answer(UserTestFixture.SANJIGI, QuestionTestFixture.Q1, "Answers Contents2");
}
