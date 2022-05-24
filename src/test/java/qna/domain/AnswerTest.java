package qna.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static qna.domain.QuestionTest.Q1;
import static qna.domain.UserTest.JAVAJIGI;
import static qna.domain.UserTest.SANJIGI;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import qna.exception.CannotDeleteException;
import qna.exception.NotFoundException;
import qna.exception.UnAuthorizedException;

class AnswerTest {
    public static final Answer A1 = new Answer(JAVAJIGI, Q1, "Answers Contents1");
    public static final Answer A2 = new Answer(SANJIGI, Q1, "Answers Contents2");

    @Test
    @DisplayName("객체 생성 확인")
    void verifyAnswer() {
        assertThat(new Answer(JAVAJIGI, Q1, "contents")).isEqualTo(new Answer(JAVAJIGI, Q1, "contents"));
    }

    @Test
    @DisplayName("유저 정보가 없으면 UnAuthorizedException이 발생")
    void inputNullUserThenUnAuthorizedException() {
        assertThatExceptionOfType(UnAuthorizedException.class)
                .isThrownBy(() -> new Answer(null, Q1, "contents"))
                .withMessage("유저 정보가 존재하지 않습니다.");
    }

    @Test
    @DisplayName("질문 정보가 없으면 NotFoundException이 발생")
    void inputNullQuestionThenNotFoundException() {
        assertThatExceptionOfType(NotFoundException.class)
                .isThrownBy(() -> new Answer(JAVAJIGI, null, "contents"))
                .withMessage("질문 정보가 존재하지 않습니다.");
    }

    @Test
    @DisplayName("이미 삭제한 답변일 경우 NotFoundException이 발생")
    void validateRemovableThenNotFoundException() {
        Answer answer = new Answer(JAVAJIGI, Q1, "contents");
        answer.delete(JAVAJIGI);

        assertThatExceptionOfType(NotFoundException.class)
                .isThrownBy(() -> answer.delete(JAVAJIGI))
                .withMessage("이미 삭제된 답변입니다.");
    }

    @Test
    @DisplayName("답변 작성자와 로그인한 유저가 다른 경우 삭제가 불가능해 CannotDeleteException이 발생")
    void validateRemovableThenCannotDeleteException() {
        Answer answer = new Answer(JAVAJIGI, Q1, "contents");

        assertThatExceptionOfType(CannotDeleteException.class)
                .isThrownBy(() -> answer.delete(SANJIGI));
    }

    @Test
    @DisplayName("삭제 상태 값 변경 후 바뀌었는지 확인")
    void verifyChangeDeleteStatus() {
        Answer answer = new Answer(JAVAJIGI, Q1, "contents");
        answer.delete(JAVAJIGI);

        assertThat(answer.isDeleted()).isTrue();
    }
}
