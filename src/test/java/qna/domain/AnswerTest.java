package qna.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import qna.NotFoundException;
import qna.UnAuthorizedException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

public class AnswerTest {
    public static final Answer A1 = new Answer(UserTest.JAVAJIGI, QuestionTest.Q1, "Answers Contents1");
    public static final Answer A2 = new Answer(UserTest.SANJIGI, QuestionTest.Q1, "Answers Contents2");

    @Test
    @DisplayName("답변 작성 시, 작성자가 null이면 오류가 발생한다.")
    void checkExceptionCreateAnswerWithWriterIsNull() {
        assertThatThrownBy(() -> new Answer(1L, null, QuestionTest.Q1, "wow~")).isExactlyInstanceOf(UnAuthorizedException.class);
    }

    @Test
    @DisplayName("답변 작성 시, 질문이 null이면 오류가 발생한다.")
    void checkExceptionCreateAnswerWithQuestionIsNull() {
        assertThatThrownBy(() -> new Answer(1L, UserTest.JAVAJIGI, null, "wow~!")).isExactlyInstanceOf(NotFoundException.class);
    }

    @Test
    @DisplayName("글의 작성자(주인)을 확인한다.")
    void checkOwner() {
        assertAll(() -> assertThat(A1.isOwner(UserTest.JAVAJIGI)).isTrue());
        assertAll(() -> assertThat(A2.isOwner(UserTest.SANJIGI)).isTrue());
    }

    @Test
    @DisplayName("질문 등록할 수 있다.")
    void checkToQuestion() {
        assertAll(() -> {
            A1.setQuestion(QuestionTest.Q2);
            assertThat(A1.getQuestion().getId()).isEqualTo(QuestionTest.Q2.getId());
        });
        assertAll(() -> {
            A2.setQuestion(QuestionTest.Q2);
            assertThat(A2.getQuestion().getId()).isEqualTo(QuestionTest.Q2.getId());
        });
    }
}
