package qna.domain;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import qna.CannotDeleteException;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatExceptionOfType;
import static org.junit.jupiter.api.Assertions.assertAll;

public class AnswerTest {
    public static final Answer A1 = new Answer(UserTest.JAVAJIGI, QuestionTest.Q1, "Answers Contents1");
    public static final Answer A2 = new Answer(UserTest.SANJIGI, QuestionTest.Q1, "Answers Contents2");

    @Test
    @DisplayName("정상적인 삭제 테스트")
    void delete() throws CannotDeleteException {
        A1.delete(UserTest.JAVAJIGI);
        A2.delete(UserTest.SANJIGI);

        assertAll(
                () -> Assertions.assertThat(A1.isDeleted()).isTrue(),
                () -> Assertions.assertThat(A2.isDeleted()).isTrue()
        );
    }

    @Test
    @DisplayName("다른 유저가 삭제를 시도한 경우 실패 테스트")
    void deleteFalse() {
        assertThatExceptionOfType(CannotDeleteException.class)
                .isThrownBy(() -> A1.delete(UserTest.SANJIGI))
                .withMessageContaining("답변을 삭제할 수 없습니다.");
    }
}
