package qna.domain;

import qna.domain.answer.Answer;
import qna.domain.answer.Contents;

public class AnswerTest {
    public static final Answer A1 = new Answer(UserTest.JAVAJIGI, QuestionTest.Q1, new Contents("Answers Contents1"));
    public static final Answer A2 = new Answer(UserTest.SANJIGI, QuestionTest.Q1, new Contents("Answers Contents2"));
}
