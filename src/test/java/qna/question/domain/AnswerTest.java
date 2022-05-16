package qna.question.domain;

import qna.user.domain.UserTest;

public class AnswerTest {
    public static final Answer A1 = new Answer(null, UserTest.JAVAJIGI, QuestionTest.Q1, "Answers Contents1");
    public static final Answer A2 = new Answer(null, UserTest.SANJIGI, QuestionTest.Q1, "Answers Contents2");
}
