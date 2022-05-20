package qna.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.junit.jupiter.api.Assertions.assertAll;
import static qna.domain.AnswerTest.A1;
import static qna.domain.DeleteHistory.mergeQuestionAndLinkedAnswer;
import static qna.domain.UserTest.JAVAJIGI;
import static qna.domain.UserTest.SANJIGI;

import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import qna.exception.CannotDeleteException;

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
    @DisplayName("질문 작성자와 로그인한 유저가 다른 경우 삭제가 불가능해 CannotDeleteException이 발생")
    void validateRemovableThenCannotDeleteException() {
        assertThatExceptionOfType(CannotDeleteException.class)
                .isThrownBy(() -> Q1.validateRemovable(SANJIGI))
                .withMessage("질문을 삭제할 권한이 없습니다.");
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

    @Test
    @DisplayName("내가 단 질문과 답변에 삭제할 목록이 정상인지 확인")
    void verifyReturnDeleteHistories() throws CannotDeleteException {
        List<DeleteHistory> deleteHistories = mergeQuestionAndLinkedAnswer(Q1, new Answers(Arrays.asList(A1)));
        DeleteHistories expected = new DeleteHistories(deleteHistories);
        Q1.addAnswer(A1);

        assertThat(Q1.toDeleteHistories(JAVAJIGI)).isEqualTo(expected);
    }
}
