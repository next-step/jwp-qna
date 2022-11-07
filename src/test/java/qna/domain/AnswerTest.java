package qna.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import qna.NotFoundException;
import qna.UnAuthenticationException;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class AnswerTest {
    public static final Answer A1 = new Answer(UserTest.JAVAJIGI, QuestionTest.Q1, "Answers Contents1");
    public static final Answer A2 = new Answer(UserTest.SANJIGI, QuestionTest.Q1, "Answers Contents2");

    @Test
    @DisplayName("Answer writer Not Null Error Test")
    void Answer_writer_not_null() {
        assertThatThrownBy(() -> {
           Answer testAnswer = new Answer(null, QuestionTest.Q1, "Answer Contents1");
        }).isInstanceOf(UnAuthenticationException.class);
    }

    @Test
    @DisplayName("Answer question Not Null ErrorTest")
    void Answer_question_not_null() {
        assertThatThrownBy(() -> {
            Answer testAnswer = new Answer(UserTest.JAVAJIGI, null, "Answer Contents1");
        }).isInstanceOf(NotFoundException.class);
    }
}
