package qna.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import qna.NotFoundException;
import qna.UnAuthorizedException;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class AnswerTest {
    public static final Answer A1 = new Answer(UserTest.JAVAJIGI, QuestionTest.Q1, "Answers Contents1");
    public static final Answer A2 = new Answer(UserTest.SANJIGI, QuestionTest.Q1, "Answers Contents2");

    @DisplayName("작성자가 없는 답변은 예외가 발생한다")
    @Test
    void no_writer_exception_test() {
        assertThatThrownBy(() -> new Answer(null, QuestionTest.Q1, "컨텐츠"))
                .isInstanceOf(UnAuthorizedException.class);
    }

    @DisplayName("질문이 없는 답변은 예외가 발생한다")
    @Test
    void no_question_exception_test() {
        assertThatThrownBy(() -> new Answer(UserTest.JAVAJIGI, null, "컨텐츠"))
                .isInstanceOf(NotFoundException.class);
    }
}
