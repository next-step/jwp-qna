package qna.domain;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import qna.CannotDeleteException;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatExceptionOfType;
import static qna.domain.AnswerTest.A1;
import static qna.domain.AnswerTest.A2;
import static qna.domain.UserTest.JAVAJIGI;
import static qna.domain.UserTest.SANJIGI;

class AnswersTest {

    @Test
    @DisplayName("정상적인 답변 등록 테스트")
    void addAnswers() {
        Answers answers = new Answers();
        answers.add(A1);
        answers.add(A2);

        Assertions.assertThat(answers.getAnswers()).size().isEqualTo(2);
    }

    @Test
    @DisplayName("정상적인 답변 삭제 테스트")
    void deleteAnswers() throws CannotDeleteException {
        Answers answers = new Answers();
        answers.add(A1);

        DeleteHistories delete = answers.delete(JAVAJIGI);
        Assertions.assertThat(delete.getDeleteHistories()).size().isEqualTo(1);
    }

    @Test
    @DisplayName("답변 삭제시 권한 오류")
    void cannotDeleteException() {
        Answers answers = new Answers();
        answers.add(A1);

        assertThatExceptionOfType(CannotDeleteException.class)
                .isThrownBy(() -> answers.delete(SANJIGI))
                .withMessageContaining("답변을 삭제할 수 없습니다.");
    }
}
