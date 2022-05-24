package qna.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.junit.jupiter.api.Assertions.assertAll;
import static qna.domain.AnswerTest.A1;
import static qna.domain.UserTest.JAVAJIGI;
import static qna.domain.UserTest.SANJIGI;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import qna.exception.CannotDeleteException;
import qna.exception.NotFoundException;

public class QuestionTest {
    public static final Question Q1 = new Question(1L, "title1", "contents1").writeBy(UserTest.JAVAJIGI);
    public static final Question Q2 = new Question(2L, "title2", "contents2").writeBy(UserTest.SANJIGI);

    @Test
    @DisplayName("객체 생성 확인")
    void verifyQuestion() {
        Question idNullQuestion = new Question("title1", "contents1").writeBy(UserTest.JAVAJIGI);

        assertAll(
                () -> assertThat(Q1).isEqualTo(Q1),
                () -> assertThat(Q1).isNotEqualTo(idNullQuestion)
        );
    }

    @Test
    @DisplayName("이미 삭제한 질문일 경우 NotFoundException이 발생")
    void validateRemovableThenNotFoundException() {
        Q1.delete(JAVAJIGI);

        assertThatExceptionOfType(NotFoundException.class)
                .isThrownBy(() -> Q1.delete(JAVAJIGI))
                .withMessage("이미 삭제된 질문입니다.");
    }

    @Test
    @DisplayName("질문 작성자와 로그인한 유저가 다른 경우 삭제가 불가능해 CannotDeleteException이 발생")
    void validateRemovableThenCannotDeleteException() {
        assertThatExceptionOfType(CannotDeleteException.class)
                .isThrownBy(() -> Q2.delete(JAVAJIGI))
                .withMessage("질문을 삭제할 권한이 없습니다.");
    }

    @Test
    @DisplayName("현재 질문과 등록하려는 답변의 질문이 일치하지 않는 경우 IllegalArgumentExceptiond이 발생")
    void differentBetweenQuestionIdOfAnswerAndThisQuestionId() {
        Answer answerOfQ2 = new Answer(SANJIGI, Q2, "Answers Contents2");
        assertThatIllegalArgumentException()
                .isThrownBy(() -> Q1.addAnswer(answerOfQ2))
                .withMessage("현재 질문과 등록하려는 답변에 대한 질문이 일치하지 않습니다.");
    }

    @Test
    @DisplayName("해당 질문에 답변을 달아주고 제대로 매핑되었는지 확인")
    void verifyAddAnswer() {
        Q1.addAnswer(A1);

        assertAll(
                () -> assertThat(Q1.getAnswers().contains(A1)).isTrue(),
                () -> assertThat(A1.getQuestion()).isEqualTo(Q1)
        );
    }
}
