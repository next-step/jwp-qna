package qna.domain;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import qna.CannotDeleteException;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatExceptionOfType;
import static qna.domain.UserTest.JAVAJIGI;
import static qna.domain.UserTest.SANJIGI;

public class QuestionTest {
    public static final Question Q1 = new Question("title1", "contents1").writeBy(JAVAJIGI);
    public static final Question Q2 = new Question("title2", "contents2").writeBy(SANJIGI);

    @Test
    @DisplayName("정상적인 삭제 테스트")
    void delete() throws CannotDeleteException {
        DeleteHistories delete = Q1.delete(JAVAJIGI);

        Assertions.assertThat(delete.getDeleteHistories()).size().isEqualTo(1);
    }

    @Test
    @DisplayName("다른 유저가 삭제를 시도한 경우 실패 테스트")
    void cannotDeleteException() {
        assertThatExceptionOfType(CannotDeleteException.class)
                .isThrownBy(() -> Q1.delete(SANJIGI))
                .withMessageContaining("질문을 삭제할 권한이 없습니다.");
    }

    @Test
    @DisplayName("답변과 질문을 동시에 삭제하는 테스트")
    void deleteAnswerAndQuestion() throws CannotDeleteException {
        // given
        Question question = new Question("title1", "contents1").writeBy(SANJIGI);
        Answer answer = new Answer(JAVAJIGI, question, "Answer~");
        question.addAnswer(answer);
        question.getAnswers().delete(JAVAJIGI);

        // when
        DeleteHistories delete = question.delete(SANJIGI);
        Assertions.assertThat(delete.getDeleteHistories()).size().isEqualTo(1);
    }

    @Test
    @DisplayName("질문 삭제시 답변이 남아있는 경우 실패 테스트")
    void deleteQuestionCannotDeleteException() {
        Question question = new Question("title1", "contents1").writeBy(SANJIGI);
        Answer answer = new Answer(JAVAJIGI, question, "Answer~");
        question.addAnswer(answer);

        assertThatExceptionOfType(CannotDeleteException.class)
                .isThrownBy(() -> question.delete(SANJIGI))
                .withMessageContaining("답변을 삭제할 수 없습니다.");
    }
}
