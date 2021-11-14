package qna.domain;

import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import qna.CannotDeleteException;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class AnswersTest {

    private Answer answer;
    private Answer answer2;

    @BeforeEach
    void setUp() {
        answer = new Answer(UserTest.JAVAJIGI, QuestionTest.Q1, "Answers Contents1");
        answer2 = new Answer(UserTest.JAVAJIGI, QuestionTest.Q2, "Answers Contents2");
    }

    @DisplayName("로그인 사용자와 질문한 사람이 같은 경우 답변 삭제 성공")
    @Test
    void deleteSuccessOneAnswer() throws CannotDeleteException {
        Answers answers = new Answers(Arrays.asList(answer));

        List<DeleteHistory> deleteHistories = answers.delete(UserTest.JAVAJIGI);

        assertThat(answer.isDeleted()).isTrue();
        assertThat(deleteHistories).hasSize(1);
    }

    @DisplayName("로그인 사용자와 질문한 사람이 같은 경우 답변 여러개 삭제 성공")
    @Test
    void deleteSuccessAnswers() throws CannotDeleteException {
        Answers answers = new Answers(Arrays.asList(answer, answer2));

        List<DeleteHistory> deleteHistories = answers.delete(UserTest.JAVAJIGI);

        assertThat(answer.isDeleted()).isTrue();
        assertThat(answer2.isDeleted()).isTrue();
        assertThat(deleteHistories).hasSize(2);
    }

    @DisplayName("로그인 사용자와 질문한 사람이 다른 경우 예외 발생")
    @Test
    void deleteFailed() {
        Answers answers = new Answers(Arrays.asList(answer));

        ThrowableAssert.ThrowingCallable throwingCallable = () -> answers.delete(UserTest.SANJIGI);
        assertThatThrownBy(throwingCallable)
                .isInstanceOf(CannotDeleteException.class)
                .hasMessageContaining("다른 사람이 쓴 답변이 있어 삭제할 수 없습니다.");
    }

}
