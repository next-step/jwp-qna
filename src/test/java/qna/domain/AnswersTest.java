package qna.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static qna.domain.AnswerTest.A1;
import static qna.domain.AnswerTest.A2;
import static qna.domain.UserTest.JAVAJIGI;

import java.util.Collections;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import qna.exception.CannotDeleteException;

class AnswersTest {
    private Answers answers;

    @BeforeEach
    void setUp() {
        answers = new Answers();
    }

    @Test
    @DisplayName("객체 생성 확인")
    void verifyAnswers() {
        answers.add(A1);

        assertThat(answers).isEqualTo(new Answers(Collections.singletonList(A1)));
    }

    @Test
    @DisplayName("답변 목록에 답변 넣은 후 사이즈 확인")
    void addAnswerThenCheckSize() {
        answers.add(A1);

        assertThat(answers.list()).hasSize(1);
    }

    @Test
    @DisplayName("답변 목록에 해당 답변이 들어있는지 확인")
    void containAnswer() {
        answers.add(A1);

        assertThat(answers.contains(A1)).isTrue();
    }

    @Test
    @DisplayName("답변 목록에 자기 자신 외 다른 유저의 답변이 있으면 CannotDeleteException이 발생")
    void existAnswerByOtherUserThenCannotDeleteException() {
        answers.add(A1);
        answers.add(A2);

        assertThatExceptionOfType(CannotDeleteException.class)
                .isThrownBy(() -> answers.validateExistAnswerByOtherUser(JAVAJIGI));
    }
}
