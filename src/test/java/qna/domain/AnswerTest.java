package qna.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatNoException;

@DisplayName("답변")
public class AnswerTest {
    public static final String ANSWERS_CONTENTS_1 = "Answers Contents1";
    public static final String ANSWERS_CONTENTS_2 = "Answers Contents2";
    public static final Answer ANSWER_1 = new Answer(1L, UserTest.JAVAJIGI, QuestionTest.QUESTION_1, ANSWERS_CONTENTS_1);
    public static final Answer ANSWER_2 = new Answer(2L, UserTest.SANJIGI, QuestionTest.QUESTION_2, ANSWERS_CONTENTS_2);

    @DisplayName("답변 생성")
    @Test
    void constructor() {
        assertThatNoException().isThrownBy(() -> new Answer(1L, UserTest.JAVAJIGI, QuestionTest.QUESTION_1, ANSWERS_CONTENTS_1));
    }
}
