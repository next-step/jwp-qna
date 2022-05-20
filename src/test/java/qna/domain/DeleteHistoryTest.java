package qna.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static qna.domain.AnswerTest.A1;
import static qna.domain.AnswerTest.A2;
import static qna.domain.DeleteHistory.mergeAnswer;
import static qna.domain.DeleteHistory.mergeQuestion;
import static qna.domain.DeleteHistory.mergeQuestionAndLinkedAnswer;
import static qna.domain.QuestionTest.Q1;
import static qna.domain.UserTest.JAVAJIGI;

import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import qna.exception.UnAuthorizedException;

class DeleteHistoryTest {

    @Test
    @DisplayName("객체 검증 비교")
    void verifyDeleteHistory() {
        DeleteHistoryContent deleteHistoryContent = DeleteHistoryContent.removeQuestion(Q1);
        DeleteHistory deleteHistory = new DeleteHistory(deleteHistoryContent, JAVAJIGI);

        assertThat(deleteHistory).isEqualTo(new DeleteHistory(deleteHistoryContent, JAVAJIGI));
    }

    @Test
    @DisplayName("삭제할 콘텐츠 정보가 없으면 IllegalArgumentException이 발생")
    void inputNullDeleteHistoryContent() {
        assertThatIllegalArgumentException()
                .isThrownBy(() -> new DeleteHistory(null, JAVAJIGI))
                .withMessage("삭제 콘텐츠 정보가 존재하지 않습니다.");
    }

    @Test
    @DisplayName("유저 정보가 없으면 UnAuthorizedException이 발생")
    void inputNullDeleter() {
        DeleteHistoryContent deleteHistoryContent = DeleteHistoryContent.removeQuestion(Q1);

        assertThatThrownBy(() -> new DeleteHistory(deleteHistoryContent, null))
                .isInstanceOf(UnAuthorizedException.class)
                .hasMessage("유저 정보가 존재하지 않습니다.");
    }

    @Test
    @DisplayName("삭제할 질문이 타입이랑 아이디가 제대로 나왔는지 검증")
    void verifyMergeQuestion() {
        DeleteHistoryContent expected = DeleteHistoryContent.removeQuestion(Q1);
        DeleteHistory actual = mergeQuestion(Q1);

        assertAll(
                () -> assertThat(actual.contentInformation()).isEqualTo(expected),
                () -> assertThat(actual.getDeleter()).isEqualTo(Q1.getWriter())
        );
    }

    @Test
    @DisplayName("삭제할 답변이 타입이랑 아이디가 제대로 나왔는지 검증")
    void verifyMergeAnswer() {
        DeleteHistoryContent expected = DeleteHistoryContent.removeAnswer(A1);
        DeleteHistory actual = DeleteHistory.mergeAnswer(A1);

        assertAll(
                () -> assertThat(actual.contentInformation()).isEqualTo(expected),
                () -> assertThat(actual.getDeleter()).isEqualTo(Q1.getWriter())
        );
    }

    @Test
    @DisplayName("삭제할 질문과 답변이 합쳐서 잘 나왔는지 검증")
    void verifyMergeQuestionAndAnswer() {
        Answers answers = new Answers(Arrays.asList(A1, A2));
        List<DeleteHistory> deleteHistories = mergeQuestionAndLinkedAnswer(Q1, answers);

        assertThat(deleteHistories).containsExactly(mergeQuestion(Q1), mergeAnswer(A1), mergeAnswer(A2));
    }
}
