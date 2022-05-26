package qna.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import qna.NotFoundException;
import qna.UnAuthorizedException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static qna.domain.QuestionTest.Q1;

public class AnswerTest {
    public static final Answer A1 = new Answer(UserTest.JAVAJIGI, Q1, "Answers Contents1");
    public static final Answer A2 = new Answer(UserTest.SANJIGI, Q1, "Answers Contents2");

    @Test
    @DisplayName("답변의 작성자가 null일 경우 예외를 발생시킨다.")
    void UnAuthorizedException() {
        assertThatThrownBy(() -> new Answer(3L, null, Q1, "answer contents"))
                .isExactlyInstanceOf(UnAuthorizedException.class);
    }

    @Test
    @DisplayName("답변에 해당하는 질문이 null일 경우 예외를 발생시킨다.")
    void NotFoundException() {
        assertThatThrownBy(() -> new Answer(3L, UserTest.JAVAJIGI, null, "answer contents"))
                .isExactlyInstanceOf(NotFoundException.class);
    }

    @Test
    @DisplayName("답변의 작성자가 맞는지 확인한다.")
    void isOwner() {
        assertThat(A1.isOwner(UserTest.JAVAJIGI))
                .isTrue();
        assertThat(A2.isOwner(UserTest.SANJIGI))
                .isTrue();
    }

    @Test
    @DisplayName("입력받은 질문 Id로 변경한다.")
    void toQuestion() {
        Question question = new Question(10L, "Test Question", "Test Question Contents");
        A1.toQuestion(question);

        assertThat(A1.getQuestionId())
                .isEqualTo(question.getId())
                .isEqualTo(10L);
    }
}
