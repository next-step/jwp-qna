package qna.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static qna.domain.AnswerTest.A1;
import static qna.domain.QuestionTest.Q1;
import static qna.domain.UserTest.JAVAJIGI;
import static qna.domain.UserTest.SANJIGI;

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
    @DisplayName("동일한 답변(객체)가 달릴경우 IllegalArgumentException가 발생")
    void addSameIdOfAnswer() {
        answers.add(A1);

        assertThatIllegalArgumentException()
                .isThrownBy(() -> answers.add(A1))
                .withMessage("이미 등록된 답변입니다.");
    }

    @Test
    @DisplayName("답변 목록에 자기 자신 외 다른 유저의 답변이 있으면 CannotDeleteException이 발생")
    void existAnswerByOtherUserThenCannotDeleteException() {
        answers.add(new Answer(JAVAJIGI, Q1, "Answers Contents1"));
        answers.add(new Answer(SANJIGI, Q1, "Answers Contents2"));

        assertThatExceptionOfType(CannotDeleteException.class)
                .isThrownBy(() -> answers.deleteAnswers(JAVAJIGI));
    }
}
