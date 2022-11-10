package qna.domain;

public class AnswerTest {
    public static final String ANSWERS_CONTENTS_1 = "Answers Contents1";
    public static final String ANSWERS_CONTENTS_2 = "Answers Contents2";
    public static final Answer ANSWER_1 = new Answer(1L, UserTest.JAVAJIGI, QuestionTest.Q1, ANSWERS_CONTENTS_1);
    public static final Answer ANSWER_2 = new Answer(2L, UserTest.SANJIGI, QuestionTest.Q1, ANSWERS_CONTENTS_2);
}
